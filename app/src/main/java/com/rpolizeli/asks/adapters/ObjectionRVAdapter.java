package com.rpolizeli.asks.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rpolizeli.asks.listeners.EditListener;
import com.rpolizeli.asks.persistence.entity.Objection;

import java.util.List;

public class ObjectionRVAdapter extends RecyclerView.Adapter<ObjectionRVAdapter.ViewHolder>{
    private List<Objection> objections;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView objectionTextView;

        public ViewHolder(View v) {
            super(v);
            objectionTextView = v.findViewById(com.rpolizeli.asks.R.id.objectionTextView);
        }
    }

    public void setObjections(List<Objection> objections) {
        this.objections = objections;
        notifyDataSetChanged();
    }

    public ObjectionRVAdapter() {
    }

    @Override
    public ObjectionRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(com.rpolizeli.asks.R.layout.item_recycler_view_objection, parent, false);
        setItemListeners(v);
        ObjectionRVAdapter.ViewHolder vh = new ObjectionRVAdapter.ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ObjectionRVAdapter.ViewHolder holder, int position) {

        Objection objection = this.objections.get(position);
        holder.objectionTextView.setText(objection.getObjection());
    }

    public Objection getItem(int position) {
        if (this.objections == null){
            return null;
        } else{
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

    private void setItemListeners(View v){
        EditListener editListener = new EditListener();
        editListener.setObjections(this.objections);
        v.setOnClickListener(editListener);
    }


}