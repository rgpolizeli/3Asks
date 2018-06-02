package com.example.myapplication.persistence.asynctask;

import android.os.AsyncTask;

import com.example.myapplication.persistence.dao.EpisodeDao;
import com.example.myapplication.persistence.dao.ReactionDao;
import com.example.myapplication.persistence.entity.Episode;
import com.example.myapplication.persistence.entity.Reaction;

public class insertReactionAsyncTask extends AsyncTask<Reaction, Void, Long> {
    private ReactionDao mAsyncTaskDao;

    public insertReactionAsyncTask(ReactionDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected Long doInBackground(final Reaction... params) {
        long id = mAsyncTaskDao.insert(params[0]);
        return id;
    }

    @Override
    protected void onPostExecute(Long result) {
        super.onPostExecute(result);

    }
}
