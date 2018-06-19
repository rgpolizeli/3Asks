package com.example.myapplication.persistence.asynctask;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.myapplication.auxiliaries.Constants;
import com.example.myapplication.messages.SavedEditedBeliefEvent;
import com.example.myapplication.persistence.dao.BeliefDao;
import com.example.myapplication.persistence.dao.BeliefThinkingStyleDao;
import com.example.myapplication.persistence.dao.EpisodeDao;
import com.example.myapplication.persistence.entity.Belief;
import com.example.myapplication.persistence.entity.BeliefThinkingStyle;
import com.example.myapplication.persistence.entity.Episode;
import com.example.myapplication.persistence.entity.ThinkingStyle;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class saveBeliefAsyncTask extends AsyncTask<Belief, Void, Boolean> {
    private BeliefDao beliefDao;
    private BeliefThinkingStyleDao beliefThinkingStyleDao;
    private List<ThinkingStyle> toDeleteThinkingStyles;
    private List<ThinkingStyle> toInsertThinkingStyles;

    public saveBeliefAsyncTask(BeliefDao beliefDao, BeliefThinkingStyleDao beliefThinkingStyleDao, List<ThinkingStyle> toDeleteThinkingStyles, List<ThinkingStyle> toInsertThinkingStyles) {
        this.beliefDao = beliefDao;
        this.beliefThinkingStyleDao = beliefThinkingStyleDao;
        this.toDeleteThinkingStyles = toDeleteThinkingStyles;
        this.toInsertThinkingStyles = toInsertThinkingStyles;

    }

    @Override
    protected Boolean doInBackground(final Belief... params) {
        Belief b = params[0];

        boolean deleteThinkingStylesResult = this.deleteThinkingStyles(b.getId());
        this.insertThinkingStyles(b.getId());
        int updateThoughtResult = beliefDao.update(params[0]);

        if (deleteThinkingStylesResult && updateThoughtResult==1){
            return true;
        } else{
            return false;
        }
    }

    private boolean deleteThinkingStyles(@NonNull final int beliefId){
        boolean result = true;
        int intermediateResult;
        for(ThinkingStyle th : toDeleteThinkingStyles){
            intermediateResult = beliefThinkingStyleDao.delete(new BeliefThinkingStyle(beliefId,th.getThinkingStyle()));
            if (intermediateResult==0){
                result = false;
            }
        }
        return result;
    }

    private void insertThinkingStyles(@NonNull final int beliefId){

        for(ThinkingStyle th : toInsertThinkingStyles){
            beliefThinkingStyleDao.insert(new BeliefThinkingStyle(beliefId,th.getThinkingStyle()));
        }

    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (result){
            EventBus.getDefault().post(new SavedEditedBeliefEvent(Constants.SAVED_EDITED_BELIEF_MESSAGE));
        }
    }
}
