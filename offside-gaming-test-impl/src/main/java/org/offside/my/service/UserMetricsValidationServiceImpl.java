package org.offside.my.service;

import org.offside.my.model.UserMetric;
import org.offside.my.pojo.ValidationResult;
import org.springframework.stereotype.Service;

@Service
public class UserMetricsValidationServiceImpl implements UserMetricsValidationService {
    @Override
    public ValidationResult isUserMetricValid(UserMetric newUserMetric, UserMetric oldUserMetric) {
        final Double gasMetric = newUserMetric.getGasMetric();
        final Double coldWaterMetric = newUserMetric.getColdWaterMetric();
        final Double hotWaterMetric = newUserMetric.getHotWaterMetric();
        final ValidationResult validationResult = checkIsValidValues(gasMetric, coldWaterMetric, hotWaterMetric);
        if (validationResult != null) {
            return validationResult;
        }
        if (gasMetric < oldUserMetric.getGasMetric()) {
            return createValidationResult(false, "Показания газа введены неверно");
        }
        if (coldWaterMetric < oldUserMetric.getColdWaterMetric()) {
            return createValidationResult(false, "Показания холодной воды введены неверно");
        }
        if (hotWaterMetric < oldUserMetric.getHotWaterMetric()) {
            return createValidationResult(false, "Показания горячей воды введены неверно");
        }
        return createValidationResult(true, null);
    }

    @Override
    public ValidationResult isUserMetricValid(UserMetric userMetric) {
        final Double gasMetric = userMetric.getGasMetric();
        final Double coldWaterMetric = userMetric.getColdWaterMetric();
        final Double hotWaterMetric = userMetric.getHotWaterMetric();
        final ValidationResult validationResult = checkIsValidValues(gasMetric, coldWaterMetric, hotWaterMetric);
        if (validationResult != null) {
            return validationResult;
        }
        if (gasMetric < 0 || coldWaterMetric < 0 || hotWaterMetric < 0) {
            return createValidationResult(false, "Введены отрицательные значения");
        }
        return createValidationResult(true, null);
    }

    private ValidationResult checkIsValidValues(Double gasMetric, Double coldWaterMetric, Double hotWaterMetric) {
        if (gasMetric == null || coldWaterMetric == null || hotWaterMetric == null) {
            return createValidationResult(false, "Введены не все метрики");
        }
        if (gasMetric.isNaN() || gasMetric.isInfinite() || coldWaterMetric.isNaN() || coldWaterMetric.isInfinite() || hotWaterMetric.isNaN() || hotWaterMetric.isInfinite()) {
            return createValidationResult(false, "Неправильно введены метрики");
        }
        return null;
    }

    private ValidationResult createValidationResult(Boolean result, String errorMessage) {
        return ValidationResult.builder()
                .result(result)
                .errorMessage(errorMessage)
                .build();
    }
}
