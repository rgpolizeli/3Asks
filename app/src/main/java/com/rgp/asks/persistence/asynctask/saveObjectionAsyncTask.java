package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.persistence.dao.ObjectionDao;
import com.rgp.asks.persistence.entity.Objection;

public class saveObjectionAsyncTask extends AsyncTask<Objection, Void, Integer> {
    private ObjectionDao mAsyncTaskDao;

    public saveObjectionAsyncTask(ObjectionDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Integer doInBackground(final Objection... params) {
        int result = mAsyncTaskDao.update(params[0]);
        return result;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

    }
}

