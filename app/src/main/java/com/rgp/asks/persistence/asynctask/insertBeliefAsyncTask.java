package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.interfaces.OnInsertedEntityListener;
import com.rgp.asks.persistence.dao.BeliefDao;
import com.rgp.asks.persistence.entity.Belief;

public class insertBeliefAsyncTask extends AsyncTask<Belief, Void, Long> {
    private BeliefDao dao;
    private OnInsertedEntityListener onInsertedEntityListener;

    public insertBeliefAsyncTask(BeliefDao dao, OnInsertedEntityListener onInsertedEntityListener) {
        this.dao = dao;
        this.onInsertedEntityListener = onInsertedEntityListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Long doInBackground(final Belief... params) {
        return dao.insert(params[0]);
    }

    @Override
    protected void onPostExecute(Long id) {
        super.onPostExecute(id);
        if (this.onInsertedEntityListener != null) {
            onInsertedEntityListener.onInsertedEntity(id.intValue());
        }
    }
}
