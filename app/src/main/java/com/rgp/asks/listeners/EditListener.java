package com.rgp.asks.listeners;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.rgp.asks.messages.OpenEditBeliefActivityEvent;
import com.rgp.asks.persistence.entity.Belief;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class EditListener implements View.OnClickListener {

    private List<Belief> beliefs;

    public EditListener() {
    }

    @Override
    public void onClick(View v) {
        if (beliefs != null) {
            startEditBeliefActivity(v);
        }
    }

    private void startEditBeliefActivity(View v) {
        RecyclerView recyclerView = (RecyclerView) v.getParent();
        int position = recyclerView.getChildAdapterPosition(v);
        EventBus.getDefault().post(new OpenEditBeliefActivityEvent(position));
    }

    public void setBeliefs(List<Belief> beliefs) {
        this.beliefs = beliefs;
    }
}
