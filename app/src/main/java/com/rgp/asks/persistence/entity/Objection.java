package com.rgp.asks.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        foreignKeys = {
                @ForeignKey(entity = Belief.class, parentColumns = "id", childColumns = "beliefId", onDelete = CASCADE)
        }
)
public class Objection {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String objection;

    private int beliefId;

    public Objection(@NonNull String objection, int beliefId) {
        this.objection = objection;
        this.beliefId = beliefId;
    }

    public Objection(int objectionId, @NonNull String objection, int beliefId) {
        this.id = objectionId;
        this.objection = objection;
        this.beliefId = beliefId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getObjection() {
        return objection;
    }

    public void setObjection(@NonNull String objection) {
        this.objection = objection;
    }

    public int getBeliefId() {
        return beliefId;
    }

    public void setBeliefId(int beliefId) {
        this.beliefId = beliefId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Objection objection1 = (Objection) o;

        if (getId() != objection1.getId()) return false;
        if (getBeliefId() != objection1.getBeliefId()) return false;
        return getObjection().equals(objection1.getObjection());
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getObjection().hashCode();
        result = 31 * result + getBeliefId();
        return result;
    }
}
