package net.codelet.cloud.user.command.api;

import net.codelet.cloud.user.command.dto.UserSignUpDTO;
import net.codelet.cloud.user.command.entity.UserCommandEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "${services.user.command.name:user-command}",
    contextId = "user-command"
)
public interface UserCommandApi {

    /**
     * 用户注册。
     * @param signUpDTO 用户信息
     */
    @PostMapping("/users")
    void create(@RequestBody UserSignUpDTO signUpDTO);

    /**
     * 创建用户。
     * @param userId 用户标识
     */
    @PutMapping("/users/{userId}")
    UserCommandEntity create(@PathVariable("userId") String userId);
}
