package org.offside.my.repository;

import org.offside.my.model.UserMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMetricsRepository extends JpaRepository<UserMetric, Long>, UserMetricsCustomRepository {

    List<UserMetric> findAllByUserIdOrderByDateOfMetric(String userId);
}
