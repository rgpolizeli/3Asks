package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.rgp.asks.interfaces.OnDeletedEntityListener;
import com.rgp.asks.persistence.dao.EntityDao;

public class DeleteAsyncTask<T> extends AsyncTask<T, Void, Integer> {
    private EntityDao<T> dao;
    private OnDeletedEntityListener onDeletedEntityListener;

    public DeleteAsyncTask(@NonNull EntityDao<T> dao, OnDeletedEntityListener onDeletedEntityListener) {
        this.dao = dao;
        this.onDeletedEntityListener = onDeletedEntityListener;
    }

    @SafeVarargs
    @Override
    protected final Integer doInBackground(final T... params) {
        return dao.delete(params[0]);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        if (this.onDeletedEntityListener != null) {
            this.onDeletedEntityListener.onDeletedEntity(result);
        }
    }
}

