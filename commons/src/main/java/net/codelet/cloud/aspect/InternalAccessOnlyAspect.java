package net.codelet.cloud.aspect;

import net.codelet.cloud.annotation.InternalAccessOnly;
import net.codelet.cloud.error.AccessTokenInvalidError;
import net.codelet.cloud.error.UnauthorizedError;
import net.codelet.cloud.util.RandomUtils;
import net.codelet.cloud.util.ReflectionUtils;
import net.codelet.cloud.util.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.*;

import static net.codelet.cloud.constant.HttpRequestHeaders.ACCESS_TOKEN;

/**
 * 检查通过 FeignClient 访问时，请求头中的 X-Access-Token 的令牌是否有效。
 */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Component
public class InternalAccessOnlyAspect extends BaseAspect {

    private final StringRedisTemplate stringRedisTemplate;
    private final ValueOperations<String, String> redisValueOperations;

    @Autowired
    public InternalAccessOnlyAspect(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        redisValueOperations = stringRedisTemplate.opsForValue();
    }

    /**
     * 定义切入点：使用 @InternalAccessOnly 注解的方法。
     */
    @Pointcut("@annotation(annotation)")
    public void controller(InternalAccessOnly annotation) {
    }

    /**
     * 检查 X-Access-Token 请求头。
     * @param point      切入点信息
     * @param annotation 注解
     */
    @Before(value = "controller(annotation)", argNames = "point,annotation")
    public void doBefore(final JoinPoint point, final InternalAccessOnly annotation) {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return;
        }

        Method method = ((MethodSignature) point.getSignature()).getMethod();
        Class<?> declaringClass = method.getDeclaringClass();

