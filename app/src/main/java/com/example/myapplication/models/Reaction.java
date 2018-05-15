package com.example.myapplication.models;

public class Reaction {
    private String reaction;
    private String reactionClass;

    public Reaction(String reaction, String reactionClass) {
        this.reaction = reaction;
        this.reactionClass = reactionClass;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public String getReactionClass() {
        return reactionClass;
    }

    public void setReactionClass(String reactionClass) {
        this.reactionClass = reactionClass;
    }
}
