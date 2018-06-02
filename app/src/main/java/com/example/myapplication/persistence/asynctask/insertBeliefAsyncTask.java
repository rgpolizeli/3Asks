package com.example.myapplication.persistence.asynctask;

import android.os.AsyncTask;

import com.example.myapplication.persistence.dao.BeliefDao;
import com.example.myapplication.persistence.dao.ReactionDao;
import com.example.myapplication.persistence.entity.Belief;
import com.example.myapplication.persistence.entity.Reaction;

public class insertBeliefAsyncTask extends AsyncTask<Belief, Void, Long> {
    private BeliefDao mAsyncTaskDao;

    public insertBeliefAsyncTask(BeliefDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected Long doInBackground(final Belief... params) {
        long id = mAsyncTaskDao.insert(params[0]);
        return id;
    }

    @Override
    protected void onPostExecute(Long result) {
        super.onPostExecute(result);

    }
}
