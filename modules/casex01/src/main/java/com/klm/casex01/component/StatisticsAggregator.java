package com.klm.casex01.component;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.IntStream.range;

@Component
@RequiredArgsConstructor
public class StatisticsAggregator {

    public static final String HTTP_METRIC_NAME = "http.server.requests";

    private final MeterRegistry registry;

    public Map<String, Number> getStatistics() {
        return new HashMap<String, Number>() {{
            put("totalRequests", statsTotalByKeys("exception", "method", "outcome", "status", "uri").orElse(0));
            put("avgResponseTimeMs", statsAvgByKeys("exception", "method", "outcome", "status", "uri").orElse(0));
            put("maxResponseTimeMs", statsMaxByKeys("exception", "method", "outcome", "status", "uri").orElse(0));
            compute("totalRequests2XX", (k, v) -> sumValues(ofNullable(v), statsTotalByTags(range(200, 207), "uri", "/location")));
            compute("totalRequests2XX", (k, v) -> sumValues(ofNullable(v), statsTotalByTags(range(200, 207), "uri", "/fare")));
            compute("totalRequests2XX", (k, v) -> sumValues(ofNullable(v), statsTotalByTags(range(200, 207), "uri", "/stats")));
            compute("totalRequests2XX", (k, v) -> sumValues(ofNullable(v), statsTotalByTags(range(200, 207), "uri", "/actuator/prometheus")));
            compute("totalRequests4XX", (k, v) -> sumValues(ofNullable(v), statsTotalByTags(range(400, 430), "uri", "/location")));
            compute("totalRequests4XX", (k, v) -> sumValues(ofNullable(v), statsTotalByTags(range(400, 430), "uri", "/fare")));
            compute("totalRequests4XX", (k, v) -> sumValues(ofNullable(v), statsTotalByTags(range(400, 430), "uri", "/stats")));
            compute("totalRequests4XX", (k, v) -> sumValues(ofNullable(v), statsTotalByTags(range(400, 430), "uri", "/actuator/prometheus")));
            compute("totalRequests5XX", (k, v) -> sumValues(ofNullable(v), statsTotalByTags(range(500, 505), "uri", "/location")));
            compute("totalRequests5XX", (k, v) -> sumValues(ofNullable(v), statsTotalByTags(range(500, 505), "uri", "/fare")));
            compute("totalRequests5XX", (k, v) -> sumValues(ofNullable(v), statsTotalByTags(range(500, 505), "uri", "/stats")));
            compute("totalRequests5XX", (k, v) -> sumValues(ofNullable(v), statsTotalByTags(range(500, 505), "uri", "/actuator/prometheus")));
        }};
    }

    private Optional<Number> statsTotalByTags(IntStream statusRange, String... tags) {
        return statusRange
          .mapToObj(status ->
            Stream.concat(
              Arrays.stream(tags),
              Arrays.stream(new String[]{"status", String.valueOf(status)})).toArray(String[]::new))
          .map(allTags -> statsTotalByTags(allTags))
          .reduce(of(0L), (a, b) -> of(a.orElse(0).longValue() + b.orElse(0).longValue()));
    }

    private Optional<Number> statsTotalByTags(String... tags) {
        return ofNullable(registry.find(HTTP_METRIC_NAME).tags(tags).timers())
          .map(timers -> timers.stream().map(timer -> timer.count()).reduce(0L, (a, b) -> a + b));
    }

    private Optional<Number> statsTotalByKeys(String... keys) {
        return ofNullable(registry.find(HTTP_METRIC_NAME).tagKeys(keys).timers())
          .map(timers -> timers.stream().map(timer -> timer.count()).reduce(0L, (a, b) -> a + b));
    }

    private Optional<Number> statsAvgByKeys(String... keys) {
        Optional<Number> totalTime = ofNullable(registry.find(HTTP_METRIC_NAME).tagKeys(keys).timers())
          .map(timers -> timers.stream().map(timer -> timer.totalTime(MILLISECONDS)).reduce(0D, (a, b) -> a + b));

        BigDecimal scaled = BigDecimal.valueOf(0);
        Optional<Number> totalValues = statsTotalByKeys(keys);

        if(totalValues.isPresent() && totalValues.get().longValue() > 0) {
            scaled = BigDecimal.valueOf(
              totalTime.orElse(0).doubleValue() / totalValues.get().longValue()).setScale(2, RoundingMode.HALF_UP);
        }

        return totalValues.isPresent() && totalValues.get().longValue() > 0 ?
          of(scaled) : of(0);
    }

    private Optional<Number> statsMaxByKeys(String... keys) {
        return ofNullable(registry.find(HTTP_METRIC_NAME).tagKeys(keys).timers())
          .map(timers -> timers.stream().map(timer -> timer.max(MILLISECONDS)).reduce(0D, (a, b) -> Math.max(a, b)));
    }

    private Long sumValues(Optional<Number> v1, Optional<Number> v2) {
        return v1.orElse(0).longValue() + v2.orElse(0).longValue();
    }

}
