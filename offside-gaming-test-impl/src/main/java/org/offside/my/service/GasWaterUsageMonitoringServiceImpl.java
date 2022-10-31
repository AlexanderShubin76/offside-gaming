package org.offside.my.service;

import org.offside.my.dto.SendingResultDto;
import org.offside.my.dto.UserMetricsDto;
import org.offside.my.mapper.SendingResultMapper;
import org.offside.my.mapper.UserMetricsMapper;
import org.offside.my.model.UserMetric;
import org.offside.my.pojo.ValidationResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GasWaterUsageMonitoringServiceImpl implements GasWaterUsageMonitoringService {

    private final DaoMonitoringService daoMonitoringService;
    private final UserMetricsMapper userMetricsMapper;
    private final UserMetricsValidationService userMetricsValidationService;
    private final SendingResultMapper sendingResultMapper;

    public GasWaterUsageMonitoringServiceImpl(
            DaoMonitoringService daoMonitoringService,
            UserMetricsMapper userMetricsMapper,
            UserMetricsValidationService userMetricsValidationService,
            SendingResultMapper sendingResultMapper
    ) {
        this.daoMonitoringService = daoMonitoringService;
        this.userMetricsMapper = userMetricsMapper;
        this.userMetricsValidationService = userMetricsValidationService;
        this.sendingResultMapper = sendingResultMapper;
    }

    @Override
    public SendingResultDto addNewMetric(UserMetricsDto userMetricsDto) {
        final UserMetric userMetric = userMetricsMapper.mapToUserMetrics(userMetricsDto);
        Optional<UserMetric> lastUserMetrics = daoMonitoringService.findLastUserMetrics(userMetric.getUserId());
        ValidationResult validationResult;
        if (lastUserMetrics.isPresent()) {
            validationResult = userMetricsValidationService.isUserMetricValid(userMetric, lastUserMetrics.get());
        } else {
            validationResult = userMetricsValidationService.isUserMetricValid(userMetric);
        }
        if (!validationResult.isResult()) {
            return sendingResultMapper.mapResult(validationResult);
        }
        daoMonitoringService.addNewMetric(userMetric);
        return sendingResultMapper.mapResult(validationResult);
    }

    @Override
    public List<UserMetricsDto> getMetricsHistory(String id) {
        List<UserMetric> userMetricList = daoMonitoringService.getAllUserMetrics(id);
        return userMetricsMapper.mapToUserMetricsDtoList(userMetricList);
    }
}
