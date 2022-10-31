package org.offside.my.repository;

import org.offside.my.model.UserMetric;

import java.util.Optional;

public interface UserMetricsCustomRepository {

    Optional<UserMetric> findLatestUserMetricByUserId(String userId);
}
