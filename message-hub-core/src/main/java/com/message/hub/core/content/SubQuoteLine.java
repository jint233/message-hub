package com.message.hub.core.content;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 引用行
 *
 * @author jint233
 * @date 2025/04/22
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubQuoteLine extends SubLine {

    protected SubQuoteLine(String content) {
        this.content = content;
        this.lineType = SubLineEnum.QUOTE;
    }

}
