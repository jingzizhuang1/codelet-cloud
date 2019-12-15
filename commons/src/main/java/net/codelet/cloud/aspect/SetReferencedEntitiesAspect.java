package net.codelet.cloud.aspect;

import net.codelet.cloud.annotation.ReferenceEntity;
import net.codelet.cloud.annotation.SetReferencedEntities;
import net.codelet.cloud.api.BaseQueryApi;
import net.codelet.cloud.entity.BaseEntity;
import net.codelet.cloud.helper.AsyncHelper;
import net.codelet.cloud.repository.BaseRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import static net.codelet.cloud.constant.HttpRequestAttributes.REFERENCED_ENTITIES;
import static net.codelet.cloud.constant.HttpRequestHeaders.SET_REFERENCED_ENTITIES;
import static net.codelet.cloud.constant.HttpRequestHeaders.SET_REFERENCED_ENTITIES_OFF;

/**
 * 设置关联数据。
 */
@Aspect
@Component
public class SetReferencedEntitiesAspect extends BaseAspect {

    // Spring 应用上下文对象
    private final ApplicationContext applicationContext;

    // 项目顶级包名
    private final String rootPackageName;

    // 异步处理 Helper
    private final AsyncHelper asyncHelper;

    /**
     * 构造方法。
     * @param applicationContext Spring 应用上下文对象
     * @param asyncHelper        异步处理 Helper
     */
    @Autowired
    public SetReferencedEntitiesAspect(
        ApplicationContext applicationContext,
        AsyncHelper asyncHelper
    ) {
        this.applicationContext = applicationContext;
        List<String> packageName = new ArrayList<>(Arrays.asList(this.getClass().getPackage().getName().split("\\.")));
        packageName.remove(packageName.size() - 1);
        this.rootPackageName = String.join(".", packageName);
        this.asyncHelper = asyncHelper;
    }

    /**
     * 定义切入点：使用 @SetReferenceData 注解的方法。
     */
    @Pointcut("@annotation(annotation)")
    public void controller(SetReferencedEntities annotation) {
    }

    /**
     * 设置关联数据。
     * @param point      切入点信息
     * @param annotation 注解设置
     * @param returning  返回结果
     */
    @AfterReturning(
        value = "controller(annotation)",
        argNames = "point,annotation,returning",
        returning = "returning"
    )
    public void doAfterReturning(
        final JoinPoint point,
        final SetReferencedEntities annotation,
        final Object returning
    ) {
        // 取得 HTTP 请求实例
        HttpServletRequest request = getRequest();

        if (returning == null
            || SET_REFERENCED_ENTITIES_OFF.equals(request.getHeader(SET_REFERENCED_ENTITIES))) {
            return;
        }

        Collection<?> data;

        if (returning instanceof Page) {
            data = ((Page) returning).getContent();
        } else if (returning instanceof Collection) {
            data = (Collection) returning;
        } else if (returning instanceof Object[]) {
            data = Arrays.asList((Object[]) returning);
        } else {
            data = Collections.singleton(returning);
        }

        if (data.size() == 0) {
            return;
        }

        // 实体 ID 缓存
        Map<String, Set<String>> referencedEntityIDs = new HashMap<>();

        // 取得已获取实体数据的缓存
        // noinspection unchecked
        Map<String, Object> referencedEntities = (Map<String, Object>) request.getAttribute(REFERENCED_ENTITIES);

        if (referencedEntities == null) {
            referencedEntities = new HashMap<>();
            request.setAttribute(REFERENCED_ENTITIES, referencedEntities);
        }

        // 取得返回结果中设置为关联实体数据（@ReferenceEntity）的字段
        for (Object entity : data) {
            if (entity == null) {
                continue;
            }
            setReferencedEntityIDs(entity, entity.getClass(), referencedEntityIDs, referencedEntities);
        }

        // 取得关联实体并添加到缓存中
        setReferencedEntities(referencedEntityIDs, referencedEntities);
    }

