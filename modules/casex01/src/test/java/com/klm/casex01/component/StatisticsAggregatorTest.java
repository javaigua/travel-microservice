package com.klm.casex01.component;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatisticsAggregatorTest {

    @Autowired(required=true)
    private StatisticsAggregator statisticsAggregator;

    @Test
    public void shouldReturnEmptyStatistics() {
        //when
        Map<String, Number> stats = statisticsAggregator.getStatistics();

        //then
        assertThat(stats).isNotNull();
        assertThat(stats).isNotEmpty();
        assertThat(stats.keySet().size()).isEqualTo(6);
        assertThat(stats.entrySet().iterator().next().getValue()).isEqualTo(0);
    }


}
