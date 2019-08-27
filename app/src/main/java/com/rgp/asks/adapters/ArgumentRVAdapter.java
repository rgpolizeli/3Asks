package com.rgp.asks.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rgp.asks.persistence.entity.Argument;

import java.util.List;

public class ArgumentRVAdapter extends RecyclerView.Adapter<ArgumentRVAdapter.ViewHolder> {

    private List<Argument> arguments;
    private View.OnClickListener onItemClickListener;

    public ArgumentRVAdapter(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ArgumentRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(com.rgp.asks.R.layout.item_recycler_view_argument, parent, false);
        v.setOnClickListener(this.onItemClickListener);
        return new ViewHolder(v);
    }

    public void setArguments(List<Argument> arguments) {
        this.arguments = arguments;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Argument argument = this.arguments.get(position);
        holder.argumentTextView.setText(argument.getArgument());
    }

    @Override
    public int getItemCount() {
        if (this.arguments == null)
            return 0;
        else
            return arguments.size();
    }

    public Argument getItem(int position) {
        if (this.arguments == null) {
            return null;
        } else {
            return arguments.get(position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView argumentTextView;

        ViewHolder(View v) {
            super(v);
            argumentTextView = v.findViewById(com.rgp.asks.R.id.argumentTextView);
        }
    }
}
