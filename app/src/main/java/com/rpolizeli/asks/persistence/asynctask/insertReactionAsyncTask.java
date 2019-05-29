package com.rpolizeli.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rpolizeli.asks.auxiliaries.Constants;
import com.rpolizeli.asks.messages.CreatedReactionEvent;
import com.rpolizeli.asks.messages.CreatingReactionEvent;
import com.rpolizeli.asks.persistence.dao.ReactionDao;
import com.rpolizeli.asks.persistence.entity.Reaction;

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
