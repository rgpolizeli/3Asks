package com.example.myapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myapplication.R;
import com.example.myapplication.persistence.entity.Objection;

import java.util.ArrayList;
import java.util.List;

public class ObjectionRVAdapter extends RecyclerView.Adapter<ObjectionRVAdapter.ViewHolder>{
    List<Objection> objections;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public EditText mEditText;
        public ImageButton mDeleteButton;
        public MyTextWatcher mTextWatcher;

        public ViewHolder(View v) {
            super(v);
            mEditText = (EditText) v.findViewById(R.id.objectionEditView);
            mDeleteButton = (ImageButton) v.findViewById(R.id.objectionDeleteButton);
            mTextWatcher = new MyTextWatcher(this);

            mEditText.addTextChangedListener(mTextWatcher);

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    try {
                        objections.remove(position);
                        notifyItemRemoved(position);
                    }catch (ArrayIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }
            });

        }
    }


    public ObjectionRVAdapter() {
    }


    public Objection getItem(int position) {
        if (this.objections == null){
            return null;
        } else{
            return objections.get(position);
        }
    }


    @Override
    public ObjectionRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.objection_item_recycler_view, parent, false);
        ObjectionRVAdapter.ViewHolder vh = new ObjectionRVAdapter.ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ObjectionRVAdapter.ViewHolder holder, int position) {

        if (holder.mEditText.hasFocus()){
            holder.mEditText.clearFocus();
        }

        holder.mEditText.removeTextChangedListener(holder.mTextWatcher);
        holder.mEditText.setText(objections.get(position).getObjection());
        holder.mEditText.addTextChangedListener(holder.mTextWatcher);

        if(objections.get(position).getObjection().isEmpty()) {
            holder.mEditText.requestFocus();
        }
    }


    @Override
    public int getItemCount() {
        return objections.size();
    }


    private class MyTextWatcher implements TextWatcher {

        private ObjectionRVAdapter.ViewHolder vh;

        public MyTextWatcher(ObjectionRVAdapter.ViewHolder viewHolder){
            vh = viewHolder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //objections.set(this.vh.getAdapterPosition(), s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}