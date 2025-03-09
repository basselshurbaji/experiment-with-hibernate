package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.Getter;

abstract public class AbstractRepository<T> {

    @Getter(value = AccessLevel.PROTECTED)
    private final EntityManager em;

    @Getter(value = AccessLevel.PROTECTED)
    private final Class<T> entityClass;

    @Getter(value = AccessLevel.PROTECTED)
    private final CriteriaQuery<T> cq;

    @Getter(value = AccessLevel.PROTECTED)
    private final Root<T> root;

    protected AbstractRepository(Class<T> entityClass, EntityManager em) {
        this.entityClass = entityClass;
        this.em = em;
        this.cq = buildCriteriaQuery();
        this.root = buildRoot();
    }

    protected CriteriaBuilder getCb() {
        return em.getCriteriaBuilder();
    }

    protected String[] getDefaultFetches() {
        return new String[]{};
    }

    private CriteriaQuery<T> buildCriteriaQuery() {
        CriteriaQuery<T> criteriaQuery = getCb().createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        for (String fetch : getDefaultFetches()) {
            root.fetch(fetch);
        }
        return criteriaQuery;
    }

    @SuppressWarnings("unchecked")
    private Root<T> buildRoot() {
        return (Root<T>) cq.getRoots()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Root not found in AbstractRepository" + "<" + entityClass.getSimpleName() + ">"));
    }
}
