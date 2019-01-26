package com.klm.casex01.component.configprops;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rest-template")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class RestTemplateConfigProps {

    private ConnectionManager connectionManager;
    private RequestFactory requestFactory;

    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode
    public static class ConnectionManager {
        private Integer maxTotal;
        private Integer defaultMaxPerRoute;
        private Integer validateAfterInactivity;
        private SocketConfig socketConfig;

        @Getter
        @Setter
        @ToString
        @EqualsAndHashCode
        public static class SocketConfig {
            private Boolean tcpNoDelay;
            private Boolean soKeepAlive;
            private Boolean soReuseAddress;
        }
    }

    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode
    public static class RequestFactory {
        private Integer readTimeout;
        private Integer connectTimeout;
        private Integer connectionRequestTimeout;
        private Boolean bufferRequestBody;
    }

}
