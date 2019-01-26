package com.klm.casex01.service;

import com.klm.casex01.component.RestTemplateFactory;
import com.klm.casex01.service.beans.airports.Airport;
import com.klm.casex01.service.beans.airports.Airports;
import com.klm.casex01.service.beans.fares.Fares;
import com.klm.casex01.service.beans.token.Token;
import com.klm.casex01.component.configprops.SimpleTravelServiceConfigProps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleTravelServiceImpl implements SimpleTravelService {

    private static final List<MediaType> MEDIA_TYPES = new ArrayList() {{
        add(MediaType.APPLICATION_JSON);
    }};

    private final SimpleTravelServiceConfigProps configProps;
    private final RestTemplateFactory restTemplateFactory;

    @Override
    public Token getToken() {
        String username = configProps.getEndpoints().getOauth().getClientId();
        String password = configProps.getEndpoints().getOauth().getSecret();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
          configProps.getBaseUrl().concat(configProps.getEndpoints().getOauth().getPath()));

        HttpEntity<Token> response = restTemplateFactory.getRestTemplate()
                .exchange(builder.build().encode().toUri(),
                  HttpMethod.POST,
                  new HttpEntity(getHeadersWithBasicAuth(username, password)),
                  Token.class);

        log.info("Token fetched, {}", response.getBody().toString());
        return response.getBody();
    }

    @Override
    public Airports getAirports(String term, Optional<String> lang, Optional<Integer> page,
                                Optional<Integer> size, Optional<Token> currentToken) {
        if(!Optional.ofNullable(term).isPresent()) {
            throw new SimpleTravelServiceException("term parameter must be defined");
        }
        Token token = currentToken.isPresent() ? currentToken.get() : this.getToken();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
          configProps.getBaseUrl().concat(configProps.getEndpoints().getAirports().getPath()));
        builder.queryParam("term", term);
        if (size.isPresent()) {
            builder.queryParam("size", size.get());
        }
        if (page.isPresent()) {
            builder.queryParam("page", page.get());
        }
        if (lang.isPresent()) {
            builder.queryParam("lang", lang.get());
        }

        HttpEntity<Airports> response = restTemplateFactory.getRestTemplate()
          .exchange(builder.build().encode().toUri(),
            HttpMethod.GET,
            new HttpEntity(getHeadersWithTokenAuth(token)),
            Airports.class);

        log.info("Airports fetched, {}", response.getBody().getPage().toString());
        return response.getBody();
    }

    @Override
    public Airport getAirport(String code, Optional<String> lang, Optional<Token> currentToken) {
        Token token = currentToken.isPresent() ? currentToken.get() : this.getToken();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
          configProps.getBaseUrl().concat(
            MessageFormat.format(configProps.getEndpoints().getAirport().getPath(), code)));
        if (lang.isPresent()) {
            builder.queryParam("lang", lang.get());
        }

        HttpEntity<Airport> response = restTemplateFactory.getRestTemplate()
          .exchange(builder.build().encode().toUri(),
            HttpMethod.GET,
            new HttpEntity(getHeadersWithTokenAuth(token)),
            Airport.class);

        log.info("Airport fetched, {}", response.getBody().getCode());
        return response.getBody();
    }

    @Override
    public Fares getFare(String originCode, String destinationCode, Optional<String> currency, Optional<Token> currentToken) {
        Token token = currentToken.isPresent() ? currentToken.get() : this.getToken();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
          configProps.getBaseUrl().concat(
            MessageFormat.format(configProps.getEndpoints().getFares().getPath(), originCode, destinationCode)));
        if (currency.isPresent()) {
            builder.queryParam("currency", currency.get());
        }

        HttpEntity<Fares> response = restTemplateFactory.getRestTemplate()
          .exchange(builder.build().encode().toUri(),
            HttpMethod.GET,
            new HttpEntity(getHeadersWithTokenAuth(token)),
            Fares.class);

        log.info("Fare fetched, {}-{} {}{}", response.getBody().getOrigin(), response.getBody().getDestination(),
          response.getBody().getAmount(), response.getBody().getCurrency());
        return response.getBody();
    }

    @Override
    @Async("customTaskExecutor")
    public CompletableFuture<Airport> getAirportAsync(String code, Optional<String> lang, Optional<Token> currentToken) {
        log.info("Async airport fetching, {}", code);
        return CompletableFuture.completedFuture(getAirport(code, lang, currentToken));
    }

    @Override
    @Async("customTaskExecutor")
    public CompletableFuture<Fares> getFareAsync(String originCode, String destinationCode, Optional<String> currency,
                                                 Optional<Token> currentToken) {
        log.info("Async fare fetching, {}-{}", originCode, destinationCode);
        return CompletableFuture.completedFuture(getFare(originCode, destinationCode, currency, currentToken));
    }

    private HttpHeaders getDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(MEDIA_TYPES);
        return headers;
    }

    private HttpHeaders getHeadersWithBasicAuth(String username, String password) {
        HttpHeaders headers = getDefaultHeaders();
        headers.setBasicAuth(username, password);
        return  headers;
    }

    private HttpHeaders getHeadersWithTokenAuth(Token token) {
        HttpHeaders headers = getDefaultHeaders();
        headers.set("Authorization", String.format("%s %s", token.getTokenType(), token.getAccessToken()));
        return  headers;
    }
}
