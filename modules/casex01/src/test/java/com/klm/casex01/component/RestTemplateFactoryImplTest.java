package com.klm.casex01.component;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestTemplateFactoryImplTest {

    @Autowired(required=true)
    private RestTemplateFactoryImpl templateFactory;

    @Test
    public void shouldReturnFactory() {
        //given
        PoolingHttpClientConnectionManager clientConnectionManager = mock(PoolingHttpClientConnectionManager.class);
        ReflectionTestUtils.setField(templateFactory, "connectionManager", clientConnectionManager);

        //when
        RestTemplate restTemplate = templateFactory.getRestTemplate();

        //then
        assertThat(restTemplate).isNotNull();
    }
}