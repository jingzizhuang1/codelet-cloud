package net.codelet.cloud.auth.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.auth.vo.CredentialType;
import net.codelet.cloud.dto.BaseDTO;
import net.codelet.cloud.error.ValidationError;
import net.codelet.cloud.util.RegExpUtils;
import net.codelet.cloud.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 认证凭证数据传输对象。
 */
public class CredentialDTO extends BaseDTO {

    private static final long serialVersionUID = 2337500795827151406L;

    private static final Pattern CREDENTIAL = Pattern.compile("^(([_0-9A-Z]+):)?([^:]+?)$", Pattern.CASE_INSENSITIVE);

    @Getter
    @Setter
    @ApiModelProperty("凭证类型")
    private CredentialType type;

    @Getter
    @Setter
    @ApiModelProperty("认证凭证")
    private String credential;

    @Getter
    @Setter
    @ApiModelProperty("电子邮箱地址")
    private String email;

    @Getter
    @Setter
    @ApiModelProperty("手机号码")
    private String mobile;

    @Getter
    @Setter
    @ApiModelProperty("登录用户名")
    private String username;

    public CredentialDTO() {
    }

    public CredentialDTO(String credentialPattern) {
        Matcher matcher = CREDENTIAL.matcher(credentialPattern);
        if (!matcher.matches()) {
            throw new ValidationError("error.credential.invalid-credential-pattern"); // TODO: set message
        }

        String typeName = matcher.group(2);
        credential = matcher.group(3);

        if (!StringUtils.isEmpty(typeName)) {
            type = CredentialType.valueOf(typeName);
        } else {
            if (RegExpUtils.isEmailAddress(credential)) {
                type = CredentialType.EMAIL;
            } else if (RegExpUtils.isMobileNo(credential)) {
                type = CredentialType.MOBILE;
            } else if (RegExpUtils.isUsername(credential)) {
                type = CredentialType.USERNAME;
            } else {
                throw new ValidationError("error.credential.invalid-credential-type"); // TODO: set message
            }
        }

        switch (type) {
            case EMAIL:
                credential = credential.toLowerCase();
                email = credential;
                break;
            case MOBILE:
                mobile = credential;
                break;
            case USERNAME:
                username = credential;
                break;
        }
    }
}
