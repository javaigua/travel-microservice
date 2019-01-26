package com.klm.casex01;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.klm.casex01.component.configprops.SimpleTravelServiceConfigProps;
import com.klm.casex01.service.beans.airports.*;
import com.klm.casex01.service.beans.fares.Fares;
import com.klm.casex01.service.beans.token.Token;

import java.text.MessageFormat;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;

public class TestFixtures {

    public static final int DEFAULT_SIZE = 25;
    public static final int DEFAULT_PAGE = 1;
    public static final String DEFAULT_LANG = "EN";
    public static final String DEFAULT_CURRENCY = "EUR";

    public static final String AIRPORT_CODE_BOG = "BOG";
    public static final String AIRPORT_CODE_AMS = "AMS";

    public static Token buildToken() {
        return Token.builder()
          .accessToken("access-token")
          .tokenType("token-type")
          .scope("token-scope")
          .expiresIn(600)
          .build();
    }

    public static Airport buildAirport(String code) {
        return Airport.builder()
          .code(code)
          .name("name")
          .description("desc")
          .coordinates(Coordinates.builder()
            .latitude(1D)
            .longitude(2D)
            .build())
          .build();
    }

    public static Airports buildAirports(List<Airport> locations) {
        return Airports.builder()
          .page(Page.builder()
            .number(1)
            .size(2)
            .totalElements(locations.size())
            .totalPages(1)
            .build())
          .embedded(Embedded.builder()
            .locations(locations)
            .build())
          .build();
    }

    public static Fares buildFare(String origin, String destination) {
        return Fares.builder()
          .amount(10D)
          .currency("EUR")
          .origin(origin)
          .destination(destination)
          .build();
    }
    
    public static MappingBuilder sucessfulTravelServiceGetFareCall(String origin,
                                                                   String destination,
                                                                   SimpleTravelServiceConfigProps travelServiceConfigProps,
                                                                   Fares fare,
                                                                   ObjectMapper objectMapper,
                                                                   String currency) throws JsonProcessingException {
        SimpleTravelServiceConfigProps.Endpoints.Fares faresEndpoint = travelServiceConfigProps.getEndpoints().getFares();
        return get(MessageFormat.format(faresEndpoint.getPath(), origin, destination)+"?currency="+currency)
          .withHeader("Accept", equalTo("application/json"))
          .willReturn(
            aResponse()
              .withStatus(200)
              .withHeader("Content-Type", "application/hal+json; charset=UTF-8")
              .withBody(objectMapper.writeValueAsString(fare)));
    }

    public static MappingBuilder successfulTravelServiceGetAirportCall(String code,
                                                                       SimpleTravelServiceConfigProps travelServiceConfigProps,
                                                                       Airport airport,
                                                                       ObjectMapper objectMapper,
                                                                       String lang) throws JsonProcessingException {
        SimpleTravelServiceConfigProps.Endpoints.Airport airportEndpoint = travelServiceConfigProps.getEndpoints().getAirport();
        return get(MessageFormat.format(airportEndpoint.getPath(), code)+"?lang="+lang)
          .withHeader("Accept", equalTo("application/json"))
          .willReturn(
            aResponse()
              .withStatus(200)
              .withHeader("Content-Type", "application/hal+json; charset=UTF-8")
              .withBody(objectMapper.writeValueAsString(airport)));
    }

    public static MappingBuilder successfulTravelServiceGetAirportsCall(String term,
                                                                        SimpleTravelServiceConfigProps travelServiceConfigProps,
                                                                        Airports airports,
                                                                        ObjectMapper objectMapper,
                                                                        int size,
                                                                        int page,
                                                                        String lang) throws JsonProcessingException {
        SimpleTravelServiceConfigProps.Endpoints.Airports airportsEndpoint = travelServiceConfigProps.getEndpoints().getAirports();
        return get(airportsEndpoint.getPath() + "?term=" + term + "&size=" + size + "&page=" + page + "&lang=" + lang)
          .withHeader("Accept", equalTo("application/json"))
          .willReturn(
            aResponse()
              .withStatus(200)
              .withHeader("Content-Type", "application/hal+json; charset=UTF-8")
              .withBody(objectMapper.writeValueAsString(airports)));
    }

    public static MappingBuilder successfulTravelServicePostTokenCall(SimpleTravelServiceConfigProps travelServiceConfigProps,
                                                                      Token token,
                                                                      ObjectMapper objectMapper) throws JsonProcessingException {
        SimpleTravelServiceConfigProps.Endpoints.OAuth oAuthEndpoint = travelServiceConfigProps.getEndpoints().getOauth();
        return post(oAuthEndpoint.getPath())
          .withHeader("Accept", equalTo("application/json"))
          .withBasicAuth(oAuthEndpoint.getClientId(), oAuthEndpoint.getSecret())
          .willReturn(
            aResponse()
              .withStatus(200)
              .withHeader("Content-Type", "application/hal+json; charset=UTF-8")
              .withBody(objectMapper.writeValueAsString(token)));
    }

}
