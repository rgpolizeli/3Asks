package com.rgp.asks.persistence;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.rgp.asks.interfaces.OnDeletedEntityListener;
import com.rgp.asks.interfaces.OnInsertedEntityListener;
import com.rgp.asks.interfaces.OnUpdatedEntityListener;
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

    public LiveData<List<Episode>> getAllEpisodes() {
        return this.episodeDao.getAllEpisodes();
    }

    public LiveData<Episode> getEpisodeById(int episodeId) {
        return this.episodeDao.getEpisodeById(episodeId);
    }

    public LiveData<Belief> getBeliefById(int beliefId) {
        return this.beliefDao.getBeliefById(beliefId);
    }

    public LiveData<Argument> getArgumentById(int argumentId) {
        return this.argumentDao.getArgumentById(argumentId);
    }

    public LiveData<Objection> getObjectionById(int objectionId) {
        return this.objectionDao.getObjectionById(objectionId);
    }

    public LiveData<Reaction> getReactionById(int reactionId) {
        return this.reactionDao.getReactionById(reactionId);
    }

    public LiveData<List<Reaction>> getReactionsForEpisode(int episodeId) {
        return this.reactionDao.getReactionsForEpisode(episodeId);
    }

    public LiveData<List<Belief>> getBeliefsForEpisode(int episodeId) {
        return this.beliefDao.getBeliefsForEpisode(episodeId);
    }

    public LiveData<List<Argument>> getArgumentsForBelief(int beliefId) {
        return this.argumentDao.getArgumentsForBelief(beliefId);
    }

    public LiveData<List<Objection>> getObjectionsForBelief(int beliefId) {
        return this.objectionDao.getObjectionsForBelief(beliefId);
    }

    public LiveData<List<ThinkingStyle>> getThinkingStylesForBelief(int beliefId) {
        return this.beliefThinkingStyleDao.getThinkingStylesForBelief(beliefId);
    }

    ///////////////
    // CREATE OPERATIONS //
    ///////////////

    public void createEpisode(@NonNull String newEpisodeName, @NonNull String newEpisodeDate, @NonNull String newEpisodePeriod) {

        Date episodeDate;

        try {
            episodeDate = newEpisodeDate.isEmpty() ? Calendar.getInstance().getTime() : DateFormat.getDateInstance(DateFormat.SHORT).parse(newEpisodeDate);
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

    public void createArgument(final int beliefId, @NonNull final String newArgument, OnInsertedEntityListener onInsertedEntityListener) {
        Argument a = new Argument(
                newArgument,
                beliefId
        );
        new insertArgumentAsyncTask(this.argumentDao, onInsertedEntityListener).execute(a);
    }

    public void editArgument(@NonNull final Argument argument, OnUpdatedEntityListener onUpdatedEntityListener) {
        new saveArgumentAsyncTask(this.argumentDao, onUpdatedEntityListener).execute(argument);
    }

    public void deleteArgument(@NonNull final Argument argument, OnDeletedEntityListener onDeletedEntityListener) {
        new deleteArgumentAsyncTask(this.argumentDao, onDeletedEntityListener).execute(argument);
    }

    public void createObjection(final int beliefId, @NonNull final String newObjection, OnInsertedEntityListener onInsertedEntityListener) {
        Objection o = new Objection(
                newObjection,
                beliefId
        );
        new insertObjectionAsyncTask(this.objectionDao, onInsertedEntityListener).execute(o);
    }

    public void editObjection(@NonNull final Objection objection, OnUpdatedEntityListener onUpdatedEntityListener) {
        new saveObjectionAsyncTask(this.objectionDao, onUpdatedEntityListener).execute(objection);
    }

    public void deleteObjection(@NonNull final Objection objection, OnDeletedEntityListener onDeletedEntityListener) {
        new deleteObjectionAsyncTask(this.objectionDao, onDeletedEntityListener).execute(objection);
    }

    public void createReaction(final int episodeId, @NonNull String newReaction, @NonNull String newReactionClass, OnInsertedEntityListener onInsertedEntityListener) {
        Reaction r = new Reaction(
                newReaction,
                newReactionClass,
                episodeId
        );
        new insertReactionAsyncTask(this.reactionDao, onInsertedEntityListener).execute(r);
    }

    public void editReaction(@NonNull final Reaction reaction, OnUpdatedEntityListener onUpdatedEntityListener) {
        new saveReactionAsyncTask(this.reactionDao, onUpdatedEntityListener).execute(reaction);
    }

    public void deleteReaction(@NonNull final Reaction reaction, OnDeletedEntityListener onDeletedEntityListener) {
        new deleteReactionAsyncTask(this.reactionDao, onDeletedEntityListener).execute(reaction);
    }

    public void createBelief(final int episodeId, @NonNull final String newBelief, OnInsertedEntityListener onInsertedEntityListener) {
        Belief b = new Belief(newBelief, episodeId);
        new insertBeliefAsyncTask(this.beliefDao, onInsertedEntityListener).execute(b);
    }

    public void saveEpisode(@NonNull final Episode newEpisode) {
        new saveEpisodeAsyncTask(this.episodeDao).execute(newEpisode);
    }

    public void saveBelief(@NonNull final Belief newBelief, @NonNull final List<ThinkingStyle> toDeleteSelectedThinkingStyles, @NonNull final List<ThinkingStyle> toInsertSelectedThinkingStyles, OnUpdatedEntityListener onUpdatedEntityListener) {
        new saveBeliefAsyncTask(
                this.beliefDao,
                this.beliefThinkingStyleDao,
                toDeleteSelectedThinkingStyles,
                toInsertSelectedThinkingStyles,
                onUpdatedEntityListener
        ).execute(newBelief);
    }

    public void deleteEpisode(@NonNull final Episode episode) {
        new deleteEpisodeAsyncTask(this.episodeDao).execute(episode);
    }

    public void deleteBelief(@NonNull final Belief belief, OnDeletedEntityListener onDeletedEntityListener) {
        new deleteBeliefAsyncTask(this.beliefDao, onDeletedEntityListener).execute(belief);
    }
}
