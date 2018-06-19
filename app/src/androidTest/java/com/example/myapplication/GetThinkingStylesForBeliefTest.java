package com.example.myapplication;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.myapplication.persistence.AppRoomDatabase;
import com.example.myapplication.persistence.dao.BeliefDao;
import com.example.myapplication.persistence.dao.BeliefThinkingStyleDao;
import com.example.myapplication.persistence.dao.EpisodeDao;
import com.example.myapplication.persistence.dao.ThinkingStyleDao;
import com.example.myapplication.persistence.entity.Belief;
import com.example.myapplication.persistence.entity.BeliefThinkingStyle;
import com.example.myapplication.persistence.entity.Episode;
import com.example.myapplication.persistence.entity.ThinkingStyle;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class GetThinkingStylesForBeliefTest {
    private BeliefThinkingStyleDao beliefThinkingStyleDao;
    private EpisodeDao episodeDao;
    private BeliefDao beliefDao;
    private ThinkingStyleDao thinkingStyleDao;
    private AppRoomDatabase mDb;
    private Context context;

    @Rule
    public InstantTaskExecutorRule instant = new InstantTaskExecutorRule();

    @Before
    public void createDb() {
        context = InstrumentationRegistry.getTargetContext();
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

    @Mock
    Observer<List<ThinkingStyle>> observer;

    @Test
    public void writeUserAndReadInList() throws Exception {
        Episode e = new Episode("episode","",new Date(),"");
        Long eId = episodeDao.insert(e);

        Belief b = new Belief("belief",eId.intValue());
        Long bId = beliefDao.insert(b);

        String thId1 = "ThinkingStyle1";
        String thId2 = "ThinkingStyle2";
        ThinkingStyle th1 = new ThinkingStyle(thId1);
        ThinkingStyle th2 = new ThinkingStyle(thId2);
        thinkingStyleDao.insert(th1);
        thinkingStyleDao.insert(th2);

         BeliefThinkingStyle bTh1 = new BeliefThinkingStyle(bId.intValue(),thId1);
        BeliefThinkingStyle bTh2 = new BeliefThinkingStyle(bId.intValue(),thId2);
         beliefThinkingStyleDao.insert(bTh1);
        beliefThinkingStyleDao.insert(bTh2);

         LiveData<List<ThinkingStyle>> thinkingStylesForBelief = beliefThinkingStyleDao.getThinkingStylesForBelief(bId.intValue());
         thinkingStylesForBelief.observeForever(new Observer<List<ThinkingStyle>>() {
             @Override
             public void onChanged(@Nullable List<ThinkingStyle> thinkingStyles) {
                 assert(thinkingStyles.size()==1);
             }
         });



    }
}
