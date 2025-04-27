package com.message.hub.core.content;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 重点行
 *
 * @author jint233
 * @date 2025/04/22
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubBoldLine extends SubLine {

    protected SubBoldLine(String content) {
        this.content = content;
        this.lineType = SubLineEnum.BOLD;
    }

}
