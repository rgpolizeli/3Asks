package com.example.myapplication.listeners;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.myapplication.activities.AddNewBeliefActivity;
import com.example.myapplication.activities.AsksActivity;
import com.example.myapplication.adapters.BeliefRVAdapter;
import com.example.myapplication.adapters.EpisodesRVAdapter;
import com.example.myapplication.auxiliaries.Constants;
import com.example.myapplication.messages.OpenEditArgumentDialogEvent;
import com.example.myapplication.messages.OpenEditObjectionDialogEvent;
import com.example.myapplication.messages.OpenEditReactionDialogEvent;
import com.example.myapplication.persistence.entity.Argument;
import com.example.myapplication.persistence.entity.Belief;
import com.example.myapplication.persistence.entity.Episode;
import com.example.myapplication.persistence.entity.Objection;
import com.example.myapplication.persistence.entity.Reaction;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class EditListener implements View.OnClickListener{

    private List<Reaction> reactions;
    private List<Belief> beliefs;
    private List<Episode> episodes;
    private List<Argument> arguments;
    private List<Objection> objections;

    @Override
    public void onClick(View v) {

        if (reactions != null){
            startEditReactionActivity(v);
        } else{
            if (beliefs != null){
                startEditBeliefActivity(v);
            } else{
                if(episodes != null){
                    startEditEpisodeActivity(v);
                } else{
                    if(arguments != null){
                        startEditArgumentActivity(v);
                    } else{
                        if(objections != null){
                            startEditObjectionActivity(v);
                        }
                    }
                }
            }
        }
    }

    private void startEditReactionActivity(View v){
        RecyclerView recyclerView = (RecyclerView) v.getParent();
        int position = recyclerView.getChildAdapterPosition(v);
        EventBus.getDefault().post(new OpenEditReactionDialogEvent(position));
    }

    private void startEditBeliefActivity(View v){
        RecyclerView recyclerView = (RecyclerView) v.getParent();
        int position = recyclerView.getChildAdapterPosition(v);
        Belief belief = ((BeliefRVAdapter)recyclerView.getAdapter()).getItem(position);

        if (belief != null){
            Intent intent = new Intent(v.getContext(), AddNewBeliefActivity.class);
            intent.putExtra(Constants.ARG_BELIEF, belief.getId());
            v.getContext().startActivity(intent);
        }else{
            //err
        }
    }

    private void startEditEpisodeActivity(View v){

        RecyclerView recyclerView = (RecyclerView) v.getParent();
        int position = recyclerView.getChildAdapterPosition(v);
        Episode episode = ((EpisodesRVAdapter)recyclerView.getAdapter()).getItem(position);

        if (episode != null){
            Intent intent = new Intent(v.getContext(), AsksActivity.class);
            intent.putExtra(Constants.ARG_EPISODE, episode.getId());
            v.getContext().startActivity(intent);
        }

    }

    private void startEditArgumentActivity(View v){
        RecyclerView recyclerView = (RecyclerView) v.getParent();
        int position = recyclerView.getChildAdapterPosition(v);
        EventBus.getDefault().post(new OpenEditArgumentDialogEvent(position));
    }

    private void startEditObjectionActivity(View v){
        RecyclerView recyclerView = (RecyclerView) v.getParent();
        int position = recyclerView.getChildAdapterPosition(v);
        EventBus.getDefault().post(new OpenEditObjectionDialogEvent(position));
    }


    public EditListener(){
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }

    public void setBeliefs(List<Belief> beliefs) {
        this.beliefs = beliefs;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    public void setArguments(List<Argument> arguments) {
        this.arguments = arguments;
    }

    public void setObjections(List<Objection> objections) {
        this.objections = objections;
    }

}
