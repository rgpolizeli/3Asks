package com.example.myapplication.persistence.asynctask;

import android.os.AsyncTask;

import com.example.myapplication.auxiliaries.Constants;
import com.example.myapplication.messages.CreatedBeliefEvent;
import com.example.myapplication.messages.CreatingBeliefEvent;
import com.example.myapplication.messages.CreatingEpisodeEvent;
import com.example.myapplication.persistence.dao.BeliefDao;
import com.example.myapplication.persistence.entity.Belief;

import org.greenrobot.eventbus.EventBus;

public class insertBeliefAsyncTask extends AsyncTask<Belief, Void, Long> {
    private BeliefDao mAsyncTaskDao;

    public insertBeliefAsyncTask(BeliefDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        EventBus.getDefault().post(new CreatingBeliefEvent(Constants.START_CREATE_BELIEF_MESSAGE));
    }

    @Override
    protected Long doInBackground(final Belief... params) {
        long id = mAsyncTaskDao.insert(params[0]);
        return id;
    }

    @Override
    protected void onPostExecute(Long beliefId) {
        super.onPostExecute(beliefId);
        EventBus.getDefault().post(new CreatedBeliefEvent(beliefId.intValue()));
    }
}
