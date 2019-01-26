package com.klm.casex01.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.klm.casex01.component.configprops.SimpleTravelServiceConfigProps;
import com.klm.casex01.service.beans.AirportsFareDetails;
import com.klm.casex01.service.beans.airports.Airport;
import com.klm.casex01.service.beans.airports.Airports;
import com.klm.casex01.service.beans.fares.Fares;
import com.klm.casex01.service.beans.token.Token;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.Arrays;

import static com.klm.casex01.TestFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ComponentScan("com.klm.casex01")
public class AirportsFareControllerTest {

    @Autowired(required=true)
    private ObjectMapper objectMapper;

    @Autowired(required=true)
    private SimpleTravelServiceConfigProps travelServiceConfigProps;

    @ClassRule
    public static WireMockRule wireMock = new WireMockRule(wireMockConfig().dynamicPort());

    @Autowired(required=true)
    private TestRestTemplate restTemplate;

    @Before
    public void setUp() {
        wireMock.resetToDefaultMappings();
        travelServiceConfigProps.setBaseUrl("http://localhost:" + wireMock.port());
    }

    @Test
    public void shouldGetLocation() throws Exception {
        Token token = buildToken();
        Airport airport1 = buildAirport(AIRPORT_CODE_BOG);
        Airport airport2 = buildAirport(AIRPORT_CODE_AMS);

        //given
        // a request to casex01 /location controller
        RequestEntity request = RequestEntity.get(URI.create("/location?term="+AIRPORT_CODE_BOG))
          .header(HttpHeaders.ACCEPT, "application/json; charset=UTF-8")
          .build();
        // which triggers a couple of successful requests to simple travel service
        givenThat(successfulTravelServicePostTokenCall(travelServiceConfigProps, token, objectMapper));
        givenThat(successfulTravelServiceGetAirportsCall(AIRPORT_CODE_BOG, travelServiceConfigProps,
          buildAirports(Arrays.asList(new Airport[]{airport1, airport2})), objectMapper, DEFAULT_SIZE, DEFAULT_PAGE, DEFAULT_LANG));

        //when
        ResponseEntity<Airports> response = restTemplate.exchange(request, Airports.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmbedded().getLocations().size()).isEqualTo(2);
        assertThat(response.getHeaders().get("X-Trace-Id")).isNotNull();
    }

    @Test
    public void shouldGetFare() throws Exception {
        Token token = buildToken();
        Airport airport1 = buildAirport(AIRPORT_CODE_BOG);
        Airport airport2 = buildAirport(AIRPORT_CODE_AMS);
        Fares fare = buildFare(AIRPORT_CODE_BOG, AIRPORT_CODE_AMS);

        //given
        // a request to casex01 /location controller
        RequestEntity request = RequestEntity.get(URI.create("/fare?origin="+AIRPORT_CODE_BOG+"&destination="+AIRPORT_CODE_AMS))
          .header(HttpHeaders.ACCEPT, "application/json; charset=UTF-8")
          .build();
        // which triggers a couple of successful requests to simple travel service
        givenThat(successfulTravelServicePostTokenCall(travelServiceConfigProps, token, objectMapper));
        givenThat(successfulTravelServiceGetAirportCall(AIRPORT_CODE_BOG, travelServiceConfigProps, airport1, objectMapper, DEFAULT_LANG));
        givenThat(successfulTravelServiceGetAirportCall(AIRPORT_CODE_AMS, travelServiceConfigProps, airport2, objectMapper, DEFAULT_LANG));
        givenThat(sucessfulTravelServiceGetFareCall(AIRPORT_CODE_BOG, AIRPORT_CODE_AMS, travelServiceConfigProps, fare, objectMapper, DEFAULT_CURRENCY));

        //when
        ResponseEntity<AirportsFareDetails> response = restTemplate.exchange(request, AirportsFareDetails.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFare()).isNotNull();
        assertThat(response.getBody().getOrigin()).isNotNull();
        assertThat(response.getBody().getDestination()).isNotNull();
        assertThat(response.getHeaders().get("X-Trace-Id")).isNotNull();
    }
}
