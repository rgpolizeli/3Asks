package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.interfaces.OnDeletedEntityListener;
import com.rgp.asks.persistence.dao.BeliefDao;
import com.rgp.asks.persistence.entity.Belief;

public class deleteBeliefAsyncTask extends AsyncTask<Belief, Void, Integer> {
    private BeliefDao dao;
    private OnDeletedEntityListener onDeletedEntityListener;

    public deleteBeliefAsyncTask(BeliefDao dao, OnDeletedEntityListener onDeletedEntityListener) {
        this.dao = dao;
        this.onDeletedEntityListener = onDeletedEntityListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(final Belief... params) {
        return dao.delete(params[0]);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        if (this.onDeletedEntityListener != null) {
            this.onDeletedEntityListener.onDeletedEntity(result);
        }
    }
}

