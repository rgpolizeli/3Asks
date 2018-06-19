package com.example.myapplication.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.example.myapplication.auxiliaries.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class ReactionViewModel extends ViewModel {
    private String reaction;
    private String reactionClass;
    private String reactionJSONSource;

    public String getReaction() {
        if (reaction == null) {
            loadReaction();
        }
        return this.reaction;
    }

    public String getReactionClass() {
        if (reactionClass == null) {
            loadReaction();
        }
        return this.reactionClass;
    }

    public void setReaction(String newReaction){
        getReaction();
        this.reaction = newReaction;
    }

    public void setReactionClass(String newReactionClass){
        getReactionClass();
        this.reactionClass = newReactionClass;
    }

    public void fillReactionSource(String reactionJSONString){
        this.reactionJSONSource = reactionJSONString;
    }

    private void loadReaction() {
        if (this.reactionJSONSource == null){
            this.reaction = "";
            this.reactionClass = "";
        } else{
            try {
                JSONObject reactionJSON = new JSONObject(this.reactionJSONSource);
                this.reaction = reactionJSON.getString(Constants.JSON_REACTION);
                this.reactionClass = reactionJSON.getString(Constants.JSON_REACTION_CLASS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    public boolean isEqualToReaction(Reaction r){
        return (r.getReaction().equals(this.reaction) && r.getReactionClass().equals(this.reactionClass));
    }
    */
}
