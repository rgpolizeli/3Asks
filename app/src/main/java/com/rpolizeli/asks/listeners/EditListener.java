package com.rpolizeli.asks.listeners;

import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.rpolizeli.asks.activities.AsksActivity;
import com.rpolizeli.asks.adapters.EpisodesRVAdapter;
import com.rpolizeli.asks.auxiliaries.Constants;
import com.rpolizeli.asks.messages.OpenEditArgumentDialogEvent;
import com.rpolizeli.asks.messages.OpenEditBeliefActivityEvent;
import com.rpolizeli.asks.messages.OpenEditObjectionDialogEvent;
import com.rpolizeli.asks.messages.OpenEditReactionDialogEvent;
import com.rpolizeli.asks.persistence.entity.Argument;
import com.rpolizeli.asks.persistence.entity.Belief;
import com.rpolizeli.asks.persistence.entity.Episode;
import com.rpolizeli.asks.persistence.entity.Objection;
import com.rpolizeli.asks.persistence.entity.Reaction;

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
        EventBus.getDefault().post(new OpenEditBeliefActivityEvent(position));
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
