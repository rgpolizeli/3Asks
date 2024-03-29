package com.rgp.asks.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rgp.asks.interfaces.Filterable;
import com.rgp.asks.persistence.entity.Reaction;

import java.util.ArrayList;
import java.util.List;

public class ReactionRecyclerViewAdapter extends RecyclerView.Adapter<ReactionRecyclerViewAdapter.ViewHolder> implements Filterable {
    @NonNull
    private final List<Reaction> reactions;
    @NonNull
    private final List<Reaction> backupReactions;
    private View.OnClickListener onItemClickListener;
    private final String emptyName;

    public ReactionRecyclerViewAdapter(@NonNull String emptyName, View.OnClickListener onItemClickListener) {
        this.reactions = new ArrayList<>();
        this.backupReactions = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
        this.emptyName = emptyName;
    }

    @NonNull
    @Override
    public ReactionRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(com.rgp.asks.R.layout.item_recycler_view_reaction, parent, false);

        v.setOnClickListener(this.onItemClickListener);

        return new ViewHolder(v);
    }

    public void setData(@NonNull final List<Reaction> reactions) {
        this.reactions.clear();
        this.backupReactions.clear();
        this.reactions.addAll(reactions);
        this.backupReactions.addAll(reactions);
        notifyDataSetChanged();
    }

    private String getNameToDisplay(int position) {
        String name = this.reactions.get(position).getReaction();
        if (name.isEmpty()) {
            name = this.emptyName;
        }
        return name;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.reactionTextView.setText(getNameToDisplay(position));
        holder.reactionClassTextView.setText(this.reactions.get(position).getReactionCategory());
    }

    @Override
    public int getItemCount() {
        return reactions.size();
    }

    public Reaction getItem(int position) {
        return reactions.get(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchedTerm = charSequence.toString().toLowerCase();
                List<Reaction> filteredList = new ArrayList<>();
                if (searchedTerm.isEmpty()) {
                    filteredList.addAll(backupReactions);
                } else {
                    for (Reaction reaction : backupReactions) {
                        //search case-insensitive
                        if (reaction.getReaction().toLowerCase().contains(searchedTerm)) {
                            filteredList.add(reaction);
                        } else if (reaction.getReactionCategory().toLowerCase().contains(searchedTerm)) {
                            filteredList.add(reaction);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.count = filteredList.size();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                List<Reaction> filteredList = (List<Reaction>) filterResults.values;
                //this if is necessary because when the filtered results is empty, the filtered results received is null, even the filtered list maked in performFiltering is not null, only empty. See the issue #35.
                if (filteredList == null) {
                    filteredList = new ArrayList<>();
                }
                reactions.clear();
                reactions.addAll(filteredList);
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void removeAllAppliedFilter() {
        reactions.clear();
        reactions.addAll(backupReactions);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView reactionTextView;
        TextView reactionClassTextView;

        ViewHolder(View v) {
            super(v);
            reactionTextView = v.findViewById(com.rgp.asks.R.id.reaction_text_view);
            reactionClassTextView = v.findViewById(com.rgp.asks.R.id.reaction_class_text_view);
        }
    }
}
