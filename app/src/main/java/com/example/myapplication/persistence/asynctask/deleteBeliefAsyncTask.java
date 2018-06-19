package com.example.myapplication.persistence.asynctask;

import android.os.AsyncTask;

import com.example.myapplication.messages.DeletedBeliefEvent;
import com.example.myapplication.messages.DeletedEpisodeEvent;
import com.example.myapplication.persistence.dao.BeliefDao;
import com.example.myapplication.persistence.entity.Belief;
import com.example.myapplication.persistence.entity.Episode;

import org.greenrobot.eventbus.EventBus;

public class deleteBeliefAsyncTask extends AsyncTask<Belief, Void, Integer> {
    private BeliefDao mAsyncTaskDao;
    private int deletedBeliefId;

    public deleteBeliefAsyncTask(BeliefDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(final Belief... params) {
        deletedBeliefId = params[0].getId();
        return mAsyncTaskDao.delete(params[0]);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        EventBus.getDefault().post(new DeletedBeliefEvent(result==1, deletedBeliefId));
    }
}

