package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.persistence.dao.ReactionDao;
import com.rgp.asks.persistence.entity.Reaction;

public class deleteReactionAsyncTask extends AsyncTask<Reaction, Void, Integer> {
    private ReactionDao mAsyncTaskDao;

    public deleteReactionAsyncTask(ReactionDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Integer doInBackground(final Reaction... params) {
        return mAsyncTaskDao.delete(params[0]);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
    }
}
