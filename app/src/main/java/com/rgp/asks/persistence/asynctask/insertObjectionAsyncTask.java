package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.interfaces.OnInsertedEntityListener;
import com.rgp.asks.persistence.dao.ObjectionDao;
import com.rgp.asks.persistence.entity.Objection;

public class insertObjectionAsyncTask extends AsyncTask<Objection, Void, Long> {
    private ObjectionDao dao;
    private OnInsertedEntityListener onInsertedEntityListener;

    public insertObjectionAsyncTask(ObjectionDao dao, OnInsertedEntityListener onInsertedEntityListener) {
        this.dao = dao;
        this.onInsertedEntityListener = onInsertedEntityListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Long doInBackground(final Objection... params) {
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
