package com.rpolizeli.asks.persistence.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

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

    public void setThinkingStyle(@NonNull String thinkingStyle) {
        this.thinkingStyle = thinkingStyle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThinkingStyle that = (ThinkingStyle) o;

        return getThinkingStyle().equals(that.getThinkingStyle());
    }

    @Override
    public int hashCode() {
        return getThinkingStyle().hashCode();
    }
}
