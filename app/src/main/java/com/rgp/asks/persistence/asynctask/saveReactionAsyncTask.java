package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.interfaces.OnUpdatedEntityListener;
import com.rgp.asks.persistence.dao.ReactionDao;
import com.rgp.asks.persistence.entity.Reaction;

public class saveReactionAsyncTask extends AsyncTask<Reaction, Void, Integer> {
    private ReactionDao dao;
    private OnUpdatedEntityListener onUpdatedEntityListener;

    public saveReactionAsyncTask(ReactionDao dao, OnUpdatedEntityListener onUpdatedEntityListener) {
        this.dao = dao;
        this.onUpdatedEntityListener = onUpdatedEntityListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(final Reaction... params) {
        return dao.update(params[0]);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        if (this.onUpdatedEntityListener != null) {
            this.onUpdatedEntityListener.onUpdatedEntity(result);
        }
    }
}

