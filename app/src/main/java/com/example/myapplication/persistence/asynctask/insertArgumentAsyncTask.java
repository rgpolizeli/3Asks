package com.example.myapplication.persistence.asynctask;

import android.os.AsyncTask;

import com.example.myapplication.auxiliaries.Constants;
import com.example.myapplication.messages.CreatedReactionEvent;
import com.example.myapplication.messages.CreatingReactionEvent;
import com.example.myapplication.persistence.dao.ArgumentDao;
import com.example.myapplication.persistence.entity.Argument;

import org.greenrobot.eventbus.EventBus;

public class insertArgumentAsyncTask extends AsyncTask<Argument, Void, Long> {
    private ArgumentDao mAsyncTaskDao;

    public insertArgumentAsyncTask(ArgumentDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        //EventBus.getDefault().post(new CreatingReactionEvent(Constants.START_CREATE_REACTION_MESSAGE));
    }

    @Override
    protected Long doInBackground(final Argument... params) {
        long id = mAsyncTaskDao.insert(params[0]);
        return id;
    }

    @Override
    protected void onPostExecute(Long argumentId) {
        super.onPostExecute(argumentId);
        //EventBus.getDefault().post(new CreatedReactionEvent(argumentId.intValue()));
    }
}
