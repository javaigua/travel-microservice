package com.klm.casex01.component;

import com.klm.casex01.component.configprops.TaskExecutorsConfigProps;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableAsync
@RequiredArgsConstructor
public class TaskExecutorsConfig implements WebMvcConfigurer {

    private final TaskExecutorsConfigProps configProps;

    @Bean(name = "customTaskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(configProps.getAsyncTasks().getCorePoolSize());
        executor.setMaxPoolSize(configProps.getAsyncTasks().getMaxPoolSize());
        executor.setQueueCapacity(configProps.getAsyncTasks().getQueueCapacity());
        executor.setThreadNamePrefix(configProps.getAsyncTasks().getThreadNamePrefix());
        executor.initialize();
        return executor;
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(configProps.getWebTasks().getCorePoolSize());
        executor.setMaxPoolSize(configProps.getWebTasks().getMaxPoolSize());
        executor.setQueueCapacity(configProps.getWebTasks().getQueueCapacity());
        executor.setThreadNamePrefix(configProps.getWebTasks().getThreadNamePrefix());
        executor.initialize();
        configurer.setTaskExecutor(executor);
    }

}
