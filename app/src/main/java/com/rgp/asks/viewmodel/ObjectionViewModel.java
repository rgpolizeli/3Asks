package com.rgp.asks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rgp.asks.interfaces.OnDeletedEntityListener;
import com.rgp.asks.interfaces.OnUpdatedEntityListener;
import com.rgp.asks.persistence.Repository;
import com.rgp.asks.persistence.entity.Objection;

public class ObjectionViewModel extends AndroidViewModel {
    private int id;
    private LiveData<Objection> entityLiveData;
    private boolean isInFirstLoad;
    private Objection modifiableCopy;
    private Repository repository;

    public ObjectionViewModel(Application application) {
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

    public LiveData<Objection> getEntityLiveData() {
        if (this.entityLiveData == null) {
            loadReactionById();
        }
        return this.entityLiveData;
    }

    private void loadReactionById() {
        this.entityLiveData = this.repository.getObjectionById(getId());
    }

    public void delete(OnDeletedEntityListener onDeletedEntityListener) {
        Objection objection = getEntityFromLiveData();
        if (objection != null) {
            this.repository.deleteObjection(objection, onDeletedEntityListener);
        } else {
            //todo: err
        }
    }

    public void update(OnUpdatedEntityListener onUpdatedEntityListener) {
        this.repository.editObjection(getModifiableCopy(), onUpdatedEntityListener);
    }

    public boolean wasChanged() {

        Objection currentEntity = getEntityFromLiveData();
        Objection modifiedEntity = getModifiableCopy();

        if (currentEntity != null && modifiedEntity != null && currentEntity.getId() == modifiedEntity.getId()) {
            return !currentEntity.equals(modifiedEntity);
        } else {
            //err
            return false;
        }
    }

    @Nullable
    public Objection getModifiableCopy() {
        return modifiableCopy;
    }

    public void initModifiableCopy(@NonNull Objection loadedEntity) {
        if (loadedEntity.getId() == this.id) {
            this.modifiableCopy = new Objection(
                    loadedEntity.getObjection(),
                    loadedEntity.getBeliefId()
            );
            this.modifiableCopy.setId(loadedEntity.getId());
        } else {
            //todo: err
        }
    }

    @Nullable
    private Objection getEntityFromLiveData() {
        return this.entityLiveData.getValue();
    }

}
