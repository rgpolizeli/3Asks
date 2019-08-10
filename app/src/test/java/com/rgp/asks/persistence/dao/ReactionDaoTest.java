package com.rgp.asks.persistence.dao;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.rgp.asks.persistence.AppRoomDatabase;
import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.persistence.entity.Reaction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class ReactionDaoTest extends EntitiesBuilder {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();

    private ReactionDao reactionDao;
    private int episodeId;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        AppRoomDatabase mDb = Room.inMemoryDatabaseBuilder(context, AppRoomDatabase.class)
                .allowMainThreadQueries()
                .build();
        EpisodeDao episodeDao = mDb.episodeDao();
        reactionDao = mDb.reactionDao();

        Episode episodeToInsert = buildAnyEpisode();
        this.episodeId = (int) episodeDao.insert(episodeToInsert);
    }

    @Test
    public void createReaction() {
        //given: a reaction to insert in db for the episode.
        final Reaction reaction = buildAnyReaction(this.episodeId);

        //when: user insert the reaction for the episode.
        int reactionId = (int) this.reactionDao.insert(reaction);
        reaction.setId(reactionId);

        //then: reaction is in db and is associated with episode.
        this.reactionDao.getReactionsForEpisode(episodeId).observeForever(reactions -> assertEquals(reaction, reactions.get(0)));
    }

    @Test
    public void updateReaction() {
        //given: an inserted reaction.
        final Reaction reaction = buildAnyReaction(this.episodeId);
        int reactionId = (int) this.reactionDao.insert(reaction);
        reaction.setId(reactionId);

        //when: user modifies some reaction's properties and call reactionDao.update() on it.
        final String reactionClassUpdated = "Evening";
        reaction.setReactionCategory(reactionClassUpdated);
        int affectedRows = this.reactionDao.update(reaction);

        //then: the number of affected rows is equal to 1.
        assertEquals(affectedRows, 1);
    }

    @Test
    public void deleteReaction() {
        //given: an inserted reaction.
        final Reaction reaction = buildAnyReaction(this.episodeId);
        int reactionId = (int) this.reactionDao.insert(reaction);
        reaction.setId(reactionId);

        //when: user deletes the reaction.
        int affectedRows = this.reactionDao.delete(reaction);

        //then: the number of affected rows is equal to 1 and the episode hasn't any reaction associated with it.
        assertEquals(affectedRows, 1);
        this.reactionDao.getReactionsForEpisode(episodeId).observeForever(reactions -> assertEquals(0, reactions.size()));
    }
}