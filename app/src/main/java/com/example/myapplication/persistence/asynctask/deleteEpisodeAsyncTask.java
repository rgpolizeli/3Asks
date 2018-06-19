package com.example.myapplication.persistence.asynctask;

import android.os.AsyncTask;

import com.example.myapplication.messages.DeletedEpisodeEvent;
import com.example.myapplication.persistence.dao.EpisodeDao;
import com.example.myapplication.persistence.dao.ReactionDao;
import com.example.myapplication.persistence.entity.Episode;
import com.example.myapplication.persistence.entity.Reaction;

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
        EventBus.getDefault().post(new DeletedEpisodeEvent(result==1,deletedEpisodeId));
    }
}

