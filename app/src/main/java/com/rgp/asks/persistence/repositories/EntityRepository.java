package com.rgp.asks.persistence.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.rgp.asks.interfaces.OnDeletedEntityListener;
import com.rgp.asks.interfaces.OnInsertedEntityListener;
import com.rgp.asks.interfaces.OnUpdatedEntityListener;
import com.rgp.asks.persistence.asynctask.DeleteAsyncTask;
import com.rgp.asks.persistence.asynctask.InsertAsyncTask;
import com.rgp.asks.persistence.asynctask.UpdateAsyncTask;
import com.rgp.asks.persistence.dao.EntityDao;

public abstract class EntityRepository<T> {

    private EntityDao<T> dao;

    EntityRepository(@NonNull EntityDao<T> dao) {
        this.dao = dao;
    }

    EntityDao<T> getDao() {
        return this.dao;
    }

    public void insertEntity(T entity, OnInsertedEntityListener onInsertedEntityListener) {
        new InsertAsyncTask<>(this.dao, onInsertedEntityListener).execute(entity);
    }

    public void updateEntity(T updatedEntity, boolean finishSignal, OnUpdatedEntityListener onUpdatedEntityListener) {
        new UpdateAsyncTask<>(this.dao, finishSignal, onUpdatedEntityListener).execute(updatedEntity);
    }

    public void deleteEntity(T entity, OnDeletedEntityListener onDeletedEntityListener) {
        new DeleteAsyncTask<>(this.dao, onDeletedEntityListener).execute(entity);
    }

    public abstract LiveData<T> getEntityById(int entityId);
}
