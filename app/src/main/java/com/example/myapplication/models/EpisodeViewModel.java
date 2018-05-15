package com.example.myapplication.models;

import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;

public class EpisodeViewModel extends ViewModel {
    private String episodeName;
    private String episodeDescription;
    private String episodeDate;
    private String episodePeriod;
    private ArrayList<Reaction> reactions;
    private ArrayList<Belief> beliefs;

    public String getEpisodeName() {
        if (episodeName == null) {
            episodeName = "";
            loadEpisode();
        }
        return this.episodeName;
    }

    public String getEpisodeDescription() {
        if (episodeDescription == null) {
            episodeDescription = "";
            loadEpisode();
        }
        return this.episodeDescription;
    }

    public String getEpisodeDate() {
        if (episodeDate == null) {
            episodeDate = "";
            loadEpisode();
        }
        return this.episodeDate;
    }

    public String getEpisodePeriod() {
        if (episodePeriod == null) {
            episodePeriod = "";
            loadEpisode();
        }
        return this.episodePeriod;
    }

    public ArrayList<Reaction> getReactions() {
        if (reactions == null) {
            reactions = new ArrayList<Reaction>();
            loadEpisode();
        }
        return this.reactions;
    }

    public ArrayList<Belief> getBeliefs() {
        if (beliefs == null) {
            beliefs = new ArrayList<Belief>();
            loadEpisode();
        }
        return this.beliefs;
    }

    public void setEpisodeName(String newEpisodeName){
        getEpisodeName();
        this.episodeName = newEpisodeName;
    }

    public void setEpisodeDescription(String newEpisodeDescription){
        getEpisodeDescription();
        this.episodeDescription = newEpisodeDescription;
    }

    public void setEpisodeDate(String newEpisodeDate){
        getEpisodeDate();
        this.episodeDate = newEpisodeDate;
    }

    public void setEpisodePeriod(String newEpisodePeriod){
        getEpisodePeriod();
        this.episodePeriod = newEpisodePeriod;
    }

    public void addReaction(Reaction newReaction){
        ArrayList<Reaction> reactions = getReactions();
        reactions.add(newReaction);
    }

    public void removeReaction(Reaction reaction){
        ArrayList<Reaction> reactions = getReactions();
        reactions.remove(reaction);
    }

    public void addBelief(Belief newBelief){
        ArrayList<Belief> beliefs = getBeliefs();
        beliefs.add(newBelief);
    }

    public void removeBelief(Belief belief){
        ArrayList<Belief> beliefs = getBeliefs();
        beliefs.remove(belief);
    }

    private void loadEpisode() {
        // Do an asynchronous operation to fetch users.
    }
}
