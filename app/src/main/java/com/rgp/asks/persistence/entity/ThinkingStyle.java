package com.rgp.asks.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ThinkingStyle {
    @NonNull
    @PrimaryKey
    private String thinkingStyle;

    public ThinkingStyle(@NonNull String thinkingStyle) {
        this.thinkingStyle = thinkingStyle;
    }

    @NonNull
    public String getThinkingStyle() {
        return thinkingStyle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThinkingStyle that = (ThinkingStyle) o;

        return getThinkingStyle().equals(that.getThinkingStyle());
    }
}
