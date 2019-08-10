package com.rgp.asks.persistence.dao;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.rgp.asks.persistence.AppRoomDatabase;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.persistence.entity.Objection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class ObjectionDaoTest extends EntitiesBuilder {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();

    private ObjectionDao objectionDao;
    private int beliefId;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        AppRoomDatabase mDb = Room.inMemoryDatabaseBuilder(context, AppRoomDatabase.class)
                .allowMainThreadQueries()
                .build();

        EpisodeDao episodeDao = mDb.episodeDao();
        Episode episodeToInsert = buildAnyEpisode();
        int episodeId = (int) episodeDao.insert(episodeToInsert);

        BeliefDao beliefDao = mDb.beliefDao();
        Belief beliefToInsert = buildAnyBelief(episodeId);
        this.beliefId = (int) beliefDao.insert(beliefToInsert);

        this.objectionDao = mDb.objectionDao();
    }

    @Test
    public void createObjection() {
        //given: a objection to insert in db for the belief.
        final Objection objection = buildAnyObjection(this.beliefId);

        //when: user insert the objection for the belief.
        int objectionId = (int) this.objectionDao.insert(objection);
        objection.setId(objectionId);

        //then: objection is in db and is associated with the belief.
        this.objectionDao.getObjectionsForBelief(this.beliefId).observeForever(objections -> assertEquals(objection, objections.get(0)));
    }

    @Test
    public void updateObjection() {
        //given: an inserted objection.
        final Objection objection = buildAnyObjection(this.beliefId);
        int objectionId = (int) this.objectionDao.insert(objection);
        objection.setId(objectionId);

        //when: user modifies objection and call objectionDao.update() on it.
        final String objectionUpdated = "Modified Objection";
        objection.setObjection(objectionUpdated);
        int affectedRows = this.objectionDao.update(objection);

        //then: the number of affected rows is equal to 1.
        assertEquals(affectedRows, 1);
    }

    @Test
    public void deleteObjection() {
        //given: an inserted objection.
        final Objection objection = buildAnyObjection(this.beliefId);
        int objectionId = (int) this.objectionDao.insert(objection);
        objection.setId(objectionId);

        //when: user deletes the objection.
        int affectedRows = this.objectionDao.delete(objection);

        //then: the number of affected rows is equal to 1 and the belief hasn't any objection associated with it.
        assertEquals(affectedRows, 1);
        this.objectionDao.getObjectionsForBelief(this.beliefId).observeForever(objections -> assertEquals(0, objections.size()));
    }
}