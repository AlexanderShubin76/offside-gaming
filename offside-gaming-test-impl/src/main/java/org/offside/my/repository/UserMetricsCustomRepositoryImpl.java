package org.offside.my.repository;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import org.offside.my.model.UserMetric;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class UserMetricsCustomRepositoryImpl implements UserMetricsCustomRepository {

    private final EntityManager entityManager;

    public UserMetricsCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<UserMetric> findLatestUserMetricByUserId(String userId) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<UserMetric> criteriaQuery = criteriaBuilder.createQuery(UserMetric.class);
        final Root<UserMetric> userMetricRoot = criteriaQuery.from(UserMetric.class);

        final Predicate userIdPredicate = criteriaBuilder.equal(userMetricRoot.get("userId"), userId);
        final Order order = criteriaBuilder.desc(userMetricRoot.get("dateOfMetric"));

        final CriteriaQuery<UserMetric> query = criteriaQuery.where(userIdPredicate)
                .orderBy(order);
        final List<UserMetric> resultList = entityManager.createQuery(query)
                .setMaxResults(1)
                .getResultList();
        return resultList.isEmpty() ? empty() : ofNullable(resultList.get(0));
    }
}
