package org.offside.my.mapper;

import org.mapstruct.Mapper;
import org.offside.my.dto.UserMetricsDto;
import org.offside.my.model.UserMetric;

import java.util.List;

@Mapper
public interface UserMetricsMapper {

    UserMetric mapToUserMetrics(UserMetricsDto userMetricsDto);

    List<UserMetricsDto> mapToUserMetricsDtoList(List<UserMetric> userMetricList);

    UserMetricsDto mapToUserMetricsDto(UserMetric userMetric);
}
