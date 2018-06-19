package com.example.myapplication.persistence.asynctask;

import android.os.AsyncTask;

import com.example.myapplication.auxiliaries.Constants;
import com.example.myapplication.messages.CreatedEpisodeEvent;
import com.example.myapplication.messages.CreatedReactionEvent;
import com.example.myapplication.messages.CreatingEpisodeEvent;
import com.example.myapplication.messages.CreatingReactionEvent;
import com.example.myapplication.persistence.dao.ReactionDao;
import com.example.myapplication.persistence.entity.Reaction;

import org.greenrobot.eventbus.EventBus;

public class insertReactionAsyncTask extends AsyncTask<Reaction, Void, Long> {
    private ReactionDao mAsyncTaskDao;

    public insertReactionAsyncTask(ReactionDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        EventBus.getDefault().post(new CreatingReactionEvent(Constants.START_CREATE_REACTION_MESSAGE));
    }

    @Override
    protected Long doInBackground(final Reaction... params) {
        long id = mAsyncTaskDao.insert(params[0]);
        return id;
    }

    @Override
    protected void onPostExecute(Long reactionId) {
        super.onPostExecute(reactionId);
        EventBus.getDefault().post(new CreatedReactionEvent(reactionId.intValue()));

    }
}
