package com.klm.casex01.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.klm.casex01.Casex01Application;
import com.klm.casex01.TestFixtures;
import com.klm.casex01.component.configprops.SimpleTravelServiceConfigProps;
import com.klm.casex01.service.beans.airports.*;
import com.klm.casex01.service.beans.fares.Fares;
import com.klm.casex01.service.beans.token.Token;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static java.util.Optional.*;
import static com.klm.casex01.TestFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Casex01Application.class)
@SpringBootApplication
@ComponentScan("com.klm.casex01")
public class SimpleTravelServiceImplTest {

    @Autowired(required=true)
    private ObjectMapper objectMapper;

    @Autowired(required=true)
    private SimpleTravelServiceConfigProps travelServiceConfigProps;

    @ClassRule
    public static WireMockRule wireMock = new WireMockRule(wireMockConfig().dynamicPort());

    @Autowired(required=true)
    private SimpleTravelServiceImpl simpleTravelService;

    private Token token;
    private Airport airport1;
    private Airport airport2;
    private Airports airports;
    private Fares fare;

    @Before
    public void setUp() {
        wireMock.resetToDefaultMappings();
        travelServiceConfigProps.setBaseUrl("http://localhost:" + wireMock.port());

        token = TestFixtures.buildToken();
        airport1 = TestFixtures.buildAirport(AIRPORT_CODE_BOG);
        airport2 = TestFixtures.buildAirport(AIRPORT_CODE_AMS);
        airports = TestFixtures.buildAirports(Arrays.asList(new Airport[]{airport1, airport2}));
        fare = TestFixtures.buildFare(AIRPORT_CODE_BOG, AIRPORT_CODE_AMS);
    }

    @Test
    public void shouldGetToken() throws Exception {
        //given
        givenThat(successfulTravelServicePostTokenCall(travelServiceConfigProps, token, objectMapper));

        //when
        Token response = simpleTravelService.getToken();

        //then
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo(token.getAccessToken());
    }

    @Test
    public void shouldGetAirports() throws Exception {
        //given
        givenThat(successfulTravelServiceGetAirportsCall(AIRPORT_CODE_BOG, travelServiceConfigProps, airports, objectMapper,
          DEFAULT_SIZE, DEFAULT_PAGE, DEFAULT_LANG));

        //when
        Airports response = simpleTravelService.getAirports(AIRPORT_CODE_BOG, of(DEFAULT_LANG), of(DEFAULT_PAGE), of(DEFAULT_SIZE), of(token));

        //then
        assertThat(response).isNotNull();
        assertThat(response.getEmbedded().getLocations().size()).isEqualTo(airports.getEmbedded().getLocations().size());
    }

    @Test
    public void shouldGetAirport() throws Exception {
        //given
        givenThat(successfulTravelServiceGetAirportCall(AIRPORT_CODE_BOG, travelServiceConfigProps, airport1, objectMapper, DEFAULT_LANG));

        //when
        Airport response = simpleTravelService.getAirport(AIRPORT_CODE_BOG, of(DEFAULT_LANG), of(token));

        //then
        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo(AIRPORT_CODE_BOG);
    }

    @Test
    public void shouldGetFare() throws Exception {
        //given
        givenThat(sucessfulTravelServiceGetFareCall(AIRPORT_CODE_BOG, AIRPORT_CODE_AMS, travelServiceConfigProps, fare, objectMapper, DEFAULT_CURRENCY));

        //when
        Fares response = simpleTravelService.getFare(AIRPORT_CODE_BOG, AIRPORT_CODE_AMS, of(DEFAULT_CURRENCY), of(token));

        //then
        assertThat(response).isNotNull();
        assertThat(response.getAmount()).isEqualTo(fare.getAmount());
        assertThat(response.getCurrency()).isEqualTo(fare.getCurrency());
        assertThat(response.getOrigin()).isEqualTo(airport1.getCode());
        assertThat(response.getDestination()).isEqualTo(airport2.getCode());
    }

}
