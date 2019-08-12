package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.persistence.dao.ArgumentDao;
import com.rgp.asks.persistence.entity.Argument;

public class insertArgumentAsyncTask extends AsyncTask<Argument, Void, Long> {
    private ArgumentDao mAsyncTaskDao;

    public insertArgumentAsyncTask(ArgumentDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Long doInBackground(final Argument... params) {
        long id = mAsyncTaskDao.insert(params[0]);
        return id;
    }

    @Override
    protected void onPostExecute(Long argumentId) {
        super.onPostExecute(argumentId);
    }
}
