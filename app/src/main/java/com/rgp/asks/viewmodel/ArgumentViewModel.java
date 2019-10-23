package com.rgp.asks.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.rgp.asks.interfaces.OnDeletedEntityListener;
import com.rgp.asks.interfaces.OnUpdatedEntityListener;
import com.rgp.asks.persistence.entity.Argument;
import com.rgp.asks.persistence.repositories.ArgumentRepository;

public class ArgumentViewModel extends EntityViewModel<Argument> {
    private ArgumentRepository argumentRepository;

    public ArgumentViewModel(Application application) {
        super(application);
        this.argumentRepository = new ArgumentRepository(application);
    }

    public LiveData<Argument> getArgument() {
        return super.getEntityById(this.argumentRepository);
    }

    public void updateArgument(boolean finishSignal, OnUpdatedEntityListener onUpdatedEntityListener) {
        super.updateEntity(this.argumentRepository, finishSignal, onUpdatedEntityListener);
    }

    public void deleteArgument(OnDeletedEntityListener onDeletedEntityListener) {
        super.deleteEntity(this.argumentRepository, onDeletedEntityListener);
    }
}
