package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.messages.CreatedReactionEvent;
import com.rgp.asks.persistence.dao.ReactionDao;
import com.rgp.asks.persistence.entity.Reaction;

import org.greenrobot.eventbus.EventBus;

public class insertReactionAsyncTask extends AsyncTask<Reaction, Void, Long> {
    private ReactionDao mAsyncTaskDao;

    public insertReactionAsyncTask(ReactionDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected Long doInBackground(final Reaction... params) {
        return mAsyncTaskDao.insert(params[0]);
    }

    @Override
    protected void onPostExecute(Long reactionId) {
        super.onPostExecute(reactionId);
        EventBus.getDefault().post(new CreatedReactionEvent(reactionId.intValue()));
    }
}
