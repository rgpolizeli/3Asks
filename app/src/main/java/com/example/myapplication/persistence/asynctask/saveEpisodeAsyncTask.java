package com.example.myapplication.persistence.asynctask;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.myapplication.activities.AsksActivity;
import com.example.myapplication.auxiliaries.Constants;
import com.example.myapplication.messages.SavedEditedBeliefEvent;
import com.example.myapplication.messages.SavedEditedEpisodeEvent;
import com.example.myapplication.persistence.dao.EpisodeDao;
import com.example.myapplication.persistence.entity.Episode;

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
