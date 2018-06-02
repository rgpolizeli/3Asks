package com.example.myapplication.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.myapplication.persistence.asynctask.insertBeliefAsyncTask;
import com.example.myapplication.persistence.asynctask.insertEpisodeAsyncTask;
import com.example.myapplication.persistence.asynctask.insertReactionAsyncTask;
import com.example.myapplication.persistence.asynctask.saveEpisodeAsyncTask;
import com.example.myapplication.persistence.dao.ArgumentDao;
import com.example.myapplication.persistence.dao.BeliefDao;
import com.example.myapplication.persistence.dao.BeliefThinkingStyleDao;
import com.example.myapplication.persistence.dao.EpisodeDao;
import com.example.myapplication.persistence.dao.ObjectionDao;
import com.example.myapplication.persistence.dao.ReactionDao;
import com.example.myapplication.persistence.dao.ThinkingStyleDao;
import com.example.myapplication.persistence.entity.Argument;
import com.example.myapplication.persistence.entity.Belief;
import com.example.myapplication.persistence.entity.Episode;
import com.example.myapplication.persistence.entity.Objection;
import com.example.myapplication.persistence.entity.Reaction;
import com.example.myapplication.persistence.entity.ThinkingStyle;

import java.util.Date;
import java.util.List;

public class Repository {

    private ArgumentDao argumentDao;
    private BeliefDao beliefDao;
    private BeliefThinkingStyleDao beliefThinkingStyleDao;
    private EpisodeDao episodeDao;
    private ObjectionDao objectionDao;
    private ReactionDao reactionDao;
    private ThinkingStyleDao thinkingStyleDao;

    public Repository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);

        this.reactionDao = db.reactionDao();
        this.argumentDao = db.argumentDao();
        this.beliefDao = db.beliefDao();
        this.beliefThinkingStyleDao = db.beliefThinkingStyleDao();
        this.episodeDao = db.episodeDao();
        this.objectionDao = db.objectionDao();
        this.thinkingStyleDao = db.thinkingStyleDao();
    }

       ///////////////
    // READ OPERATIONS //
      ///////////////

    public LiveData<List<Episode>> getAllEpisodes(){
        return this.episodeDao.getAllEpisodes();
    }

    public LiveData<Episode> getEpisodeById(@NonNull int episodeId){
        return this.episodeDao.getEpisodeById(episodeId);
    }

    public LiveData<List<Reaction>> getReactionsForEpisode(@NonNull int episodeId){
        return this.reactionDao.getReactionsForEpisode(episodeId);
    }

    public LiveData<List<Belief>> getBeliefsForEpisode(@NonNull int episodeId){
        return this.beliefDao.getBeliefsForEpisode(episodeId);
    }

    public LiveData<List<Argument>> getArgumentsForBelief(@NonNull int beliefId){
        return this.argumentDao.getArgumentsForBelief(beliefId);
    }

    public LiveData<List<Objection>> getObjectionsForBelief(@NonNull int beliefId){
        return this.objectionDao.getObjectionsForBelief(beliefId);
    }

    public LiveData<List<ThinkingStyle>> getThinkingStylesForBelief(@NonNull int beliefId){
        return this.beliefThinkingStyleDao.getThinkingStylesForBelief(beliefId);
    }

       ///////////////
    // CREATE OPERATIONS //
       ///////////////

    public void createEpisode(){
        Episode e = new Episode("","",new Date(),"");
        new insertEpisodeAsyncTask(this.episodeDao).execute(e);
    }


    public void createArgumentForBelief(int beliefId){
    }

    public void createReactionForEpisode(@NonNull final int episodeId) {
        Reaction r = new Reaction("","",episodeId);
        new insertReactionAsyncTask(this.reactionDao).execute(r);
    }

    public void createBeliefForEpisode(@NonNull final int episodeId) {
        Belief b = new Belief("",episodeId);
        new insertBeliefAsyncTask(this.beliefDao).execute(b);
    }

    public void saveEpisode(@NonNull final Episode newEpisode) {
        new saveEpisodeAsyncTask(this.episodeDao).execute(newEpisode);
    }


    ///////////////
    // UPDATE OPERATIONS //
       ///////////////




}
