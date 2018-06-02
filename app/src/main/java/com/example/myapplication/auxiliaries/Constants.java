package com.example.myapplication.auxiliaries;

public class Constants {

    ///////////
    // INTENTS
    //////////

    public static final int REQUEST_NEW_REACTION = 1;
    public static final int REQUEST_EDIT_REACTION = 2;
    public static final int REQUEST_NEW_BELIEF = 3;
    public static final int REQUEST_EDIT_BELIEF = 4;

    public static final int RESULT_NEW_REACTION = 100;
    public static final int RESULT_REACTION_EDIT = 101;
    public static final int RESULT_REACTION_DELETE = 102;
    public static final int RESULT_NEW_BELIEF = 103;
    public static final int RESULT_BELIEF_EDIT = 104;
    public static final int RESULT_BELIEF_DELETE = 105;


    ///////////
    // BUNDLES
    ///////////


    public static final String ARG_NEW_BELIEF = "new_belief";
    public static final String ARG_NEW_REACTION = "new_reaction";
    public static final String ARG_BELIEF = "belief";
    public static final String ARG_REACTION = "reaction";
    public static final String ARG_REACTION_POSITION = "reaction_position";
    public static final String ARG_EPISODE = "episode";
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

    public static final String JSON_REACTION = "reaction_name";
    public static final String JSON_REACTION_CLASS = "reactionClass";

    ////////////
    // EPISODE
    ////////////
    
    public static final String JSON_EPISODE = "episode_name";
    public static final String JSON_EPISODE_DESC = "description";
    public static final String JSON_EPISODE_DATE = "date";
    public static final String JSON_EPISODE_PERIOD = "period";
    public static final String JSON_EPISODE_REACTIONS = "reactions";
    public static final String JSON_EPISODE_BELIEFS = "beliefs";

}
