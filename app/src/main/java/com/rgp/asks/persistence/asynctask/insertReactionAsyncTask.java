package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.auxiliaries.Constants;
import com.rgp.asks.messages.CreatedReactionEvent;
import com.rgp.asks.messages.CreatingReactionEvent;
import com.rgp.asks.persistence.dao.ReactionDao;
import com.rgp.asks.persistence.entity.Reaction;

import org.greenrobot.eventbus.EventBus;

public class insertReactionAsyncTask extends AsyncTask<Reaction, Void, Long> {
    private ReactionDao mAsyncTaskDao;

    public insertReactionAsyncTask(ReactionDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected void onPreExecute() {
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
