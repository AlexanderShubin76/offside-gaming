package org.offside.my.mapper;

import org.mapstruct.Mapper;
import org.offside.my.dto.SendingResultDto;
import org.offside.my.pojo.ValidationResult;

@Mapper
public interface SendingResultMapper {

    SendingResultDto mapResult(ValidationResult validationResult);
}
