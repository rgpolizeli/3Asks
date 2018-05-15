package com.example.myapplication.models;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.myapplication.BeliefRepository;

import java.util.ArrayList;

public class BeliefViewModel extends ViewModel {
    private String beliefThought;
    private ArrayList<String> selectedThinkingStylesList;
    private ArrayList<String> argumentsList;
    private ArrayList<String> objectionsList;
    private BeliefRepository beliefRepository;

    public String getBeliefThought() {
        if (beliefThought == null) {
            beliefThought = "";
            loadBelief();
        }
        return beliefThought;
    }

    public ArrayList<String> getSelectedThinkingStyles() {
        if (selectedThinkingStylesList == null) {
            selectedThinkingStylesList = new ArrayList<String>();
            loadBelief();
        }
        return selectedThinkingStylesList;
    }

    public ArrayList<String> getArguments() {
        if (argumentsList == null) {
            argumentsList = new ArrayList<String>();
            loadBelief();
        }
        return argumentsList;
    }

    public ArrayList<String> getObjections() {
        if (objectionsList == null) {
            objectionsList = new ArrayList<String>();
            loadBelief();
        }
        return objectionsList;
    }

    public void setBeliefThought(String newThought){
        getBeliefThought();
        this.beliefThought = newThought;
    }

    public void addUnhelpfulThinkingStyle(String unhelpfulThinkingStyleLabel){
        ArrayList<String>  selectedUnhelpfulThinkingStyles = getSelectedThinkingStyles();
        selectedUnhelpfulThinkingStyles.add(unhelpfulThinkingStyleLabel);
    }

    public void removeUnhelpfulThinkingStyle(String unhelpfulThinkingStyleLabel){
        ArrayList<String>  selectedUnhelpfulThinkingStyles = getSelectedThinkingStyles();
        selectedUnhelpfulThinkingStyles.remove(unhelpfulThinkingStyleLabel);
    }

    public void addArgument(){
        ArrayList<String> arguments = getArguments();
        arguments.add("");
    }

    public void removeArgument(String argumentLabel){
        ArrayList<String> arguments = getArguments();
        arguments.remove(argumentLabel);
    }

    public void addObjection(){
        ArrayList<String> objections = getObjections();
        objections.add("");
    }

    public void removeObjection(String objectionLabel){
        ArrayList<String> objections = getObjections();
        objections.remove(objectionLabel);
    }

    private void loadBelief() {
        // Do an asynchronous operation to fetch users.




    }

    public void saveBelief(){
        Log.d("kkkk","kkkk");
    }
}
