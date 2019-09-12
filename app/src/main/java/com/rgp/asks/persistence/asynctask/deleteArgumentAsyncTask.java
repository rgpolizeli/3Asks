package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.interfaces.OnDeletedEntityListener;
import com.rgp.asks.persistence.dao.ArgumentDao;
import com.rgp.asks.persistence.entity.Argument;

public class deleteArgumentAsyncTask extends AsyncTask<Argument, Void, Integer> {
    private ArgumentDao dao;
    private OnDeletedEntityListener onDeletedEntityListener;

    public deleteArgumentAsyncTask(ArgumentDao dao, OnDeletedEntityListener onDeletedEntityListener) {
        this.dao = dao;
        this.onDeletedEntityListener = onDeletedEntityListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(final Argument... params) {
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

