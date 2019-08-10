package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.persistence.dao.ReactionDao;
import com.rgp.asks.persistence.entity.Reaction;

public class saveReactionAsyncTask extends AsyncTask<Reaction, Void, Integer> {
    private ReactionDao mAsyncTaskDao;

    public saveReactionAsyncTask(ReactionDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Integer doInBackground(final Reaction... params) {
        int result = mAsyncTaskDao.update(params[0]);
        return result;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

    }
}
