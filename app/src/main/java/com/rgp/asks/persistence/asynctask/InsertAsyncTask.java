package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.rgp.asks.interfaces.OnInsertedEntityListener;
import com.rgp.asks.persistence.dao.EntityDao;

public class InsertAsyncTask<T> extends AsyncTask<T, Void, Long> {
    private EntityDao<T> dao;
    private OnInsertedEntityListener onInsertedEntityListener;

    public InsertAsyncTask(@NonNull EntityDao<T> dao, OnInsertedEntityListener onInsertedEntityListener) {
        this.dao = dao;
        this.onInsertedEntityListener = onInsertedEntityListener;
    }

    @SafeVarargs
    @Override
    protected final Long doInBackground(final T... params) {
        return dao.insert(params[0]);
    }

    @Override
    protected void onPostExecute(Long id) {
        super.onPostExecute(id);
        if (this.onInsertedEntityListener != null) {
            onInsertedEntityListener.onInsertedEntity(id.intValue());
        }
    }
}
