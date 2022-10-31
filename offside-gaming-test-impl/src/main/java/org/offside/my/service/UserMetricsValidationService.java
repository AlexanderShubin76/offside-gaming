package org.offside.my.service;

import org.offside.my.model.UserMetric;
import org.offside.my.pojo.ValidationResult;

public interface UserMetricsValidationService {
    ValidationResult isUserMetricValid(UserMetric userMetric, UserMetric userMetric1);

    ValidationResult isUserMetricValid(UserMetric userMetric);
}
