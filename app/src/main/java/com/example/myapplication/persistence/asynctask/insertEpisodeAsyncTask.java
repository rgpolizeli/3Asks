package com.example.myapplication.persistence.asynctask;

import android.os.AsyncTask;

import com.example.myapplication.auxiliaries.Constants;
import com.example.myapplication.messages.CreatedEpisodeEvent;
import com.example.myapplication.messages.CreatingEpisodeEvent;
import com.example.myapplication.persistence.dao.EpisodeDao;
import com.example.myapplication.persistence.entity.Episode;

import org.greenrobot.eventbus.EventBus;

public class insertEpisodeAsyncTask extends AsyncTask<Episode, Void, Long> {
    private EpisodeDao mAsyncTaskDao;

    public insertEpisodeAsyncTask(EpisodeDao dao) {
        this.mAsyncTaskDao = dao;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        EventBus.getDefault().post(new CreatingEpisodeEvent(Constants.START_CREATE_EPISODE_MESSAGE));
    }

    @Override
    protected Long doInBackground(final Episode... params) {
        long id = mAsyncTaskDao.insert(params[0]);
        return id;
    }

    @Override
    protected void onPostExecute(Long episodeId) {
        super.onPostExecute(episodeId);
        EventBus.getDefault().post(new CreatedEpisodeEvent(episodeId.intValue()));
    }
}
