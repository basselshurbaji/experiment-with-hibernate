package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.example.models.Profile;
import org.example.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository extends AbstractRepository<User> {

    protected UserRepository(EntityManager em) {
        super(User.class, em);
    }

    public List<User> findAll(Long id, String name) {

        List<Predicate> predicates = new ArrayList<>();

        // Filter if id filter was passed
        if (id != null) {
            predicates.add(getCb().equal(getRoot().get("id"), id));
        }

        // Filter based on name in profile
        if (name != null && !name.isBlank()) {
            Predicate like = getNameLikePredicate(name);
            predicates.add(like);
        }

        // And all predicates and add them to criteria query
        getCq().where(getCb().and(predicates.toArray(Predicate[]::new)));

        // Return result
        return getEm().createQuery(getCq()).getResultList();
    }

    @Override
    protected String[] getDefaultFetches() {
        return new String[]{"profile", "hobbies"};
    }

    private Predicate getNameLikePredicate(String name) {
        Join<User, Profile> join = getRoot().join("profile");
        Expression<String> expression = getCb().concat(
                getCb().concat(join.get("firstName"), " "),
                join.get("lastName")
        );
        return getCb().like(expression, "%" + name + "%");
    }

}
