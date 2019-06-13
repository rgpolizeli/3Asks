package com.rgp.asks.persistence;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.rgp.asks.auxiliaries.Constants;
import com.rgp.asks.persistence.converter.DateConverter;
import com.rgp.asks.persistence.dao.ArgumentDao;
import com.rgp.asks.persistence.dao.BeliefDao;
import com.rgp.asks.persistence.dao.BeliefThinkingStyleDao;
import com.rgp.asks.persistence.dao.EpisodeDao;
import com.rgp.asks.persistence.dao.ObjectionDao;
import com.rgp.asks.persistence.dao.ReactionDao;
import com.rgp.asks.persistence.dao.ThinkingStyleDao;
import com.rgp.asks.persistence.entity.Argument;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.persistence.entity.BeliefThinkingStyle;
import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.persistence.entity.Objection;
import com.rgp.asks.persistence.entity.Reaction;
import com.rgp.asks.persistence.entity.ThinkingStyle;

import java.util.concurrent.Executors;

@Database(entities = {Argument.class, Belief.class, BeliefThinkingStyle.class, Episode.class, Objection.class, Reaction.class, ThinkingStyle.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppRoomDatabase extends RoomDatabase {

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

    private static AppRoomDatabase buildDatabase(final Context context) {
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

        return new ThinkingStyle[]{

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

    public abstract ReactionDao reactionDao();

    public abstract ArgumentDao argumentDao();

    public abstract BeliefDao beliefDao();

    public abstract BeliefThinkingStyleDao beliefThinkingStyleDao();

    public abstract EpisodeDao episodeDao();

    public abstract ObjectionDao objectionDao();

    public abstract ThinkingStyleDao thinkingStyleDao();

}
