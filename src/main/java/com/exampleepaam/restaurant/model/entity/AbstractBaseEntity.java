package com.exampleepaam.restaurant.model.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Basic class for every model entity.
 * Equals and Hashcode compare ids.
 */
public abstract class AbstractBaseEntity implements Serializable {
    protected long id;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractBaseEntity that = (AbstractBaseEntity) o;
        if (id == 0) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        if (id != 0) {
            return Objects.hash(id);
        } else {
            return super.hashCode();
        }
    }

     AbstractBaseEntity(long id) {
        this.id = id;
    }

     AbstractBaseEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
