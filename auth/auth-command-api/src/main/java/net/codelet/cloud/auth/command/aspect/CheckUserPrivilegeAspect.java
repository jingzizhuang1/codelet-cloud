package net.codelet.cloud.auth.command.aspect;

import net.codelet.cloud.annotation.CheckUserPrivilege;
import net.codelet.cloud.aspect.BaseAspect;
import net.codelet.cloud.auth.command.service.AccessTokenService;
import net.codelet.cloud.dto.ContextDTO;
import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.error.AccessTokenInvalidError;
import net.codelet.cloud.error.NoPrivilegeError;
import net.codelet.cloud.error.UnauthorizedError;
import net.codelet.cloud.util.AspectUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 用户权限检查切面。
 * 根据控制器上 @CheckUserPrivilege 注解信息，检查用户是否对指定组织的指定类型的指定资源拥有所需的全部权限。
 * 若未指定任何权限且要求用户必须登录则仅检查用户提供的访问令牌是否有效。
 */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE + 2)
@Component
public class CheckUserPrivilegeAspect extends BaseAspect {

    private final AccessTokenService accessTokenService;

    @Autowired
    public CheckUserPrivilegeAspect(
        AccessTokenService accessTokenService
    ) {
        this.accessTokenService = accessTokenService;
    }

    /**
     * 定义切入点：使用 @CheckUserPrivilege 注解的方法。
     */
    @Pointcut("@annotation(annotation)")
    public void controller(CheckUserPrivilege annotation) {
    }

    /**
     * 检查用户权限。
     * @param point      切入点信息
     * @param annotation 注解设置
     */
    @Before(value = "controller(annotation)", argNames = "point,annotation")
    public void doBefore(
        final JoinPoint point,
        final CheckUserPrivilege annotation
    ) {
        // 根据访问令牌取得登录用户信息
        OperatorDTO operator = getUser();

        // 若未认证，且接口要求必须通过认证则返回未授权错误，否则正常结束
        if (operator == null) {
            if (annotation.required()
                || annotation.administrator()
                || annotation.owner()
                || annotation.member()
            ) {
                throw new UnauthorizedError();
            }
            return;
        }

        // 系统管理员可以执行所有操作
        if (operator.isAdministrator()) {
            return;
        }

        // 系统管理员身份检查
        if (annotation.administrator()) {
            throw new NoPrivilegeError();
        }

        // 取得切入点的资源所有者参数和资源所属组织参数
        AspectUtils.ParameterHolder parameters = AspectUtils
            .getParameters(point, annotation.ownerId(), annotation.orgId());

        // 资源所有者身份检查
        if (annotation.owner()
            && !operator.getId().equals(parameters.getAsString(annotation.ownerId()))) {
            throw new NoPrivilegeError();
        }

        // 资源所属组织成员身份检查
        if (annotation.member() || annotation.privileges().length > 0) {
            // TODO: privilegeQueryApi.checkPrivilege(operator.getId(), parameters.getAsString(annotation.orgId()), annotation.privileges())
        }
    }

    /**
     * 根据访问令牌取得登录用户信息。
     * @return 登录用户信息
     */
    private OperatorDTO getUser() {
        // 取得上下问对象
        ContextDTO context = getContext();

        // 尝试取得用户信息，若已取得则将其返回
        OperatorDTO operator = context.getOperator();
        if (operator != null) {
            return operator;
        }

        // 取得认证信息
        String authorizationType = "Bearer ";
        String authorization = context.getAuthorization();
        if (authorization == null) {
            return null;
        }

        // 授权类型不匹配时返回授权信息无效错误
        if (!authorization.startsWith(authorizationType)) {
            throw new AccessTokenInvalidError();
        }

        // 校验访问令牌，取得所有者用户信息
        operator = accessTokenService.claim(
            context,
            authorization.substring(authorizationType.length())
        );

        // 将用户信息设置到上下文中
        context.setOperator(operator);

        return operator;
    }
}
