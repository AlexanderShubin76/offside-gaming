package org.offside.my;

import static java.lang.Double.parseDouble;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.offside.my.dto.SendingResultDto;
import org.offside.my.dto.UserMetricsDto;
import org.offside.my.model.UserMetric;
import org.offside.my.repository.UserMetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = OffsideApplication.class
)
public class ApplicationTest {

    private static final String SEND_METRIC_URL = "/api/v1/send_metrics";
    private static final String GET_HISTORY_URL = "/api/v1/get_metrics_history/";
    private static final ParameterizedTypeReference<List<UserMetricsDto>> FIND_ALL_RESPONSE
            = new ParameterizedTypeReference<List<UserMetricsDto>>() {
    };

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserMetricsRepository userMetricsRepository;

    @BeforeEach
    public void cleanDataBeforeTest() {
        userMetricsRepository.deleteAll();
    }

    @AfterEach
    public void cleanDataAfterTest() {
        userMetricsRepository.deleteAll();
    }

    @Test
    public void sendFirstMetricWithEmptyMetric() {
        final Boolean result = sendNewMetricTestBody(
                RandomStringUtils.random(6),
                parseDouble("1" + RandomStringUtils.randomNumeric(5)),
                parseDouble("1" + RandomStringUtils.randomNumeric(5)),
                null
        );
        assertFalse(result);
    }

    @Test
    public void sendFirstMetricWithInfiniteMetric() {
        final Boolean result = sendNewMetricTestBody(
                RandomStringUtils.random(6),
                parseDouble("1" + RandomStringUtils.randomNumeric(5)),
                Double.POSITIVE_INFINITY,
                parseDouble("1" + RandomStringUtils.randomNumeric(5))
        );
        assertFalse(result);
    }

    @Test
    public void sendFirstMetricWithLessZeroMetric() {
        final Boolean result = sendNewMetricTestBody(
                RandomStringUtils.random(6),
                -55.0,
                parseDouble("1" + RandomStringUtils.randomNumeric(5)),
                parseDouble("1" + RandomStringUtils.randomNumeric(5))
        );
        assertFalse(result);
    }

    @Test
    public void sendNotFirstMetricWithLessGasMetric() {
        final String userId = createDataInDB();
        final Boolean result = sendNewMetricTestBody(userId, 32.2, 40.1, 69.9);
        assertFalse(result);
        final List<UserMetric> newUserMetricList = userMetricsRepository.findAll();
        assertEquals(2, newUserMetricList.size());
    }

    @Test
    public void sendNotFirstMetricWithLessColdWaterMetric() {
        final String userId = createDataInDB();
        final Boolean result = sendNewMetricTestBody(userId, 32.9, 38.1, 69.9);
        assertFalse(result);
        final List<UserMetric> newUserMetricList = userMetricsRepository.findAll();
        assertEquals(2, newUserMetricList.size());
    }

    @Test
    public void sendNotFirstMetricWithLessHotWaterMetric() {
        final String userId = createDataInDB();
        final Boolean result = sendNewMetricTestBody(userId, 32.9, 48.1, 69.0);
        assertFalse(result);
        final List<UserMetric> newUserMetricList = userMetricsRepository.findAll();
        assertEquals(2, newUserMetricList.size());
    }

    @Test
    public void successfulSendFirstMetric() {
        final String userId = RandomStringUtils.random(6);
        final List<UserMetric> userMetricList = userMetricsRepository.findAll();
        assertEquals(0, userMetricList.size());
        final Boolean result = sendNewMetricTestBody(userId, 32.9, 48.1, 69.9);
        assertTrue(result);
        final List<UserMetric> newUserMetricList = userMetricsRepository.findAll();
        assertEquals(1, newUserMetricList.size());
    }

    @Test
    public void successfulSendNotFirstMetric() {
        final String userId = createDataInDB();
        final Boolean result = sendNewMetricTestBody(userId, 32.9, 48.1, 69.9);
        assertTrue(result);
        final List<UserMetric> newUserMetricList = userMetricsRepository.findAll();
        assertEquals(3, newUserMetricList.size());
    }

    @Test
    public void getEmptyMetricsHistory() {
        final String userId = RandomStringUtils.random(6);
        assertGetHistory(userId, 0);
    }

    @Test
    public void getMetricsHistory() {
        final String userId = createDataInDB();
        assertGetHistory(userId, 2);
    }

    private Boolean sendNewMetricTestBody(String userId, Double gasMetric, Double coldWaterMetric, Double hotWaterMetric) {
        final String uriString = UriComponentsBuilder.fromPath(SEND_METRIC_URL)
                .build()
                .toUriString();
        final UserMetricsDto userMetricsDto = UserMetricsDto.builder()
                .userId(userId)
                .gasMetric(gasMetric)
                .coldWaterMetric(coldWaterMetric)
                .hotWaterMetric(hotWaterMetric)
                .build();
        final HttpEntity<UserMetricsDto> request = new HttpEntity<>(userMetricsDto);
        final ResponseEntity<SendingResultDto> responseEntity = restTemplate.exchange(uriString, POST, request, SendingResultDto.class);
        assertEquals(OK, responseEntity.getStatusCode());
        final SendingResultDto sendingResultDto = responseEntity.getBody();
        assertNotNull(sendingResultDto);
        final Boolean result = sendingResultDto.getResult();
        assertNotNull(result);
        return result;
    }

    private UserMetric buildUserMetric(String userId, double v, double v2, double v3) {
        return UserMetric.builder()
                .userId(userId)
                .gasMetric(v)
                .coldWaterMetric(v2)
                .hotWaterMetric(v3)
                .build();
    }

    private String createDataInDB() {
        final String userId = RandomStringUtils.random(6);
        final UserMetric firstMetric = buildUserMetric(userId, 12.0, 35.1, 64.2);
        userMetricsRepository.save(firstMetric);
        final UserMetric secondMetric = buildUserMetric(userId, 32.8, 39.1, 69.2);
        userMetricsRepository.save(secondMetric);
        final List<UserMetric> userMetricList = userMetricsRepository.findAll();
        assertEquals(2, userMetricList.size());
        return userId;
    }

    private void assertGetHistory(String userId, int size) {
        final String uriString = UriComponentsBuilder.fromPath(GET_HISTORY_URL + userId)
                .build()
                .toUriString();
        final HttpEntity<Object> request = new HttpEntity<>(new HttpHeaders());
        final ResponseEntity<List<UserMetricsDto>> responseEntity = restTemplate.exchange(uriString, GET, request, FIND_ALL_RESPONSE);
        assertEquals(OK, responseEntity.getStatusCode());
        final List<UserMetricsDto> userMetricsDtoList = responseEntity.getBody();
        assertNotNull(userMetricsDtoList);
        assertEquals(size, userMetricsDtoList.size());
    }
}
