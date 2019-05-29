package com.rpolizeli.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rpolizeli.asks.auxiliaries.Constants;
import com.rpolizeli.asks.messages.CreatedBeliefEvent;
import com.rpolizeli.asks.messages.CreatingBeliefEvent;
import com.rpolizeli.asks.persistence.dao.BeliefDao;
import com.rpolizeli.asks.persistence.entity.Belief;

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
