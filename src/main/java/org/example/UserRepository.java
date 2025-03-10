package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.example.models.Profile;
import org.example.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository extends AbstractRepository<User> {

    protected UserRepository(EntityManager em) {
        super(em);
    }

    public List<User> findAll(Long id, String name) {

        // Get Criteria Builder
        CriteriaBuilder cb = getEm().getCriteriaBuilder();

        // Create criteria query to spec the Query
        CriteriaQuery<User> cq = cb.createQuery(User.class);

        // Define a root for the query, and add needed fetches
        Root<User> root = cq.from(User.class);
        root.fetch("hobbies");
        root.fetch("profile");

        // Define an empty dynamic predicate array
        List<Predicate> predicates = new ArrayList<>();

        // Filter if id filter was passed
        if (id != null) {
            predicates.add(cb.equal(root.get("id"), id));
        }

        // Filter based on name in profile
        if (name != null && !name.isBlank()) {
            // Create a profile join to define predicates on
            Join<User, Profile> join = root.join("profile");

            // Concat first and last name as one Expression<String>
            Expression<String> expression = cb.concat(
                    cb.concat(join.get("firstName"), " "),
                    join.get("lastName")
            );

            // Perform like on the concatenated expression
            predicates.add(cb.like(expression, "%" + name + "%"));
        }

        // `And` all predicates and add them to criteria query
        cq.where(cb.and(predicates.toArray(Predicate[]::new)));

        // Return result
        return getEm().createQuery(cq).getResultList();
    }

}
