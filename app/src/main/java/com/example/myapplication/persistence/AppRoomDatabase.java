package com.example.myapplication.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.myapplication.persistence.converter.DateConverter;
import com.example.myapplication.persistence.dao.ArgumentDao;
import com.example.myapplication.persistence.dao.BeliefDao;
import com.example.myapplication.persistence.dao.BeliefThinkingStyleDao;
import com.example.myapplication.persistence.dao.EpisodeDao;
import com.example.myapplication.persistence.dao.ObjectionDao;
import com.example.myapplication.persistence.dao.ReactionDao;
import com.example.myapplication.persistence.dao.ThinkingStyleDao;
import com.example.myapplication.persistence.entity.Argument;
import com.example.myapplication.persistence.entity.Belief;
import com.example.myapplication.persistence.entity.BeliefThinkingStyle;
import com.example.myapplication.persistence.entity.Episode;
import com.example.myapplication.persistence.entity.Objection;
import com.example.myapplication.persistence.entity.Reaction;
import com.example.myapplication.persistence.entity.ThinkingStyle;

@Database(entities = {Argument.class, Belief.class, BeliefThinkingStyle.class, Episode.class, Objection.class, Reaction.class, ThinkingStyle.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppRoomDatabase extends RoomDatabase {

    public abstract ReactionDao reactionDao();
    public abstract ArgumentDao argumentDao();
    public abstract BeliefDao beliefDao();
    public abstract BeliefThinkingStyleDao beliefThinkingStyleDao();
    public abstract EpisodeDao episodeDao();
    public abstract ObjectionDao objectionDao();
    public abstract ThinkingStyleDao thinkingStyleDao();

    private static AppRoomDatabase INSTANCE;

    static AppRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppRoomDatabase.class, "3ask_database")
                            .build();

                }
            }
        }
        return INSTANCE;
    }

}
