package com.rgp.asks.persistence.dao;

import androidx.annotation.NonNull;

import com.rgp.asks.persistence.entity.Argument;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.persistence.entity.BeliefThinkingStyle;
import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.persistence.entity.Objection;
import com.rgp.asks.persistence.entity.Reaction;
import com.rgp.asks.persistence.entity.ThinkingStyle;

import java.util.Date;
import java.util.Random;

class EntitiesBuilder {

    private Random random = new Random();

    Episode buildAnyEpisode() {
        String episodeName = "My Episode " + random.nextInt();
        String episodeDescription = "My Episode Description " + random.nextInt();
        Date episodeDate = new Date();
        String episodePeriod = "Morning " + random.nextInt();
        return new Episode(
                episodeName,
                episodeDescription,
                episodeDate,
                episodePeriod
        );
    }

    Belief buildAnyBelief(int episodeId) {
        String beliefName = "Belief " + random.nextInt();
        return new Belief(beliefName, episodeId);
    }

    Reaction buildAnyReaction(int episodeId) {
        String reactionName = "Reaction " + random.nextInt();
        String reactionClass = "ReactionClass " + random.nextInt();
        return new Reaction(reactionName, reactionClass, episodeId);
    }

    Argument buildAnyArgument(int beliefId) {
        String argument = "Argument " + random.nextInt();
        return new Argument(argument, beliefId);
    }

    Objection buildAnyObjection(int beliefId) {
        String objection = "Objection " + random.nextInt();
        return new Objection(objection, beliefId);
    }

    ThinkingStyle buildAnyThinkingStyle() {
        String thinkingStyle = "Thinking Style " + random.nextInt();
        return new ThinkingStyle(thinkingStyle);
    }

    BeliefThinkingStyle buildBeliefThinkingStyle(int beliefId, @NonNull String thinkingStyle) {
        return new BeliefThinkingStyle(beliefId, thinkingStyle);
    }

}
