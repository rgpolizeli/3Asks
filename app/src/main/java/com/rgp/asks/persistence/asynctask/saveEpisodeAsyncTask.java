package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.auxiliaries.Constants;
import com.rgp.asks.messages.SavedEditedEpisodeEvent;
import com.rgp.asks.persistence.dao.EpisodeDao;
import com.rgp.asks.persistence.entity.Episode;

import org.greenrobot.eventbus.EventBus;

public class saveEpisodeAsyncTask extends AsyncTask<Episode, Void, Integer> {
    private EpisodeDao mAsyncTaskDao;

    public saveEpisodeAsyncTask(EpisodeDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected Integer doInBackground(final Episode... params) {
        int result = mAsyncTaskDao.update(params[0]);
        return result;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        if (result == 1) {
            EventBus.getDefault().post(new SavedEditedEpisodeEvent(Constants.SAVED_EDITED_EPISODE_MESSAGE));
        }
    }
}
