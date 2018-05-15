package com.example.myapplication.models;

import com.example.myapplication.auxiliaries.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Belief {
    private String thought;
    private ArrayList<String> selectedThinkingStylesList;
    private ArrayList<String> arguments;
    private ArrayList<String> objections;

    public Belief() {
        thought = "";
        selectedThinkingStylesList = new ArrayList<>();
        arguments = new ArrayList<>();
        objections = new ArrayList<>();
    }

    private ArrayList<String> parseJSONArrayToList(Object jsonObject) throws JSONException {
        ArrayList<String> listdata = new ArrayList<String>();
        JSONArray jArray = (JSONArray)jsonObject;
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                listdata.add(jArray.getString(i));
            }
        }
        return listdata;
    }


    public Belief parseJSONToBelief(JSONObject beliefJSON){
        Belief belief = new Belief();
        try {
            belief.loadBelief(
                    beliefJSON.getString(Constants.JSON_BELIEF_THOUGHT),
                    parseJSONArrayToList(beliefJSON.get(Constants.JSON_BELIEF_UNHELPFUL_THINKING_STYLES)),
                    parseJSONArrayToList(beliefJSON.get(Constants.JSON_BELIEF_ARGUMENTS)),
                    parseJSONArrayToList(beliefJSON.get(Constants.JSON_BELIEF_OBJECTIONS))
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return belief;
    }

    public void loadBelief(String beliefName, ArrayList<String> selectedThinkingStylesList, ArrayList<String> arguments, ArrayList<String> objections){
        this.thought = beliefName;
        this.selectedThinkingStylesList = selectedThinkingStylesList;
        this.arguments = arguments;
        this.objections = objections;
    }

    public String getThought(){return this.thought;}
    public ArrayList<String> getArguments(){
        return this.arguments;
    }
    public ArrayList<String> getObjections(){
        return this.objections;
    }
    public ArrayList<String> getSelectedThinkingStylesList(){
        return this.selectedThinkingStylesList;
    }

}
