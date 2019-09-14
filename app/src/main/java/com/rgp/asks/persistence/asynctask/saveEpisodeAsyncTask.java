package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.interfaces.OnUpdatedEntityListener;
import com.rgp.asks.persistence.dao.EpisodeDao;
import com.rgp.asks.persistence.entity.Episode;

public class saveEpisodeAsyncTask extends AsyncTask<Episode, Void, Integer> {
    private EpisodeDao dao;
    private OnUpdatedEntityListener onUpdatedEntityListener;

    public saveEpisodeAsyncTask(EpisodeDao dao, OnUpdatedEntityListener onUpdatedEntityListener) {
        this.dao = dao;
        this.onUpdatedEntityListener = onUpdatedEntityListener;
    }

    @Override
    protected Integer doInBackground(final Episode... params) {
        return dao.update(params[0]);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        if (this.onUpdatedEntityListener != null) {
            this.onUpdatedEntityListener.onUpdatedEntity(result);
        }
    }
}
