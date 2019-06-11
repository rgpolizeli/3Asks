package com.rgp.asks;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.rgp.asks.persistence.AppRoomDatabase;
import com.rgp.asks.persistence.dao.BeliefDao;
import com.rgp.asks.persistence.dao.BeliefThinkingStyleDao;
import com.rgp.asks.persistence.dao.EpisodeDao;
import com.rgp.asks.persistence.dao.ThinkingStyleDao;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.persistence.entity.BeliefThinkingStyle;
import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.persistence.entity.ThinkingStyle;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class GetThinkingStylesForBeliefTest {
    private BeliefThinkingStyleDao beliefThinkingStyleDao;
    private EpisodeDao episodeDao;
    private BeliefDao beliefDao;
    private ThinkingStyleDao thinkingStyleDao;
    private AppRoomDatabase mDb;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppRoomDatabase.class)
                .allowMainThreadQueries()
                .build();
        episodeDao = mDb.episodeDao();
        beliefDao = mDb.beliefDao();
        thinkingStyleDao = mDb.thinkingStyleDao();
        beliefThinkingStyleDao = mDb.beliefThinkingStyleDao();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void writeUserAndReadInList() {
        Episode e = new Episode("episode", "", new Date(), "");
        Long eId = episodeDao.insert(e);

        Belief b = new Belief("belief", eId.intValue());
        Long bId = beliefDao.insert(b);

        String thId1 = "ThinkingStyle1";
        String thId2 = "ThinkingStyle2";
        ThinkingStyle th1 = new ThinkingStyle(thId1);
        ThinkingStyle th2 = new ThinkingStyle(thId2);
        thinkingStyleDao.insert(th1);
        thinkingStyleDao.insert(th2);

        BeliefThinkingStyle bTh1 = new BeliefThinkingStyle(bId.intValue(), thId1);
        BeliefThinkingStyle bTh2 = new BeliefThinkingStyle(bId.intValue(), thId2);
        beliefThinkingStyleDao.insert(bTh1);
        beliefThinkingStyleDao.insert(bTh2);

        LiveData<List<ThinkingStyle>> thinkingStylesForBelief = beliefThinkingStyleDao.getThinkingStylesForBelief(bId.intValue());
        thinkingStylesForBelief.observeForever(thinkingStyles -> {
                assert(thinkingStyles != null);
                assert(thinkingStyles.size() == 2);
            }
        );
    }
}
