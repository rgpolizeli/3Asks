package com.example.myapplication;

import java.util.ArrayList;

public class Belief {
    private String beliefName;
    private ArrayList<String> selectedThinkingStylesList;
    private ArrayList<String> beliefsList;
    private ArrayList<String> reactionsList;

    public Belief() {
        beliefName = "";
        selectedThinkingStylesList = new ArrayList<>();
        beliefsList = new ArrayList<>();
        reactionsList = new ArrayList<>();
    }

    public void loadBelief(String beliefName, ArrayList<String> selectedThinkingStylesList, ArrayList<String> beliefsList, ArrayList<String> reactionsList){
        this.beliefName = beliefName;
        this.selectedThinkingStylesList = selectedThinkingStylesList;
        this.beliefsList = beliefsList;
        this.reactionsList = reactionsList;
    }

    public ArrayList<String> getBeliefsList(){
        return this.beliefsList;
    }

    public ArrayList<String> getReactionsList(){
        return this.reactionsList;
    }
    public ArrayList<String> getSelectedThinkingStylesList(){
        return this.selectedThinkingStylesList;
    }

}
