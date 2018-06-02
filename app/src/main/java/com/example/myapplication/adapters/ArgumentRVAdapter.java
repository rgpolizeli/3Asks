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
import com.example.myapplication.persistence.entity.Argument;

import java.util.ArrayList;
import java.util.List;

public class ArgumentRVAdapter extends RecyclerView.Adapter<ArgumentRVAdapter.ViewHolder> {

    List<Argument> arguments;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public EditText mEditText;
        public ImageButton mDeleteButton;
        public MyTextWatcher mTextWatcher;

        public ViewHolder(View v) {
            super(v);
            mEditText = (EditText) v.findViewById(R.id.argumentEditText);
            mDeleteButton = (ImageButton) v.findViewById(R.id.argumentDeleteButton);
            mTextWatcher = new MyTextWatcher(this);

            mEditText.addTextChangedListener(mTextWatcher);

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    try {
                        arguments.remove(position);
                        notifyItemRemoved(position);
                    }catch (ArrayIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    public ArgumentRVAdapter() {
    }

    public Argument getItem(int position) {
        if (this.arguments == null){
            return null;
        } else{
            return arguments.get(position);
        }
    }


    @Override
    public ArgumentRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.argument_item_recycler_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (holder.mEditText.hasFocus()){
            holder.mEditText.clearFocus();
        }

        holder.mEditText.removeTextChangedListener(holder.mTextWatcher);
        holder.mEditText.setText(arguments.get(position).getArgument());
        holder.mEditText.addTextChangedListener(holder.mTextWatcher);

        if(arguments.get(position).getArgument().isEmpty()) {
            holder.mEditText.requestFocus();
        }
    }


    @Override
    public int getItemCount() {
        if (this.arguments == null)
            return 0;
        else
            return arguments.size();
    }


    private class MyTextWatcher implements TextWatcher {

        private ViewHolder vh;

        public MyTextWatcher(ViewHolder viewHolder){
            vh = viewHolder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //arguments.set(this.vh.getAdapterPosition(), s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
