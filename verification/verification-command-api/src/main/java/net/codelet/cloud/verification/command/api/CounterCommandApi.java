package net.codelet.cloud.verification.command.api;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
    name = "${services.verification.command.name:verification-command}",
    contextId = "counter-command"
)
public interface CounterCommandApi {
}
