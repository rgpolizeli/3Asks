package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.interfaces.OnInsertedEntityListener;
import com.rgp.asks.persistence.dao.ArgumentDao;
import com.rgp.asks.persistence.entity.Argument;

public class insertArgumentAsyncTask extends AsyncTask<Argument, Void, Long> {
    private ArgumentDao dao;
    private OnInsertedEntityListener onInsertedEntityListener;

    public insertArgumentAsyncTask(ArgumentDao dao, OnInsertedEntityListener onInsertedEntityListener) {
        this.dao = dao;
        this.onInsertedEntityListener = onInsertedEntityListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Long doInBackground(final Argument... params) {
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
