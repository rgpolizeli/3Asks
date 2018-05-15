package com.example.myapplication.auxiliaries;

public class Constants {

    ///////////
    // INTENTS
    //////////

    public static final int REQUEST_NEW_REACTION = 1;
    public static final int REQUEST_NEW_BELIEF = 2;

    public static final int RESULT_NEW_REACTION_ADD = 100;
    public static final int RESULT_NEW_REACTION_DELETE = 101;
    public static final int RESULT_NEW_BELIEF_ADD = 102;
    public static final int RESULT_NEW_BELIEF_DELETE = 103;


    ///////////
    // BUNDLES
    ///////////


    public static final String ARG_NEW_BELIEF = "new_belief";
    public static final String ARG_NEW_REACTION = "new_reaction";
    public static final String ARG_BELIEF = "belief";
    public static final String ARG_REACTION = "reaction";
    public static final String ARG_SECTION_NUMBER = "section_number";


    ////////////
    // BELIEF //
    ////////////

    public static final String JSON_BELIEF_THOUGHT = "thought";
    public static final String JSON_BELIEF_UNHELPFUL_THINKING_STYLES = "unhelpfulThinkingStyles";
    public static final String JSON_BELIEF_ARGUMENTS = "arguments";
    public static final String JSON_BELIEF_OBJECTIONS = "objections";

    ////////////
    // REACTION
    ////////////

    public static final String JSON_REACTION = "reaction";
    public static final String JSON_REACTION_CLASS = "reactionClass";

    ////////////
    // EPISODE
    ////////////
    
    public static final String JSON_EPISODE = "episode";
    public static final String JSON_EPISODE_DESC = "description";
    public static final String JSON_EPISODE_DATE = "date";
    public static final String JSON_EPISODE_PERIOD = "period";
    public static final String JSON_EPISODE_REACTIONS = "reactions";
    public static final String JSON_EPISODE_BELIEFS = "beliefs";

}
