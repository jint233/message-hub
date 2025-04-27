package com.message.hub.core.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 平台响应
 *
 * @author admin
 * @date 2025/04/27
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SendResult {

    private Boolean success;

    private List<PlatformSendResult> results;

    private SendResult(List<PlatformSendResult> platformResults) {
        this.results = platformResults;
    }

    public static SendResult warp(List<PlatformSendResult> platformResults) {
        return new SendResult(platformResults);
    }

    public Boolean getSuccess() {
        return results.stream().allMatch(PlatformSendResult::getSuccess);
    }

    public List<PlatformSendResult> errorResults() {
        return results.stream().filter(r -> !r.getSuccess()).toList();
    }

}
