package com.message.hub.core.content;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 链接行
 *
 * @author jint233
 * @date 2025/04/22
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubLinkLine extends SubLine {

    @Getter
    private String link;

    protected SubLinkLine(String content, String link) {
        this.content = content;
        this.link = link;
        this.lineType = SubLineEnum.LINK;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        SubLinkLine that = (SubLinkLine) obj;
        return Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), link);
    }

}
