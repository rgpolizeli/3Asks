package com.rgp.asks.persistence.dao;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.rgp.asks.persistence.AppRoomDatabase;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.persistence.entity.BeliefThinkingStyle;
import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.persistence.entity.ThinkingStyle;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class BeliefThinkingStyleDaoTest extends EntitiesBuilder {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();
    private BeliefDao beliefDao;
    private BeliefThinkingStyleDao beliefThinkingStyleDao;
    private int beliefId;
    private Belief belief;
    private String thinkingStyleId; //the primary key of thinkingStyle Table is a String.

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        AppRoomDatabase mDb = Room.inMemoryDatabaseBuilder(context, AppRoomDatabase.class)
                .allowMainThreadQueries()
                .build();

        EpisodeDao episodeDao = mDb.episodeDao();
        Episode episodeToInsert = buildAnyEpisode();
        int episodeId = (int) episodeDao.insert(episodeToInsert);

        this.beliefDao = mDb.beliefDao();
        this.belief = buildAnyBelief(episodeId);
        this.beliefId = (int) beliefDao.insert(belief);
        this.belief.setId(beliefId);

        ThinkingStyleDao thinkingStyleDao = mDb.thinkingStyleDao();
        ThinkingStyle thinkingStyleToInsert = buildAnyThinkingStyle();
        thinkingStyleDao.insert(thinkingStyleToInsert);
        this.thinkingStyleId = thinkingStyleToInsert.getThinkingStyle();

        this.beliefThinkingStyleDao = mDb.beliefThinkingStyleDao();
    }

    @Test
    public void associateThinkingStyleToBelief() {

        //given: one episode, one belief for this episode and one thinking style

        //when: user associates this belief with this thinking style
        BeliefThinkingStyle beliefThinkingStyle = buildBeliefThinkingStyle(beliefId, thinkingStyleId);
        beliefThinkingStyleDao.insert(beliefThinkingStyle);

        //then: a entry in BeliefThinkingStyle Table is created.
        beliefThinkingStyleDao.getThinkingStylesForBelief(this.beliefId).observeForever(thinkingStyles -> assertEquals(1, thinkingStyles.size()));
    }

    @Test
    public void desassociateThinkingStyleToBelief() {

        //given: one episode, one belief for this episode and one thinking style associated with this belief
        BeliefThinkingStyle beliefThinkingStyle = buildBeliefThinkingStyle(beliefId, thinkingStyleId);
        beliefThinkingStyleDao.insert(beliefThinkingStyle);

        //when: user desassociates this belief with this thinking style
        beliefThinkingStyleDao.delete(beliefThinkingStyle);

        //then: a entry in BeliefThinkingStyle Table is created.
        beliefThinkingStyleDao.getThinkingStylesForBelief(this.beliefId).observeForever(thinkingStyles -> assertEquals(0, thinkingStyles.size()));
    }
}