        // 当前方法为 Feign 客户端的方法时，生成令牌
        if (declaringClass.getAnnotation(FeignClient.class) != null) {
            String accessToken = Long.toString(System.currentTimeMillis(), 16) + ":" + Base64.getEncoder().encodeToString(RandomUtils.getBytes(24));
            redisValueOperations.set(accessTokenRedisKey(accessToken), "", Duration.ofSeconds(15));
            request.setAttribute(ACCESS_TOKEN, accessToken);
        // 当前方法为 REST 控制器的方法时，校验令牌
        } else if (declaringClass.getAnnotation(RestController.class) != null && isRemoteCall(request, method)) {
            String accessToken = request.getHeader(ACCESS_TOKEN);
            if (StringUtils.isEmpty(accessToken)) {
                throw new UnauthorizedError();
            }
            if (!Boolean.TRUE.equals(stringRedisTemplate.delete(accessTokenRedisKey(accessToken)))) {
                throw new AccessTokenInvalidError();
            }
        }
    }

    /**
     * 生成令牌在 Redis 中的 Key。
     * @param accessToken 令牌
     * @return 令牌在 Redis 中的 Key
     */
    private static String accessTokenRedisKey(String accessToken) {
        return "x-access-token:" + accessToken;
    }

    // 路由映射注解类型集合
    private static final Set<Class<? extends Annotation>> ACCEPT_ANNOTATION_TYPES = new HashSet<>(Arrays.asList(
        RequestMapping.class,
        GetMapping.class,
        PostMapping.class,
        PutMapping.class,
        DeleteMapping.class,
        PatchMapping.class
    ));

    // 控制器方法路由映射解析结果缓存
    private static final Map<String, Map<String, List<String>>> ROUTES = new HashMap<>();

    /**
     * 判断客户端是否通过远程调用的方式执行当前控制器方法。
     * TODO: 由于以单实例方式启动时 FeignClient 没有被实例化，不会被执行，内部访问令牌也不会被生成。
     * TODO: 此时若要避免控制器方法执行时不会因为无法取得内部令牌而报错需要判断是否为远程调用。
     * TODO: 为了判断是远程调用（微服务时）还是本地调用（单实例时），
     * TODO: 以下逻辑通过反射机制对控制器方法的所有路由配置注解进行解析，
     * TODO: 并判断当前请求的路由（请求方法+请求路径）与控制器方法所处理的路由是否一致。
     * TODO: 这些注解包括控制器方法自身的注解，类型所实现的接口的响应方法的注解，以及类型上的注解。
     * TODO: 是否可通过其他方法判断是否为远程调用？例如是否可通过控制器方法的调用栈来判断是否为远程调用？
     * @param request HTTP 请求
     * @param handler 控制器方法。
     * @return 判断结果
     */
    private static boolean isRemoteCall(HttpServletRequest request, Method handler) {
        if (!ROUTES.containsKey(handler.toString())) {
            resolveRequestMappingAnnotations(handler);
        }

        List<String> paths = ROUTES.get(handler.toString()).get(request.getMethod().toUpperCase());
        if (paths == null || paths.size() == 0) {
            return false;
        }

        String requestURI = request.getRequestURI();
        for (String path : paths) {
            if (requestURI.matches(path)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 解析控制器方法的路由映射注解。
     * @param handler 控制器方法
     */
    private static void resolveRequestMappingAnnotations(Method handler) {
        // 取得控制器类型的路由映射注解
        List<Annotation> controllerAnnotations = new ArrayList<>();
        for (Annotation annotation : handler.getDeclaredAnnotations()) {
            if (!ACCEPT_ANNOTATION_TYPES.contains(annotation.annotationType())) {
                continue;
            }
            controllerAnnotations.add(annotation);
            break;
        }

        // 取得控制器方法的路由映射注解
        Map<Class<? extends Annotation>, Annotation> handlerAnnotations = getRequestMappingAnnotations(handler);

        final Map<String, List<String>> routes = new HashMap<>();

        if (controllerAnnotations.size() == 0 && handlerAnnotations.size() == 0) {
            ROUTES.put(handler.toString(), routes);
            return;
        }

        // 根据控制器方法的路由配置设置路由
        handlerAnnotations.forEach((annotationType, annotation) -> {
            RequestMethod[] requestMethods = readRequestMethodsFromRequestMappingAnnotation(annotation);
            String[] paths = readPathsFromRequestMappingAnnotation(annotation);
            if (requestMethods != null && paths != null) {
                for (RequestMethod requestMethod : requestMethods) {
                    routes
                        .computeIfAbsent(requestMethod.name().toUpperCase(), requestMethodName -> new ArrayList<>())
                        .addAll(Arrays.asList(paths));
                }
            }
        });

        // 根据控制器类型的路由配置设置路由
        controllerAnnotations.forEach(annotation -> {
            RequestMethod[] requestMethods = readRequestMethodsFromRequestMappingAnnotation(annotation);
            String[] prefixes = readPathsFromRequestMappingAnnotation(annotation);
            if (requestMethods != null && prefixes != null) {
                for (RequestMethod requestMethod : requestMethods) {
                    for (String prefix : prefixes) {
                        List<String> paths = routes
                            .computeIfAbsent(requestMethod.name().toUpperCase(), requestMethodName -> new ArrayList<>());
                        for (int i = 0; i < paths.size(); i++) {
                            paths.set(i, prefix + paths.get(i));
                        }
                    }
                }
            }
        });

        // 将路径格式化为正则表达式（如将参数占位符 {userId} 替换为 (.+?) 正则表达式）
        routes.forEach((requestMethod, paths) -> {
            for (int i = 0; i < paths.size(); i++) {
                paths.set(i, "^" + paths.get(i).replaceAll("\\{.+?}", "(.+?)") + "$");
            }
        });

        ROUTES.put(handler.toString(), routes);
    }

    /**
     * 取得控制器方法的路由映射配置注解。
     * @param handler 控制器方法
     * @return 注解类型与注解实例的映射表
     */
    private static Map<Class<? extends Annotation>, Annotation> getRequestMappingAnnotations(Method handler) {
        Map<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();

        // 获取 FeignClient 接口方法的注解
        for (Class<?> interfaceType : handler.getDeclaringClass().getInterfaces()) {
            if (interfaceType.getDeclaredAnnotation(FeignClient.class) == null) {
                continue;
            }
            try {
                for (Annotation annotation : interfaceType
                    .getMethod(handler.getName(), handler.getParameterTypes())
                    .getDeclaredAnnotations()
                ) {
                    if (!ACCEPT_ANNOTATION_TYPES.contains(annotation.annotationType())) {
                        continue;
                    }
                    annotations.put(annotation.annotationType(), annotation);
                }
            } catch (NoSuchMethodException ignored) {
            }
        }

        // 获取自身的注解
        for (Annotation annotation : handler.getDeclaredAnnotations()) {
            if (!ACCEPT_ANNOTATION_TYPES.contains(annotation.annotationType())) {
                continue;
            }
            annotations.put(annotation.annotationType(), annotation);
        }

        return annotations;
    }

    /**
     * 取得路由映射注解中设置的请求方法。
     * @param annotation 路由映射注解
     * @return 请求方法数组
     */
    private static RequestMethod[] readRequestMethodsFromRequestMappingAnnotation(Annotation annotation) {
        RequestMethod[] requestMethods = null;
        try {
            requestMethods = (RequestMethod[]) ReflectionUtils.invoke(annotation, "method");
        } catch (NoSuchMethodException e) {
            try {
                requestMethods = (RequestMethod[]) ReflectionUtils
                    .invoke(annotation.annotationType().getDeclaredAnnotation(RequestMapping.class), "method");
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
            }
        } catch (InvocationTargetException | IllegalAccessException ignored) {
        }
        return requestMethods;
    }

    /**
     * 取得路由映射注解中的请求路径。
     * @param annotation 路由映射注解
     * @return 请求路径数组
     */
    private static String[] readPathsFromRequestMappingAnnotation(Annotation annotation) {
        String[] requestURIs = null;
        try {
            requestURIs = (String[]) ReflectionUtils.invoke(annotation, "value");
            if (requestURIs.length == 0) {
                requestURIs = (String[]) ReflectionUtils.invoke(annotation, "path");
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
        }
        return requestURIs;
    }
}
