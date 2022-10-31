package org.offside.my.controller;

import org.offside.my.dto.SendingResultDto;
import org.offside.my.dto.UserMetricsDto;
import org.offside.my.service.GasWaterUsageMonitoringService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GasWaterUsageMonitoringController implements GasWaterUsageMonitoringApi {

    private final GasWaterUsageMonitoringService gasWaterUsageMonitoringService;

    public GasWaterUsageMonitoringController(GasWaterUsageMonitoringService gasWaterUsageMonitoringService) {
        this.gasWaterUsageMonitoringService = gasWaterUsageMonitoringService;
    }

    @Override
    public ResponseEntity<List<UserMetricsDto>> getMetricsHistoryById(String id) {
        List<UserMetricsDto> userMetricsDtoList = gasWaterUsageMonitoringService.getMetricsHistory(id);
        return ResponseEntity.ok(userMetricsDtoList);
    }

    @Override
    public ResponseEntity<SendingResultDto> sendCurrentMetrics(UserMetricsDto userMetricsDto) {
        SendingResultDto resultDto = gasWaterUsageMonitoringService.addNewMetric(userMetricsDto);
        return ResponseEntity.ok(resultDto);
    }
}
