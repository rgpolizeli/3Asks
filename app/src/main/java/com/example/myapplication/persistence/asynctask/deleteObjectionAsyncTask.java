package com.example.myapplication.persistence.asynctask;

import android.os.AsyncTask;

import com.example.myapplication.persistence.dao.ArgumentDao;
import com.example.myapplication.persistence.dao.ObjectionDao;
import com.example.myapplication.persistence.entity.Argument;
import com.example.myapplication.persistence.entity.Objection;

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

