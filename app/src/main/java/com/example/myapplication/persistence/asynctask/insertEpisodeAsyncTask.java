package com.example.myapplication.persistence.asynctask;

import android.os.AsyncTask;

import com.example.myapplication.persistence.dao.ArgumentDao;
import com.example.myapplication.persistence.dao.EpisodeDao;
import com.example.myapplication.persistence.entity.Argument;
import com.example.myapplication.persistence.entity.Episode;

public class insertEpisodeAsyncTask extends AsyncTask<Episode, Void, Long> {
    private EpisodeDao mAsyncTaskDao;

    public insertEpisodeAsyncTask(EpisodeDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected Long doInBackground(final Episode... params) {
        long id = mAsyncTaskDao.insert(params[0]);
        return id;
    }

    @Override
    protected void onPostExecute(Long result) {
        super.onPostExecute(result);

    }
}
