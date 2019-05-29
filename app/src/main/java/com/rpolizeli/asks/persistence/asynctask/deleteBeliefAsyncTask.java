package com.rpolizeli.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rpolizeli.asks.messages.DeletedBeliefEvent;
import com.rpolizeli.asks.persistence.dao.BeliefDao;
import com.rpolizeli.asks.persistence.entity.Belief;

import org.greenrobot.eventbus.EventBus;

public class deleteBeliefAsyncTask extends AsyncTask<Belief, Void, Integer> {
    private BeliefDao mAsyncTaskDao;
    private int deletedBeliefId;

    public deleteBeliefAsyncTask(BeliefDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(final Belief... params) {
        deletedBeliefId = params[0].getId();
        return mAsyncTaskDao.delete(params[0]);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        EventBus.getDefault().post(new DeletedBeliefEvent(result==1, deletedBeliefId));
    }
}

