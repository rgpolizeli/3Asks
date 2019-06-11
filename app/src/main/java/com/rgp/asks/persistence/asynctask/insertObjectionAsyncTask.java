package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.persistence.dao.ObjectionDao;
import com.rgp.asks.persistence.entity.Objection;

public class insertObjectionAsyncTask extends AsyncTask<Objection, Void, Long> {
    private ObjectionDao mAsyncTaskDao;

    public insertObjectionAsyncTask(ObjectionDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        //EventBus.getDefault().post(new CreatingReactionEvent(Constants.START_CREATE_REACTION_MESSAGE));
    }

    @Override
    protected Long doInBackground(final Objection... params) {
        long id = mAsyncTaskDao.insert(params[0]);
        return id;
    }

    @Override
    protected void onPostExecute(Long objectionId) {
        super.onPostExecute(objectionId);
        //EventBus.getDefault().post(new CreatedReactionEvent(objectionId.intValue()));
    }
}
