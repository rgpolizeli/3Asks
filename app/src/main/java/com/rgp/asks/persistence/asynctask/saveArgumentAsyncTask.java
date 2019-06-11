package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.persistence.dao.ArgumentDao;
import com.rgp.asks.persistence.entity.Argument;

public class saveArgumentAsyncTask extends AsyncTask<Argument, Void, Integer> {
    private ArgumentDao mAsyncTaskDao;

    public saveArgumentAsyncTask(ArgumentDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Integer doInBackground(final Argument... params) {
        int result = mAsyncTaskDao.update(params[0]);
        return result;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

    }
}

