package com.example.myapplication.persistence.asynctask;

import android.os.AsyncTask;

import com.example.myapplication.persistence.dao.ArgumentDao;
import com.example.myapplication.persistence.dao.ObjectionDao;
import com.example.myapplication.persistence.entity.Argument;
import com.example.myapplication.persistence.entity.Objection;

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

