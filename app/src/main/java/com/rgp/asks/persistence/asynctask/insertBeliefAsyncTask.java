package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.messages.CreatedBeliefEvent;
import com.rgp.asks.persistence.dao.BeliefDao;
import com.rgp.asks.persistence.entity.Belief;

import org.greenrobot.eventbus.EventBus;

public class insertBeliefAsyncTask extends AsyncTask<Belief, Void, Long> {
    private BeliefDao mAsyncTaskDao;
    private Belief belief;

    public insertBeliefAsyncTask(BeliefDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Long doInBackground(final Belief... params) {
        this.belief = params[0];
        return mAsyncTaskDao.insert(params[0]);
    }

    @Override
    protected void onPostExecute(Long beliefId) {
        super.onPostExecute(beliefId);
        EventBus.getDefault().post(new CreatedBeliefEvent(beliefId.intValue(), this.belief.getBelief()));
    }
}
