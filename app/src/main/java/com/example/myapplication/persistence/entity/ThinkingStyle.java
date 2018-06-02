package com.example.myapplication.persistence.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

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
}
