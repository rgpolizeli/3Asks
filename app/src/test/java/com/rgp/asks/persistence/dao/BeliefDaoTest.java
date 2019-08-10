package com.rgp.asks.persistence.dao;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.rgp.asks.persistence.AppRoomDatabase;
import com.rgp.asks.persistence.entity.Argument;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.persistence.entity.BeliefThinkingStyle;
import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.persistence.entity.Objection;
import com.rgp.asks.persistence.entity.ThinkingStyle;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class BeliefDaoTest extends EntitiesBuilder {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();

    private BeliefDao beliefDao;
    private ArgumentDao argumentDao;
    private ObjectionDao objectionDao;
    private ThinkingStyleDao thinkingStyleDao;
    private BeliefThinkingStyleDao beliefThinkingStyleDao;
    private int episodeId;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        AppRoomDatabase mDb = Room.inMemoryDatabaseBuilder(context, AppRoomDatabase.class)
                .allowMainThreadQueries()
                .build();
        EpisodeDao episodeDao = mDb.episodeDao();
        this.beliefDao = mDb.beliefDao();
        this.argumentDao = mDb.argumentDao();
        this.objectionDao = mDb.objectionDao();
        this.thinkingStyleDao = mDb.thinkingStyleDao();
        this.beliefThinkingStyleDao = mDb.beliefThinkingStyleDao();

        Episode episodeToInsert = buildAnyEpisode();
        this.episodeId = (int) episodeDao.insert(episodeToInsert);

    }

    @Test
    public void createBelief() {
        //given: a belief to insert in db for the episode.
        final Belief belief = buildAnyBelief(this.episodeId);

        //when: user insert the belief for the episode.
        int beliefId = (int) this.beliefDao.insert(belief);
        belief.setId(beliefId);

        //then: belief is in db and is associated with the episode.
        this.beliefDao.getBeliefsForEpisode(this.episodeId).observeForever(beliefs -> assertEquals(belief, beliefs.get(0)));
    }

    @Test
    public void updateBelief() {
        //given: an inserted belief.
        final Belief belief = buildAnyBelief(this.episodeId);
        int beliefId = (int) this.beliefDao.insert(belief);
        belief.setId(beliefId);

        //when: user modifies belief name and call beliefDao.update() on it.
        final String beliefNameUpdated = "Modified Belief";
        belief.setBelief(beliefNameUpdated);
        int affectedRows = this.beliefDao.update(belief);

        //then: the number of affected rows is equal to 1.
        assertEquals(affectedRows, 1);
    }

    @Test
    public void deleteBelief() {
        //given: an inserted belief.
        final Belief belief = buildAnyBelief(this.episodeId);
        int beliefId = (int) this.beliefDao.insert(belief);
        belief.setId(beliefId);

        //when: user deletes the belief.
        int affectedRows = this.beliefDao.delete(belief);

        //then: the number of affected rows is equal to 1 and the episode hasn't any belief associated with it.
        assertEquals(affectedRows, 1);
        this.beliefDao.getBeliefsForEpisode(this.episodeId).observeForever(beliefs -> assertEquals(0, beliefs.size()));
    }


    @Test
    public void deleteBelief_deleteArgumentsAndObjectionsAndBeliefThinkingStyles() {
        //given belief with arguments and objections and thinking styles.
        //when this belief is deleted.
        //then arguments and objections is deleted in cascade.

        Belief belief = buildAnyBelief(this.episodeId);
        int beliefId = (int) this.beliefDao.insert(belief);
        belief.setId(beliefId);

        Argument argument = buildAnyArgument(beliefId);
        int argumentId = (int) this.argumentDao.insert(argument);
        argument.setId(argumentId);

        Objection objection = buildAnyObjection(beliefId);
        int objectionId = (int) this.objectionDao.insert(objection);
        objection.setId(objectionId);

        ThinkingStyle thinkingStyleToInsert = buildAnyThinkingStyle();
        this.thinkingStyleDao.insert(thinkingStyleToInsert);
        String thinkingStyleId = thinkingStyleToInsert.getThinkingStyle();

        BeliefThinkingStyle beliefThinkingStyle = buildBeliefThinkingStyle(beliefId, thinkingStyleId);
        this.beliefThinkingStyleDao.insert(beliefThinkingStyle);

        this.beliefDao.delete(belief);

        beliefThinkingStyleDao.getThinkingStylesForBelief(beliefId).observeForever(thinkingStyles -> {
            assertEquals(0, thinkingStyles.size());
        });

        this.objectionDao.getObjectionsForBelief(beliefId).observeForever(objections -> {
            assertEquals(0, objections.size());
        });

        this.argumentDao.getArgumentsForBelief(beliefId).observeForever(arguments -> {
            assertEquals(0, arguments.size());
        });
    }
}