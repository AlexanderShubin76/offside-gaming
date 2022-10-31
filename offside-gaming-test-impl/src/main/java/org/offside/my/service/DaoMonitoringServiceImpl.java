package org.offside.my.service;

import org.offside.my.model.UserMetric;
import org.offside.my.repository.UserMetricsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DaoMonitoringServiceImpl implements DaoMonitoringService {

    private final UserMetricsRepository userMetricsRepository;

    public DaoMonitoringServiceImpl(UserMetricsRepository userMetricsRepository) {
        this.userMetricsRepository = userMetricsRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserMetric> findLastUserMetrics(String userId) {
        return userMetricsRepository.findLatestUserMetricByUserId(userId);
    }

    @Override
    public void addNewMetric(UserMetric userMetric) {
        userMetricsRepository.save(userMetric);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserMetric> getAllUserMetrics(String id) {
        return userMetricsRepository.findAllByUserIdOrderByDateOfMetric(id);
    }
}
