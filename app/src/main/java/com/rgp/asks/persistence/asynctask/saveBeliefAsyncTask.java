package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import com.rgp.asks.interfaces.OnUpdatedEntityListener;
import com.rgp.asks.persistence.dao.BeliefDao;
import com.rgp.asks.persistence.dao.BeliefThinkingStyleDao;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.persistence.entity.BeliefThinkingStyle;
import com.rgp.asks.persistence.entity.ThinkingStyle;

import java.util.List;

public class saveBeliefAsyncTask extends AsyncTask<Belief, Void, Boolean> {
    private BeliefDao dao;
    private BeliefThinkingStyleDao beliefThinkingStyleDao;
    private List<ThinkingStyle> toDeleteThinkingStyles;
    private List<ThinkingStyle> toInsertThinkingStyles;
    private OnUpdatedEntityListener onUpdatedEntityListener;

    public saveBeliefAsyncTask(BeliefDao dao, BeliefThinkingStyleDao beliefThinkingStyleDao, List<ThinkingStyle> toDeleteThinkingStyles, List<ThinkingStyle> toInsertThinkingStyles, OnUpdatedEntityListener onUpdatedEntityListener) {
        this.dao = dao;
        this.beliefThinkingStyleDao = beliefThinkingStyleDao;
        this.toDeleteThinkingStyles = toDeleteThinkingStyles;
        this.toInsertThinkingStyles = toInsertThinkingStyles;
        this.onUpdatedEntityListener = onUpdatedEntityListener;
    }

    @Override
    protected Boolean doInBackground(final Belief... params) {
        Belief b = params[0];

        boolean deleteThinkingStylesResult = this.deleteThinkingStyles(b.getId());
        this.insertThinkingStyles(b.getId());
        int updateThoughtResult = dao.update(params[0]);

        return deleteThinkingStylesResult && updateThoughtResult == 1;
    }

    private boolean deleteThinkingStyles(final int beliefId) {
        boolean result = true;
        int intermediateResult;
        for (ThinkingStyle th : toDeleteThinkingStyles) {
            intermediateResult = beliefThinkingStyleDao.delete(new BeliefThinkingStyle(beliefId, th.getThinkingStyle()));
            if (intermediateResult == 0) {
                result = false;
            }
        }
        return result;
    }

    private void insertThinkingStyles(final int beliefId) {
        for (ThinkingStyle th : toInsertThinkingStyles) {
            beliefThinkingStyleDao.insert(new BeliefThinkingStyle(beliefId, th.getThinkingStyle()));
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (this.onUpdatedEntityListener != null) {
            if (result) {
                onUpdatedEntityListener.onUpdatedEntity(1);
            } else {
                onUpdatedEntityListener.onUpdatedEntity(0);
            }
        }
    }
}
