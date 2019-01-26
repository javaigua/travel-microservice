package com.klm.casex01.service;

import com.klm.casex01.service.beans.airports.Airport;
import com.klm.casex01.service.beans.airports.Airports;
import com.klm.casex01.service.beans.fares.Fares;
import com.klm.casex01.service.beans.token.Token;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface SimpleTravelService {

    Token getToken();

    Airports getAirports(String term, Optional<String> lang, Optional<Integer> page, Optional<Integer> size,
                         Optional<Token> currentToken);

    Airport getAirport(String code, Optional<String> lang, Optional<Token> currentToken);

    Fares getFare(String originCode, String destinationCode, Optional<String> currency, Optional<Token> currentToken);

    CompletableFuture<Airport> getAirportAsync(String code, Optional<String> lang, Optional<Token> currentToken);

    CompletableFuture<Fares> getFareAsync(String originCode, String destinationCode, Optional<String> currency,
                                          Optional<Token> currentToken);

}
