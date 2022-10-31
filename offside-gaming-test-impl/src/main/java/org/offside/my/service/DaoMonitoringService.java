package org.offside.my.service;

import org.offside.my.model.UserMetric;

import java.util.List;
import java.util.Optional;

public interface DaoMonitoringService {

    Optional<UserMetric> findLastUserMetrics(String userId);

    void addNewMetric(UserMetric userMetric);

    List<UserMetric> getAllUserMetrics(String id);
}
