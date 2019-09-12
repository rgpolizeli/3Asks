package com.rgp.asks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rgp.asks.interfaces.OnDeletedEntityListener;
import com.rgp.asks.interfaces.OnUpdatedEntityListener;
import com.rgp.asks.persistence.Repository;
import com.rgp.asks.persistence.entity.Argument;

public class ArgumentViewModel extends AndroidViewModel {
    private int id;
    private LiveData<Argument> entityLiveData;
    private boolean isInFirstLoad;
    private Argument modifiableCopy;
    private Repository repository;

    public ArgumentViewModel(Application application) {
        super(application);
        this.repository = new Repository(application);
        this.isInFirstLoad = true;
    }

    public boolean isInFirstLoad() {
        return isInFirstLoad;
    }

    public void setIsInFirstLoad(boolean is) {
        this.isInFirstLoad = is;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LiveData<Argument> getEntityLiveData() {
        if (this.entityLiveData == null) {
            loadReactionById();
        }
        return this.entityLiveData;
    }

    private void loadReactionById() {
        this.entityLiveData = this.repository.getArgumentById(getId());
    }

    public void delete(OnDeletedEntityListener onDeletedEntityListener) {
        Argument argument = getEntityFromLiveData();
        if (argument != null) {
            this.repository.deleteArgument(argument, onDeletedEntityListener);
        } else {
            //todo: err
        }
    }

    public void update(OnUpdatedEntityListener onUpdatedEntityListener) {
        this.repository.editArgument(getModifiableCopy(), onUpdatedEntityListener);
    }

    public boolean wasChanged() {

        Argument currentEntity = getEntityFromLiveData();
        Argument modifiedEntity = getModifiableCopy();

        if (currentEntity != null && modifiedEntity != null && currentEntity.getId() == modifiedEntity.getId()) {
            return !currentEntity.equals(modifiedEntity);
        } else {
            //err
            return false;
        }
    }

    @Nullable
    public Argument getModifiableCopy() {
        return modifiableCopy;
    }

    public void initModifiableCopy(@NonNull Argument loadedEntity) {
        if (loadedEntity.getId() == this.id) {
            this.modifiableCopy = new Argument(
                    loadedEntity.getArgument(),
                    loadedEntity.getBeliefId()
            );
            this.modifiableCopy.setId(loadedEntity.getId());
        } else {
            //todo: err
        }
    }

    @Nullable
    private Argument getEntityFromLiveData() {
        return this.entityLiveData.getValue();
    }

}
