package com.rgp.asks.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rgp.asks.persistence.entity.Objection;

import java.util.List;

public class ObjectionRVAdapter extends RecyclerView.Adapter<ObjectionRVAdapter.ViewHolder> {
    private List<Objection> objections;
    private View.OnClickListener onItemClickListener;

    public ObjectionRVAdapter(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ObjectionRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                            int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(com.rgp.asks.R.layout.item_recycler_view_objection, parent, false);
        v.setOnClickListener(this.onItemClickListener);
        return new ViewHolder(v);
    }

    public void setObjections(List<Objection> objections) {
        this.objections = objections;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ObjectionRVAdapter.ViewHolder holder, int position) {
        Objection objection = this.objections.get(position);
        holder.objectionTextView.setText(objection.getObjection());
    }

    public Objection getItem(int position) {
        if (this.objections == null) {
            return null;
        } else {
            return objections.get(position);
        }
    }

    @Override
    public int getItemCount() {
        if (this.objections == null)
            return 0;
        else
            return objections.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView objectionTextView;

        public ViewHolder(View v) {
            super(v);
            objectionTextView = v.findViewById(com.rgp.asks.R.id.objectionTextView);
        }
    }


}