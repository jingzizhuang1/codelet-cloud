package net.codelet.cloud.employee.command.api;

import net.codelet.cloud.employee.command.dto.MembershipApplicationRejectDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@FeignClient(
    name = "${services.organization.command.name:organization-command}",
    contextId = "organization-member-command"
)
public interface EmployeeCommandApi {

    /**
     * 申请加入到公司。
     * @param companyId 公司 ID
     */
    @PostMapping("/companies/{companyId}/join")
    void join(@PathVariable("companyId") String companyId);

    /**
     * 邀请用户加入到公司。
     * @param companyId 公司 ID
     * @param userIDs   用户 ID 集合
     */
    @PutMapping("/companies/{companyId}/employees/{userIDs}")
    void join(
        @PathVariable("companyId") String companyId,
        @PathVariable("userIDs") Set<String> userIDs
    );

    /**
     * 接受邀请。
     * @param companyId 公司 ID
     */
    @PostMapping("/companies/{companyId}/accept")
    void accept(@PathVariable("companyId") String companyId);

    /**
     * 通过申请。
     * @param companyId 公司 ID
     * @param userIDs   用户 ID 集合
     */
    @PostMapping("/companies/{companyId}/employees/{userIDs}/approve")
    void approve(
        @PathVariable("companyId") String companyId,
        @PathVariable("userIDs") Set<String> userIDs
    );

    /**
     * 拒绝邀请。
     * @param companyId 公司 ID
     */
    @PostMapping("/companies/{companyId}/refuse")
    void refuse(@PathVariable("companyId") String companyId);

    /**
     * 驳回申请。
     * @param companyId 公司 ID
     * @param userIDs   用户 ID 集合
     */
    @PostMapping("/companies/{companyId}/employees/{userIDs}/reject")
    void reject(
        @PathVariable("companyId") String companyId,
        @PathVariable("userIDs") Set<String> userIDs,
        @RequestBody MembershipApplicationRejectDTO rejectDTO
    );

    /**
     * 从公司退出。
     * @param companyId 公司 ID
     */
    @PostMapping("/companies/{companyId}/quit")
    void quit(@PathVariable("companyId") String companyId);

    /**
     * 将用户从公司退出。
     * @param companyId 公司 ID
     * @param userIDs   用户 ID 集合
     */
    @DeleteMapping("/companies/{companyId}/employees/{userIDs}")
    void quit(
        @PathVariable("companyId") String companyId,
        @PathVariable("userIDs") Set<String> userIDs
    );
}
