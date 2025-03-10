package org.example;

import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.Getter;

abstract public class AbstractRepository<T> {

    @Getter(value = AccessLevel.PROTECTED)
    private final EntityManager em;

    protected AbstractRepository(EntityManager em) {
        this.em = em;
    }
}
