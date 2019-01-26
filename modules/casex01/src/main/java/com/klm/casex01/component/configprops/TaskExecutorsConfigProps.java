package com.klm.casex01.component.configprops;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "task-executors")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class TaskExecutorsConfigProps {

    private AsyncTasks asyncTasks;
    private WebTasks webTasks;

    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode
    public static class WebTasks {
        private Integer corePoolSize;
        private Integer maxPoolSize;
        private Integer queueCapacity;
        private String threadNamePrefix;
    }

    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode
    public static class AsyncTasks {
        private Integer corePoolSize;
        private Integer maxPoolSize;
        private Integer queueCapacity;
        private String threadNamePrefix;
    }

}
