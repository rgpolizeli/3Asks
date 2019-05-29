package com.rpolizeli.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rpolizeli.asks.persistence.dao.ObjectionDao;
import com.rpolizeli.asks.persistence.entity.Objection;

public class deleteObjectionAsyncTask extends AsyncTask<Objection, Void, Integer> {
    private ObjectionDao mAsyncTaskDao;

    public deleteObjectionAsyncTask(ObjectionDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Integer doInBackground(final Objection... params) {
        return mAsyncTaskDao.delete(params[0]);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
    }
}

