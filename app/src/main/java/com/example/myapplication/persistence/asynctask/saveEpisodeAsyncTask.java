package com.example.myapplication.persistence.asynctask;

import android.os.AsyncTask;

import com.example.myapplication.persistence.dao.EpisodeDao;
import com.example.myapplication.persistence.dao.ReactionDao;
import com.example.myapplication.persistence.entity.Episode;
import com.example.myapplication.persistence.entity.Reaction;

public class saveEpisodeAsyncTask extends AsyncTask<Episode, Void, Long> {
    private EpisodeDao mAsyncTaskDao;

    public saveEpisodeAsyncTask(EpisodeDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected Long doInBackground(final Episode... params) {
        long id = mAsyncTaskDao.update(params[0]);
        return id;
    }

    @Override
    protected void onPostExecute(Long result) {
        super.onPostExecute(result);

    }
}
