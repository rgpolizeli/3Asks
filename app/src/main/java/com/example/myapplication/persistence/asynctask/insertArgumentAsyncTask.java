package com.example.myapplication.persistence.asynctask;

import android.os.AsyncTask;

import com.example.myapplication.persistence.dao.ArgumentDao;
import com.example.myapplication.persistence.entity.Argument;

public class insertArgumentAsyncTask extends AsyncTask<Argument, Void, Long> {
    private ArgumentDao mAsyncTaskDao;

    insertArgumentAsyncTask(ArgumentDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected Long doInBackground(final Argument... params) {
        long id = mAsyncTaskDao.insert(params[0]);
        return id;
    }

    @Override
    protected void onPostExecute(Long result) {
        super.onPostExecute(result);

    }
}
