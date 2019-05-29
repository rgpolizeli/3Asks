package com.rpolizeli.asks.listeners;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class OnItemClickListener implements View.OnClickListener {

    private MutableLiveData<View> clickedView;

    public OnItemClickListener() {
    }

    public LiveData<View> getClickedView(){
        if(clickedView == null){
            clickedView = new MutableLiveData<>();
        }
        return clickedView;
    }

    @Override
    public void onClick(View v) {
        clickedView.setValue(v);
    }
}
