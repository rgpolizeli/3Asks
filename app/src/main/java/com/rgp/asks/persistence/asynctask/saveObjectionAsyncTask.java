package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.interfaces.OnUpdatedEntityListener;
import com.rgp.asks.persistence.dao.ObjectionDao;
import com.rgp.asks.persistence.entity.Objection;

public class saveObjectionAsyncTask extends AsyncTask<Objection, Void, Integer> {
    private ObjectionDao dao;
    private boolean finishSignal;
    private OnUpdatedEntityListener onUpdatedEntityListener;

    public saveObjectionAsyncTask(ObjectionDao dao, boolean finishSignal, OnUpdatedEntityListener onUpdatedEntityListener) {
        this.dao = dao;
        this.finishSignal = finishSignal;
        this.onUpdatedEntityListener = onUpdatedEntityListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(final Objection... params) {
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

