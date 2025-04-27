package com.message.hub.core.content;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 文本行
 *
 * @author jint233
 * @date 2025/04/22
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubTextLine extends SubLine {

    protected SubTextLine(String content) {
        this.content = content;
        this.lineType = SubLineEnum.TEXT;
    }

}
