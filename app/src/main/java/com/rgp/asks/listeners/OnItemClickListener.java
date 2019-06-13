package com.rgp.asks.listeners;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;

public class OnItemClickListener implements View.OnClickListener {

    private MutableLiveData<View> clickedView;

    @Inject
    public OnItemClickListener() {
    }

    public LiveData<View> getClickedView() {
        if (clickedView == null) {
            clickedView = new MutableLiveData<>();
        }
        return clickedView;
    }

    @Override
    public void onClick(View v) {
        clickedView.setValue(v);
    }
}
