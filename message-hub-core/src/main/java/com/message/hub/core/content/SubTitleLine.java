package com.message.hub.core.content;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 标题行
 *
 * @author admin
 * @date 2025/04/22
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubTitleLine extends SubLine {

    @Getter
    private Integer level;

    protected SubTitleLine(String content, Integer level) {
        this.lineType = SubLineEnum.TITLE;
        this.content = content;
        this.level = level;
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
        SubTitleLine that = (SubTitleLine) obj;
        return Objects.equals(level, that.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), level);
    }

}
