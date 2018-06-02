package com.example.myapplication.persistence.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.myapplication.persistence.entity.BeliefThinkingStyle;
import com.example.myapplication.persistence.entity.ThinkingStyle;

import java.util.List;

@Dao
public interface BeliefThinkingStyleDao {

    @Insert
    void insert(BeliefThinkingStyle beliefThinkingStyle);

    @Delete
    void delete(BeliefThinkingStyle... beliefThinkingStyles);

    @Query("SELECT ThinkingStyle.thinkingStyle FROM BeliefThinkingStyle INNER JOIN ThinkingStyle ON ThinkingStyle.thinkingStyle = BeliefThinkingStyle.thinkingStyleId WHERE BeliefThinkingStyle.beliefId=:beliefId")
    LiveData<List<ThinkingStyle>> getThinkingStylesForBelief(final int beliefId);

}
