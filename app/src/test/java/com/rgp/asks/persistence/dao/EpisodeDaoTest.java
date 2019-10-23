package com.rgp.asks.persistence.dao;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.rgp.asks.persistence.AppRoomDatabase;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.persistence.entity.Reaction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class EpisodeDaoTest extends EntitiesBuilder {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();

    private EpisodeDao episodeDao;
    private ReactionDao reactionDao;
    private BeliefDao beliefDao;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        AppRoomDatabase mDb = Room.inMemoryDatabaseBuilder(context, AppRoomDatabase.class)
                .allowMainThreadQueries()
                .build();
        this.episodeDao = mDb.episodeDao();
        this.reactionDao = mDb.reactionDao();
        this.beliefDao = mDb.beliefDao();
    }

    @Test
    public void createEpisode_WithoutId() {
        //given: an episode created without id to be inserted.
        Episode episodeToInsert = buildAnyEpisode();

        //when: call episodeDao.insert() on it.
        int episodeId = (int) this.episodeDao.insert(episodeToInsert);
        episodeToInsert.setId(episodeId);

        //then: episode in db is equal to the created episode.
        getEpisodeById(episodeId).observeForever(episode -> assertEquals(episode, episodeToInsert));
    }

    // this is a necessary test? This never occurs in application execution //
    @Test(expected = SQLiteConstraintException.class)
    public void createEpisode_WithRepeatedId() {
        //given: an inserted episode.

        Episode episodeToInsert = buildAnyEpisode();

        int episodeId = (int) this.episodeDao.insert(episodeToInsert);

        //when: call episodeDao.insert() on another episode but with the same id.
        Episode anotherEpisodeToInsert = buildAnyEpisode();
        anotherEpisodeToInsert.setId(episodeId);
        this.episodeDao.insert(anotherEpisodeToInsert);

        //then: an exception is throwed.
    }

    @Test
    public void updateEpisode() {
        //given: an inserted episode.
        Episode episodeToInsert = buildAnyEpisode();
        int episodeId = (int) this.episodeDao.insert(episodeToInsert);
        episodeToInsert.setId(episodeId);

        //when: user modifies some episode's properties and call episodeDao.update() on it.
        final String episodeNameUpdated = "Updated episode";
        final String episodePeriodUpdated = "Evening";
        episodeToInsert.setEpisode(episodeNameUpdated);
        episodeToInsert.setPeriod(episodePeriodUpdated);
        this.episodeDao.update(episodeToInsert);

        //then: updated properties of the episode in db is equal to modified properties.
        getEpisodeById(episodeId).observeForever(episode -> {
            assertEquals(episode.getEpisode(), episodeNameUpdated);
            assertEquals(episode.getPeriod(), episodePeriodUpdated);
        });
    }

    @Test
    public void deleteEpisodeWithoutReactionsAndBeliefs() {
        //given: an inserted episode.
        Episode episodeToInsert = buildAnyEpisode();
        int episodeId = (int) this.episodeDao.insert(episodeToInsert);
        episodeToInsert.setId(episodeId);

        //when: user deletes the episode.
        int affectedRows = this.episodeDao.delete(episodeToInsert);

        //then: the number of affected rows is equal to 1.
        assertEquals(affectedRows, 1);
    }

    @Test
    public void deleteEpisode_DeleteReactionsAndBeliefsInCascade() throws InterruptedException {
        //given a episode with reactions and beliefs.
        //when user deletes episode.
        //then reactions and beliefs are deleted in cascade.

        Episode episode = buildAnyEpisode();
        int episodeId = (int) this.episodeDao.insert(episode);
        episode.setId(episodeId);

        Reaction reaction = buildAnyReaction(episodeId);
        int reactionId = (int) this.reactionDao.insert(reaction);
        reaction.setId(reactionId);

        Belief belief = buildAnyBelief(episodeId);
        int beliefId = (int) this.beliefDao.insert(belief);
        belief.setId(beliefId);

        this.episodeDao.delete(episode);

        this.reactionDao.getReactionsForEpisode(episodeId).observeForever(reactions -> {
            assertEquals(0, reactions.size());
        });

        this.beliefDao.getBeliefsForEpisode(episodeId).observeForever(beliefs -> {
            assertEquals(0, beliefs.size());
        });
    }

    @Test
    public void getAllEpisodesTest() {
        //given: n inserted episodes.
        int n = 3;
        for (int i = 0; i < n; i++) {
            Episode episodeToInsert = buildAnyEpisode();
            this.episodeDao.insert(episodeToInsert);
        }
        //when: user get all episodes from db
        this.episodeDao.getAllEpisodes().observeForever(gotEpisodes -> {
            //then: the number of episodes in db is equal to n.
            assertEquals(n, gotEpisodes.size());
        });
    }

    private LiveData<Episode> getEpisodeById(int episodeId) {
        return this.episodeDao.getEntityById(episodeId);
    }
}