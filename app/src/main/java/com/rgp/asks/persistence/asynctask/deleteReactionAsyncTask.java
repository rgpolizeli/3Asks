package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.interfaces.OnDeletedEntityListener;
import com.rgp.asks.persistence.dao.ReactionDao;
import com.rgp.asks.persistence.entity.Reaction;

public class deleteReactionAsyncTask extends AsyncTask<Reaction, Void, Integer> {
    private ReactionDao dao;
    private OnDeletedEntityListener onDeletedEntityListener;

    public deleteReactionAsyncTask(ReactionDao dao, OnDeletedEntityListener onDeletedEntityListener) {
        this.dao = dao;
        this.onDeletedEntityListener = onDeletedEntityListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(final Reaction... params) {
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

