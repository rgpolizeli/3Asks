package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.interfaces.OnDeletedEntityListener;
import com.rgp.asks.persistence.dao.ObjectionDao;
import com.rgp.asks.persistence.entity.Objection;

public class deleteObjectionAsyncTask extends AsyncTask<Objection, Void, Integer> {
    private ObjectionDao dao;
    private OnDeletedEntityListener onDeletedEntityListener;

    public deleteObjectionAsyncTask(ObjectionDao dao, OnDeletedEntityListener onDeletedEntityListener) {
        this.dao = dao;
        this.onDeletedEntityListener = onDeletedEntityListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(final Objection... params) {
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

