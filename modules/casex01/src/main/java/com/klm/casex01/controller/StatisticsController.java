package com.klm.casex01.controller;

import com.klm.casex01.component.StatisticsAggregator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.Callable;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsAggregator statisticsAggregator;

    @RequestMapping("/stats")
    public Callable<Map<String, Number>> stats() {
        log.info("Incoming stats request");
        return () -> statisticsAggregator.getStatistics();
    }

}