    /**
     * 取得返回结果中设置为关联实体数据（@ReferenceEntity）的字段。
     * @param entity              返回的实体信息
     * @param clazz               返回的实体类型
     * @param referencedEntityIDs 关联的实体 ID 的缓存
     * @param referencedEntities  已取得的关联实体数据的缓存
     */
    private static void setReferencedEntityIDs(
        final Object entity,
        final Class<?> clazz,
        final Map<String, Set<String>> referencedEntityIDs,
        final Map<String, ?> referencedEntities
    ) {
        if (clazz == null) {
            return;
        }

        setReferencedEntityIDs(entity, clazz.getSuperclass(), referencedEntityIDs, referencedEntities);

        ReferenceEntity referenceEntity;
        String referencedEntityId;

        // 遍历返回数据的每一个属性
        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);

                if (field.get(entity) == null) {
                    continue;
                }

                // 若属性为集合，遍历每一个元素
                if (Collection.class.isAssignableFrom(field.getType())) {
                    for (Object item : (Collection) field.get(entity)) {
                        if (item != null) {
                            setReferencedEntityIDs(item, item.getClass(), referencedEntityIDs, referencedEntities);
                        }
                    }
                // 否则，若属性为数组，遍历每一个元素
                } else if (field.getType().isArray()) {
                    for (Object item : (Object[]) field.get(entity)) {
                        if (item != null) {
                            setReferencedEntityIDs(item, item.getClass(), referencedEntityIDs, referencedEntities);
                        }
                    }
                // 否则，若属性拥有 @ReferenceEntity 注解则将其作为引用对象的 ID
                } else {
                    referenceEntity = field.getAnnotation(ReferenceEntity.class);
                    if (referenceEntity == null || field.getType() != String.class) {
                        continue;
                    }
                    referencedEntityId = (String) field.get(entity);
                    if (referencedEntityId == null
                        || referencedEntities.containsKey(referencedEntityId)) {
                        continue;
                    }
                    referencedEntityIDs.putIfAbsent(referenceEntity.value(), new HashSet<>());
                    referencedEntityIDs.get(referenceEntity.value()).add(referencedEntityId);
                }
            } catch (IllegalAccessException ignored) {
            }
        }
    }

    /**
     * 取得关联实体并添加到缓存中。
     * @param referencedEntityIDs 关联的实体 ID 的缓存
     * @param referencedEntities  已取得的关联实体数据的缓存
     */
    private void setReferencedEntities(
        final Map<String, Set<String>> referencedEntityIDs,
        final Map<String, Object> referencedEntities
    ) {
        List<Supplier<List<? extends BaseEntity>>> entityBatchGetTasks = new ArrayList<>();

        for (String entityName : referencedEntityIDs.keySet()) {
            // 取得关联实体的 ID 的集合
            Set<String> entityIDs = referencedEntityIDs.get(entityName);

            String repositoryName = String.format("%sQueryRepository", entityName);
            Object repository = applicationContext.containsBean(repositoryName)
                ? applicationContext.getBean(repositoryName)
                : null;

            // 尝试通过数据仓库取得实体数据
            if (repository instanceof BaseRepository) {
                entityBatchGetTasks.add(() -> ((BaseRepository<? extends BaseEntity>) repository).findByIdIn(entityIDs));
            // 若未能取得实体数据则尝试通过查询 API 取得实体数据
            } else {
                String queryApiName = String.format(
                    "%s.%s.query.api.%sQueryApi",
                    rootPackageName,
                    entityName,
                    entityName.substring(0, 1).toUpperCase() + entityName.substring(1)
                );
                Object queryApi = applicationContext.containsBean(queryApiName)
                    ? applicationContext.getBean(queryApiName)
                    : null;
                if (queryApi instanceof BaseQueryApi) {
                    entityBatchGetTasks.add(() -> ((BaseQueryApi<? extends BaseEntity>) queryApi).batchGet(entityIDs));
                }
            }
        }

        if (entityBatchGetTasks.size() == 0) {
            return;
        }

        // 执行任务队列中的任务，并将取得的实体设置到缓存中
        try {
            asyncHelper.allOf(entityBatchGetTasks).forEach(entities -> {
                if (entities == null) {
                    return;
                }
                entities.forEach(entity -> {
                    referencedEntities.put(entity.getId(), entity);
                });
            });
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace(System.err);
        }
    }
}
