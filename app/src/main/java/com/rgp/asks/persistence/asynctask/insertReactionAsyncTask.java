package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.rgp.asks.interfaces.OnInsertedEntityListener;
import com.rgp.asks.persistence.dao.ReactionDao;
import com.rgp.asks.persistence.entity.Reaction;

public class insertReactionAsyncTask extends AsyncTask<Reaction, Void, Long> {
    private ReactionDao dao;
    private OnInsertedEntityListener onInsertedEntityListener;

    public insertReactionAsyncTask(ReactionDao dao, OnInsertedEntityListener onInsertedEntityListener) {
        this.dao = dao;
        this.onInsertedEntityListener = onInsertedEntityListener;
    }

    @Override
    protected Long doInBackground(@NonNull final Reaction... params) {
        return dao.insert(params[0]);
    }

    @Override
    protected void onPostExecute(Long id) {
        super.onPostExecute(id);
        if (this.onInsertedEntityListener != null) {
            this.onInsertedEntityListener.onInsertedEntity(id.intValue());
        }
    }
}
