package com.klm.casex01.component.configprops;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "simple-travel-service")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SimpleTravelServiceConfigProps {

    private String baseUrl;
    private Endpoints endpoints;

    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode
    public static class Endpoints {
        private OAuth oauth;
        private Airport airport;
        private Airports airports;
        private Fares fares;

        @Getter
        @Setter
        @ToString
        @EqualsAndHashCode
        public static class OAuth {
            private String path;
            private String clientId;
            private String secret;
        }

        @Getter
        @Setter
        @ToString
        @EqualsAndHashCode
        public static class Airport {
            private String path;
        }

        @Getter
        @Setter
        @ToString
        @EqualsAndHashCode
        public static class Airports {
            private String path;
        }

        @Getter
        @Setter
        @ToString
        @EqualsAndHashCode
        public static class Fares {
            private String path;
        }
    }
}
