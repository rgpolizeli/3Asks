package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.rgp.asks.interfaces.OnUpdatedEntityListener;
import com.rgp.asks.persistence.dao.EntityDao;

public class UpdateAsyncTask<T> extends AsyncTask<T, Void, Integer> {
    private EntityDao<T> dao;
    private boolean finishSignal;
    private OnUpdatedEntityListener onUpdatedEntityListener;

    public UpdateAsyncTask(@NonNull EntityDao<T> dao, boolean finishSignal, OnUpdatedEntityListener onUpdatedEntityListener) {
        this.dao = dao;
        this.finishSignal = finishSignal;
        this.onUpdatedEntityListener = onUpdatedEntityListener;
    }

    @SafeVarargs
    @Override
    protected final Integer doInBackground(final T... params) {
        return dao.update(params[0]);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        if (this.onUpdatedEntityListener != null) {
            this.onUpdatedEntityListener.onUpdatedEntity(this.finishSignal, result);
        }
    }
}
