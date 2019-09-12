package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.interfaces.OnUpdatedEntityListener;
import com.rgp.asks.persistence.dao.ArgumentDao;
import com.rgp.asks.persistence.entity.Argument;

public class saveArgumentAsyncTask extends AsyncTask<Argument, Void, Integer> {
    private ArgumentDao dao;
    private OnUpdatedEntityListener onUpdatedEntityListener;

    public saveArgumentAsyncTask(ArgumentDao dao, OnUpdatedEntityListener onUpdatedEntityListener) {
        this.dao = dao;
        this.onUpdatedEntityListener = onUpdatedEntityListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Integer doInBackground(final Argument... params) {
        return dao.update(params[0]);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        if (this.onUpdatedEntityListener != null) {
            this.onUpdatedEntityListener.onUpdatedEntity(result);
        }
    }
}

