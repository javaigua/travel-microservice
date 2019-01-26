package com.klm.casex01.component;

import com.klm.casex01.component.configprops.RestTemplateConfigProps;
import lombok.RequiredArgsConstructor;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class RestTemplateFactoryImpl implements RestTemplateFactory {

    private final RestTemplateConfigProps configProps;
    private PoolingHttpClientConnectionManager connectionManager;

    @PostConstruct
    private void init() {
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(configProps.getConnectionManager().getMaxTotal());
        connectionManager.setDefaultMaxPerRoute(configProps.getConnectionManager().getDefaultMaxPerRoute());
        connectionManager.setValidateAfterInactivity(configProps.getConnectionManager().getValidateAfterInactivity());

        SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(configProps.getConnectionManager().getSocketConfig().getTcpNoDelay())
                .setSoKeepAlive(configProps.getConnectionManager().getSocketConfig().getSoKeepAlive())
                .setSoReuseAddress(configProps.getConnectionManager().getSocketConfig().getSoReuseAddress())
                .build();
        connectionManager.setDefaultSocketConfig(socketConfig);
    }

    @Override
    public RestTemplate getRestTemplate() {

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .disableAuthCaching()
                .disableCookieManagement()
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setReadTimeout(configProps.getRequestFactory().getReadTimeout());
        requestFactory.setConnectTimeout(configProps.getRequestFactory().getConnectTimeout());
        requestFactory.setConnectionRequestTimeout(configProps.getRequestFactory().getConnectionRequestTimeout());
        requestFactory.setHttpClient(httpClient);
        requestFactory.setBufferRequestBody(configProps.getRequestFactory().getBufferRequestBody());

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }
}
