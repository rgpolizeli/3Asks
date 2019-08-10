package com.rgp.asks.persistence.dao;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.rgp.asks.persistence.AppRoomDatabase;
import com.rgp.asks.persistence.entity.Argument;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.persistence.entity.Episode;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class ArgumentDaoTest extends EntitiesBuilder {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();

    private ArgumentDao argumentDao;
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

        this.argumentDao = mDb.argumentDao();
    }

    @Test
    public void createArgument() {
        //given: a argument to insert in db for the belief.
        final Argument argument = buildAnyArgument(this.beliefId);

        //when: user insert the argument for the belief.
        int argumentId = (int) this.argumentDao.insert(argument);
        argument.setId(argumentId);

        //then: argument is in db and is associated with the belief.
        this.argumentDao.getArgumentsForBelief(this.beliefId).observeForever(arguments -> assertEquals(argument, arguments.get(0)));
    }

    @Test
    public void updateArgument() {
        //given: an inserted argument.
        final Argument argument = buildAnyArgument(this.beliefId);
        int argumentId = (int) this.argumentDao.insert(argument);
        argument.setId(argumentId);

        //when: user modifies argument and call argumentDao.update() on it.
        final String argumentUpdated = "Modified Argument";
        argument.setArgument(argumentUpdated);
        int affectedRows = this.argumentDao.update(argument);

        //then: the number of affected rows is equal to 1.
        assertEquals(affectedRows, 1);
    }

    @Test
    public void deleteArgument() {
        //given: an inserted argument.
        final Argument argument = buildAnyArgument(this.beliefId);
        int argumentId = (int) this.argumentDao.insert(argument);
        argument.setId(argumentId);

        //when: user deletes the argument.
        int affectedRows = this.argumentDao.delete(argument);

        //then: the number of affected rows is equal to 1 and the belief hasn't any argument associated with it.
        assertEquals(affectedRows, 1);
        this.argumentDao.getArgumentsForBelief(this.beliefId).observeForever(arguments -> assertEquals(0, arguments.size()));
    }
}