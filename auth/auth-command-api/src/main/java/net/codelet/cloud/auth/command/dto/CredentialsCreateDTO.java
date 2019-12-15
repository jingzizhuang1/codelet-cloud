package net.codelet.cloud.auth.command.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.auth.vo.CredentialType;
import net.codelet.cloud.dto.BaseDTO;
import net.codelet.cloud.error.ValidationError;
import net.codelet.cloud.util.StringUtils;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 认证凭证批量创建表单数据。
 */
public class CredentialsCreateDTO extends BaseDTO {

    private static final long serialVersionUID = 3129530389584802841L;

    @Getter
    @Setter
    @Size(min = 1)
    @ApiModelProperty("认证凭证列表")
    private List<CredentialCreateDTO> credentials = new ArrayList<>();

    @Getter
    @Setter
    @Size(min = 6, max = 32)
    @ApiModelProperty("登录密码")
    private String password;

    /**
     * 向认证凭证列表中添加认证凭证。
     * @param type       凭证类型
     * @param credential 凭证
     */
    public void addCredential(CredentialType type, String credential) {
        if (StringUtils.isEmpty(credential)) {
            return;
        }
        if (!credential.matches(type.getPattern())) {
            throw new ValidationError("error.credential.credential-is-invalid"); // TODO: set message
        }
        credentials.add(new CredentialCreateDTO(type, credential));
    }

    /**
     * 认证凭证创建表单数据。
     */
    public static class CredentialCreateDTO extends BaseDTO {

        private static final long serialVersionUID = -8206603296599843349L;

        @Getter
        @Setter
        @ApiModelProperty("凭证类型")
        private CredentialType type;

        @Getter
        @Setter
        @ApiModelProperty("凭证")
        private String credential;

        public CredentialCreateDTO() {
        }

        CredentialCreateDTO(CredentialType type, String credential) {
            this.type = type;
            this.credential = credential;
        }
    }
}
