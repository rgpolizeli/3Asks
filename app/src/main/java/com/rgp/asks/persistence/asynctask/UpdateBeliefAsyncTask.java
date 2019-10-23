package com.rgp.asks.persistence.asynctask;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.rgp.asks.interfaces.OnUpdatedEntityListener;
import com.rgp.asks.persistence.dao.BeliefThinkingStyleDao;
import com.rgp.asks.persistence.dao.EntityDao;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.persistence.entity.BeliefThinkingStyle;
import com.rgp.asks.persistence.entity.ThinkingStyle;

import java.util.List;

public class UpdateBeliefAsyncTask extends AsyncTask<Belief, Void, Integer> {
    private EntityDao<Belief> beliefDao;
    private BeliefThinkingStyleDao beliefThinkingStyleDao;
    private boolean finishSignal;
    private OnUpdatedEntityListener onUpdatedEntityListener;
    private List<ThinkingStyle> toDeleteThinkingStyles;
    private List<ThinkingStyle> toInsertThinkingStyles;

    public UpdateBeliefAsyncTask(@NonNull EntityDao<Belief> dao, @NonNull BeliefThinkingStyleDao beliefThinkingStyleDao, List<ThinkingStyle> toDeleteThinkingStyles, List<ThinkingStyle> toInsertThinkingStyles, boolean finishSignal, OnUpdatedEntityListener onUpdatedEntityListener) {
        this.beliefDao = dao;
        this.beliefThinkingStyleDao = beliefThinkingStyleDao;
        this.finishSignal = finishSignal;
        this.onUpdatedEntityListener = onUpdatedEntityListener;
        this.toDeleteThinkingStyles = toDeleteThinkingStyles;
        this.toInsertThinkingStyles = toInsertThinkingStyles;
    }

    @Override
    protected final Integer doInBackground(final Belief... params) {

        Belief b = params[0];

        boolean deleteThinkingStylesResult = this.deleteThinkingStyles(b.getId());
        this.insertThinkingStyles(b.getId());
        int updateThoughtResult = beliefDao.update(params[0]);

        if (deleteThinkingStylesResult && updateThoughtResult == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        if (this.onUpdatedEntityListener != null) {
            this.onUpdatedEntityListener.onUpdatedEntity(this.finishSignal, result);
        }
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
}
