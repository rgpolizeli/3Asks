package com.example.myapplication.persistence;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.myapplication.auxiliaries.Constants;
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

import java.util.concurrent.Executors;

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

    public static AppRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = buildDatabase(context);
                }
            }
        }
        return INSTANCE;
    }

    private static AppRoomDatabase buildDatabase(final Context context){
        return Room.databaseBuilder(context.getApplicationContext(),
                AppRoomDatabase.class, "3ask_database")
                .addCallback(new Callback() {

                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {

                        super.onCreate(db);

                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {

                            @Override

                            public void run() {
                                getDatabase(context).thinkingStyleDao().insert(createUnhelpfulThinkingStyles());
                            }

                        });

                    }

                })
                .build();
    }

    private static ThinkingStyle[] createUnhelpfulThinkingStyles() {

        return new ThinkingStyle[] {

                new ThinkingStyle(Constants.unhelpful_thinking_style_mental_filter_label),

                new ThinkingStyle(Constants.unhelpful_thinking_style_mind_reading_label),

                new ThinkingStyle(Constants.unhelpful_thinking_style_predictive_thinking_label),

                new ThinkingStyle(Constants.unhelpful_thinking_style_personalisation_label),

                new ThinkingStyle(Constants.unhelpful_thinking_style_catastrophising_label),

                new ThinkingStyle(Constants.unhelpful_thinking_style_black_white_label),

                new ThinkingStyle(Constants.unhelpful_thinking_style_overgeneralisation_label),

                new ThinkingStyle(Constants.unhelpful_thinking_style_shoulding_musting_label),

                new ThinkingStyle(Constants.unhelpful_thinking_style_magnification_minimisation_label),

                new ThinkingStyle(Constants.unhelpful_thinking_style_others_empowerment_label),

                new ThinkingStyle(Constants.unhelpful_thinking_style_labeling_label)
        };

    }

}
