package com.rgp.asks.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rgp.asks.interfaces.OnDeletedEntityListener;
import com.rgp.asks.interfaces.OnUpdatedEntityListener;
import com.rgp.asks.persistence.repositories.EntityRepository;

abstract class EntityViewModel<T> extends AndroidViewModel {
    private int entityId;
    private LiveData<T> entity;
    private boolean isEntityInFirstLoad;
    private T modifiableEntityCopy;

    EntityViewModel(@NonNull Application application) {
        super(application);
        this.isEntityInFirstLoad = true;
    }

    public boolean isEntityInFirstLoad() {
        return isEntityInFirstLoad;
    }

    public void setIsEntityInFirstLoad(boolean is) {
        this.isEntityInFirstLoad = is;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    LiveData<T> getEntityById(EntityRepository<T> entityRepository) {
        if (this.entity == null) {
            this.entity = entityRepository.getEntityById(this.entityId);
        }
        return this.entity;
    }

    void updateEntity(EntityRepository<T> entityRepository, boolean finishSignal, OnUpdatedEntityListener onUpdatedEntityListener) {
        T newEntity = getModifiableEntityCopy();
        if (newEntity != null) {
            entityRepository.updateEntity(newEntity, finishSignal, onUpdatedEntityListener);
        } else {
            //todo: err
        }
    }

    public boolean entityWasChanged() {
        T currentEntity = getEntityFromLiveData();
        T modifiedEntity = getModifiableEntityCopy();

        if (currentEntity != null && modifiedEntity != null) {
            return !currentEntity.equals(modifiedEntity);
        } else {
            //err
            return false;
        }
    }

    void deleteEntity(EntityRepository<T> entityRepository, OnDeletedEntityListener onDeletedEntityListener) {
        T entity = getEntityFromLiveData();
        if (entity != null) {
            entityRepository.deleteEntity(entity, onDeletedEntityListener);
        } else {
            //todo: err
        }
    }

    @Nullable
    public T getModifiableEntityCopy() {
        return this.modifiableEntityCopy;
    }

    public void setModifiableEntityCopy(@NonNull T loadedEntityCopy) {
        this.modifiableEntityCopy = loadedEntityCopy;
        //todo: create a copy method in Episode Class and rename this to setModifiableEntityCopy
    }

    @Nullable
    private T getEntityFromLiveData() {
        return this.entity.getValue();
    }


}
