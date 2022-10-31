package org.offside.my.service;

import org.offside.my.dto.SendingResultDto;
import org.offside.my.dto.UserMetricsDto;

import java.util.List;

public interface GasWaterUsageMonitoringService {

    SendingResultDto addNewMetric(UserMetricsDto userMetricsDto);

    List<UserMetricsDto> getMetricsHistory(String id);
}
