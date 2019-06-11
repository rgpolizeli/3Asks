package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.persistence.dao.ArgumentDao;
import com.rgp.asks.persistence.entity.Argument;

public class deleteArgumentAsyncTask extends AsyncTask<Argument, Void, Integer> {
    private ArgumentDao mAsyncTaskDao;

    public deleteArgumentAsyncTask(ArgumentDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Integer doInBackground(final Argument... params) {
        return mAsyncTaskDao.delete(params[0]);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
    }
}

