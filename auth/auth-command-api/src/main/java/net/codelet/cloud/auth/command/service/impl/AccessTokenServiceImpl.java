package net.codelet.cloud.auth.command.service.impl;

import net.codelet.cloud.auth.command.api.AuthenticationApi;
import net.codelet.cloud.auth.command.service.AccessTokenService;
import net.codelet.cloud.config.SecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties({SecurityConfiguration.class})
public class AccessTokenServiceImpl extends AccessTokenClaimServiceImpl implements AccessTokenService {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public AccessTokenServiceImpl(
        StringRedisTemplate stringRedisTemplate,
        SecurityConfiguration securityConfiguration,
        AuthenticationApi authenticationApi
    ) {
        super(stringRedisTemplate, securityConfiguration, authenticationApi);
    }
}
