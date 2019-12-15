package net.codelet.cloud.user.command.service;

import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.user.command.dto.UserCreateDTO;
import net.codelet.cloud.user.command.dto.UserSignUpDTO;
import net.codelet.cloud.user.command.dto.UserUpdateDTO;
import net.codelet.cloud.user.command.entity.UserCommandEntity;
import net.codelet.cloud.vo.UserType;

/**
 * 用户服务接口。
 */
public interface UserCommandService {

    /**
     * 创建系统用户。
     */
    void createSystemUsers();

    /**
     * 创建用户账号。
     * @param operator  操作者信息
     * @param userType  用户类型
     * @param signUpDTO 用户创建表单
     * @return 用户实体信息
     */
    UserCommandEntity create(
        OperatorDTO operator,
        UserType userType,
        UserSignUpDTO signUpDTO
    );

    /**
     * 创建用户账号。
     * @param operator  操作者信息
     * @param userType  用户类型
     * @param createDTO 用户创建表单
     * @return 用户实体信息
     */
    UserCommandEntity create(
        OperatorDTO operator,
        UserType userType,
        UserCreateDTO createDTO
    );

    /**
     * 更新用户账号。
     * @param operator  操作者信息
     * @param userId    用户 ID
     * @param revision  更新版本号
     * @param updateDTO 用户更新表单
     */
    void update(
        OperatorDTO operator,
        String userId,
        Long revision,
        UserUpdateDTO updateDTO
    );

    /**
     * 停用账号。
     * @param operator 操作者信息
     * @param userId   用户 ID
     * @param revision 更新版本号
     */
    void disable(
        OperatorDTO operator,
        String userId,
        Long revision
    );

    /**
     * 启用账号。
     * @param operator 操作者信息
     * @param userId   用户 ID
     * @param revision 更新版本号
     */
    void enable(
        OperatorDTO operator,
        String userId,
        Long revision
    );

    /**
     * 注销账号。
     * @param operator 操作者信息
     * @param userId   用户 ID
     * @param revision 更新版本号
     */
    void delete(
        OperatorDTO operator,
        String userId,
        Long revision
    );
}
