package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.messages.CreatedEpisodeEvent;
import com.rgp.asks.persistence.dao.EpisodeDao;
import com.rgp.asks.persistence.entity.Episode;

import org.greenrobot.eventbus.EventBus;

public class insertEpisodeAsyncTask extends AsyncTask<Episode, Void, Long> {
    private EpisodeDao mAsyncTaskDao;

    public insertEpisodeAsyncTask(EpisodeDao dao) {
        this.mAsyncTaskDao = dao;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Long doInBackground(final Episode... params) {
        return mAsyncTaskDao.insert(params[0]);
    }

    @Override
    protected void onPostExecute(Long episodeId) {
        super.onPostExecute(episodeId);
        EventBus.getDefault().post(new CreatedEpisodeEvent(episodeId.intValue()));
    }
}
