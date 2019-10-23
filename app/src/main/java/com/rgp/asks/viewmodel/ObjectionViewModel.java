package com.rgp.asks.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.rgp.asks.interfaces.OnDeletedEntityListener;
import com.rgp.asks.interfaces.OnUpdatedEntityListener;
import com.rgp.asks.persistence.entity.Objection;
import com.rgp.asks.persistence.repositories.ObjectionRepository;

public class ObjectionViewModel extends EntityViewModel<Objection> {
    private ObjectionRepository objectionRepository;

    public ObjectionViewModel(Application application) {
        super(application);
        this.objectionRepository = new ObjectionRepository(application);
    }

    public LiveData<Objection> getObjection() {
        return super.getEntityById(this.objectionRepository);
    }

    public void updateObjection(boolean finishSignal, OnUpdatedEntityListener onUpdatedEntityListener) {
        super.updateEntity(this.objectionRepository, finishSignal, onUpdatedEntityListener);
    }

    public void deleteObjection(OnDeletedEntityListener onDeletedEntityListener) {
        super.deleteEntity(this.objectionRepository, onDeletedEntityListener);
    }
}