package com.rpolizeli.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rpolizeli.asks.auxiliaries.Constants;
import com.rpolizeli.asks.messages.SavedEditedEpisodeEvent;
import com.rpolizeli.asks.persistence.dao.EpisodeDao;
import com.rpolizeli.asks.persistence.entity.Episode;

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
        if (result==1){
            EventBus.getDefault().post(new SavedEditedEpisodeEvent(Constants.SAVED_EDITED_EPISODE_MESSAGE));
        }
    }
}
