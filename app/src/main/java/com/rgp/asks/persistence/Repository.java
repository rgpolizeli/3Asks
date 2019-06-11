package com.rgp.asks.persistence;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.rgp.asks.persistence.asynctask.deleteArgumentAsyncTask;
import com.rgp.asks.persistence.asynctask.deleteBeliefAsyncTask;
import com.rgp.asks.persistence.asynctask.deleteEpisodeAsyncTask;
import com.rgp.asks.persistence.asynctask.deleteObjectionAsyncTask;
import com.rgp.asks.persistence.asynctask.deleteReactionAsyncTask;
import com.rgp.asks.persistence.asynctask.insertArgumentAsyncTask;
import com.rgp.asks.persistence.asynctask.insertBeliefAsyncTask;
import com.rgp.asks.persistence.asynctask.insertEpisodeAsyncTask;
import com.rgp.asks.persistence.asynctask.insertObjectionAsyncTask;
import com.rgp.asks.persistence.asynctask.insertReactionAsyncTask;
import com.rgp.asks.persistence.asynctask.saveArgumentAsyncTask;
import com.rgp.asks.persistence.asynctask.saveBeliefAsyncTask;
import com.rgp.asks.persistence.asynctask.saveEpisodeAsyncTask;
import com.rgp.asks.persistence.asynctask.saveObjectionAsyncTask;
import com.rgp.asks.persistence.asynctask.saveReactionAsyncTask;
import com.rgp.asks.persistence.dao.ArgumentDao;
import com.rgp.asks.persistence.dao.BeliefDao;
import com.rgp.asks.persistence.dao.BeliefThinkingStyleDao;
import com.rgp.asks.persistence.dao.EpisodeDao;
import com.rgp.asks.persistence.dao.ObjectionDao;
import com.rgp.asks.persistence.dao.ReactionDao;
import com.rgp.asks.persistence.dao.ThinkingStyleDao;
import com.rgp.asks.persistence.entity.Argument;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.persistence.entity.Objection;
import com.rgp.asks.persistence.entity.Reaction;
import com.rgp.asks.persistence.entity.ThinkingStyle;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Repository {
    private Application application;
    private ArgumentDao argumentDao;
    private BeliefDao beliefDao;
    private BeliefThinkingStyleDao beliefThinkingStyleDao;
    private EpisodeDao episodeDao;
    private ObjectionDao objectionDao;
    private ReactionDao reactionDao;
    private ThinkingStyleDao thinkingStyleDao;

    public Repository(Application application) {
        this.application = application;
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

    public LiveData<Belief> getBeliefById(@NonNull int beliefId){
        return this.beliefDao.getBeliefById(beliefId);
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

    public void createEpisode(@NonNull String newEpisodeName,@NonNull String newEpisodeDate,@NonNull String newEpisodePeriod){

        Date episodeDate;

        try {
            episodeDate = newEpisodeDate.isEmpty()? Calendar.getInstance().getTime() : DateFormat.getDateInstance(DateFormat.SHORT).parse(newEpisodeDate);
        } catch (ParseException e) {
            episodeDate = Calendar.getInstance().getTime();
        }

        final Episode e = new Episode(
                newEpisodeName,
                "",
                episodeDate,
                newEpisodePeriod
        );

        new insertEpisodeAsyncTask(this.episodeDao).execute(e);
    }

    public void createArgumentForBelief(@NonNull final int beliefId, @NonNull final String newArgument){
        Argument a = new Argument(
                newArgument,
                beliefId
        );
        new insertArgumentAsyncTask(this.argumentDao).execute(a);
    }

    public void editArgumentForBelief(@NonNull final Argument argument){
        new saveArgumentAsyncTask(this.argumentDao).execute(argument);
    }

    public void deleteArgumentForBelief(@NonNull final Argument argument){
        new deleteArgumentAsyncTask(this.argumentDao).execute(argument);
    }

    public void createObjectionForBelief(final int beliefId, @NonNull final String newObjection){
        Objection o = new Objection(
                newObjection,
                beliefId
        );
        new insertObjectionAsyncTask(this.objectionDao).execute(o);
    }

    public void editObjectionForBelief(@NonNull final Objection objection){
        new saveObjectionAsyncTask(this.objectionDao).execute(objection);
    }

    public void deleteObjectionForBelief(@NonNull final Objection objection){
        new deleteObjectionAsyncTask(this.objectionDao).execute(objection);
    }

    public void createReactionForEpisode(final int episodeId, @NonNull String newReaction, @NonNull String newReactionClass) {
        Reaction r = new Reaction(
                newReaction,
                newReactionClass,
                episodeId
        );
        new insertReactionAsyncTask(this.reactionDao).execute(r);
    }

    public void editReactionForEpisode(@NonNull final Reaction reaction){
        new saveReactionAsyncTask(this.reactionDao).execute(reaction);
    }

    public void deleteReactionForEpisode(@NonNull final Reaction reaction){
        new deleteReactionAsyncTask(this.reactionDao).execute(reaction);
    }

    public void createBeliefForEpisode(final int episodeId, @NonNull final String newBelief) {
        Belief b = new Belief(newBelief,episodeId);
        new insertBeliefAsyncTask(this.beliefDao).execute(b);
    }

    public void saveEpisode(@NonNull final Episode newEpisode) {
        new saveEpisodeAsyncTask(this.episodeDao).execute(newEpisode);
    }

    public void saveBelief(@NonNull final Belief newBelief, @NonNull final List<ThinkingStyle> toDeleteSelectedThinkingStyles, @NonNull final List<ThinkingStyle> toInsertSelectedThinkingStyles) {
        new saveBeliefAsyncTask(
                this.beliefDao,
                this.beliefThinkingStyleDao,
                toDeleteSelectedThinkingStyles,
                toInsertSelectedThinkingStyles
        ).execute(newBelief);
    }

    public void deleteEpisode(@NonNull final Episode episode) {
        new deleteEpisodeAsyncTask(this.episodeDao).execute(episode);
    }

    public void deleteBelief(@NonNull final Belief belief) {
        new deleteBeliefAsyncTask(this.beliefDao).execute(belief);
    }
}
