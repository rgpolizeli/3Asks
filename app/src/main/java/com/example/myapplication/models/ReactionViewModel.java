package com.example.myapplication.models;

import android.arch.lifecycle.ViewModel;

public class ReactionViewModel extends ViewModel {
    private String reaction;
    private String reactionClass;

    public String getReaction() {
        if (reaction == null) {
            reaction = "";
            loadReaction();
        }
        return this.reaction;
    }

    public String getReactionClass() {
        if (reactionClass == null) {
            reactionClass = "";
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

    private void loadReaction() {
        // Do an asynchronous operation to fetch users.
    }
}
