package net.codelet.cloud.auth.query.service.impl;

import net.codelet.cloud.auth.query.repository.AccessTokenQueryRepository;
import net.codelet.cloud.auth.query.service.AccessTokenQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenQueryServiceImpl implements AccessTokenQueryService {

    private final AccessTokenQueryRepository accessTokenQueryRepository;

    @Autowired
    public AccessTokenQueryServiceImpl(AccessTokenQueryRepository accessTokenQueryRepository) {
        this.accessTokenQueryRepository = accessTokenQueryRepository;
    }

}
