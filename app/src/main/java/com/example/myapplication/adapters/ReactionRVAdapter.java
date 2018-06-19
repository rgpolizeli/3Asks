package com.example.myapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.listeners.EditListener;
import com.example.myapplication.persistence.entity.Reaction;

import java.util.List;

public class ReactionRVAdapter extends RecyclerView.Adapter<ReactionRVAdapter.ViewHolder> {
    private List<Reaction> reactions;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView reactionTextView;
        public TextView reactionClassTextView;

        public ViewHolder(View v) {
            super(v);
            reactionTextView = v.findViewById(R.id.reaction_text_view);
            reactionClassTextView = v.findViewById(R.id.reaction_class_text_view);
        }
    }


    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
        notifyDataSetChanged();
    }

    public ReactionRVAdapter() {
    }

    @Override
    public ReactionRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view_reaction, parent, false);

        setItemListeners(v);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reaction reaction = this.reactions.get(position);
        holder.reactionTextView.setText(reaction.getReaction());
        holder.reactionClassTextView.setText(reaction.getReactionCategory());
    }

    @Override
    public int getItemCount() {
        if (this.reactions == null)
            return 0;
        else
            return reactions.size();
    }

    public Reaction getItem(int position) {
        if (this.reactions == null){
            return null;
        } else{
            return reactions.get(position);
        }
    }

    private void setItemListeners(View v){
        EditListener editListener = new EditListener();
        editListener.setReactions(this.reactions);
        v.setOnClickListener(editListener);
    }
}
