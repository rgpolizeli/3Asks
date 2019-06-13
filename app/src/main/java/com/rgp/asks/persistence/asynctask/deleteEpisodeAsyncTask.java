package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.messages.DeletedEpisodeEvent;
import com.rgp.asks.persistence.dao.EpisodeDao;
import com.rgp.asks.persistence.entity.Episode;

import org.greenrobot.eventbus.EventBus;

public class deleteEpisodeAsyncTask extends AsyncTask<Episode, Void, Integer> {
    private EpisodeDao mAsyncTaskDao;
    private int deletedEpisodeId;

    public deleteEpisodeAsyncTask(EpisodeDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(final Episode... params) {
        deletedEpisodeId = params[0].getId();
        return mAsyncTaskDao.delete(params[0]);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        EventBus.getDefault().post(new DeletedEpisodeEvent(result == 1, deletedEpisodeId));
    }
}

