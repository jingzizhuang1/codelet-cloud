package net.codelet.cloud.verification.command.service;

import net.codelet.cloud.dto.OperatorDTO;
import net.codelet.cloud.verification.command.dto.VerificationConfigurationUpdateDTO;
import net.codelet.cloud.vo.VerificationPurpose;
import net.codelet.cloud.verification.vo.VerificationType;

/**
 * 验证码配置服务。
 */
public interface VerificationConfigurationCommandService {

    /**
     * 检查是否存在验证码配置信息。
     * @param keyType 验证码类型
     * @param purpose 验证码用途
     * @param deleted 是否已删除
     * @return 验证码配置信息是否存在
     */
    boolean exists(
        VerificationType keyType,
        VerificationPurpose purpose,
        Boolean deleted
    );

    /**
     * 设置验证码配置。
     * @param operator  操作者信息
     * @param keyType   验证类型
     * @param purpose   验证码用途
     * @param revision  更新版本号
     * @param updateDTO 验证码配置信息
     * @return 是否创建了新的配置
     */
    boolean set(
        OperatorDTO operator,
        VerificationType keyType,
        VerificationPurpose purpose,
        Long revision,
        VerificationConfigurationUpdateDTO updateDTO
    );

    /**
     * 停用验证码配置。
     * @param operator 操作者信息
     * @param keyType  验证类型
     * @param purpose  验证码用途
     * @param revision 更新版本号
     */
    void disable(
        OperatorDTO operator,
        VerificationType keyType,
        VerificationPurpose purpose,
        Long revision
    );

    /**
     * 启用验证码配置。
     * @param operator 操作者信息
     * @param keyType  验证类型
     * @param purpose  验证码用途
     * @param revision 更新版本号
     */
    void enable(
        OperatorDTO operator,
        VerificationType keyType,
        VerificationPurpose purpose,
        Long revision
    );

    /**
     * 删除验证码配置。
     * @param operator 操作者信息
     * @param keyType  验证类型
     * @param purpose  验证码用途
     * @param revision 更新版本号
     */
    void delete(
        OperatorDTO operator,
        VerificationType keyType,
        VerificationPurpose purpose,
        Long revision
    );
}
