package com.rgp.asks.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rgp.asks.listeners.EditListener;
import com.rgp.asks.persistence.entity.Reaction;

import java.util.List;

public class ReactionRVAdapter extends RecyclerView.Adapter<ReactionRVAdapter.ViewHolder> {
    private List<Reaction> reactions;

    public ReactionRVAdapter() {
    }

    @Override
    public ReactionRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(com.rgp.asks.R.layout.item_recycler_view_reaction, parent, false);

        setItemListeners(v);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
        notifyDataSetChanged();
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
        if (this.reactions == null) {
            return null;
        } else {
            return reactions.get(position);
        }
    }

    private void setItemListeners(View v) {
        EditListener editListener = new EditListener();
        editListener.setReactions(this.reactions);
        v.setOnClickListener(editListener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView reactionTextView;
        public TextView reactionClassTextView;

        public ViewHolder(View v) {
            super(v);
            reactionTextView = v.findViewById(com.rgp.asks.R.id.reaction_text_view);
            reactionClassTextView = v.findViewById(com.rgp.asks.R.id.reaction_class_text_view);
        }
    }
}
