package com.klm.casex01.controller;

import brave.Tracer;
import com.klm.casex01.service.SimpleTravelService;
import com.klm.casex01.service.SimpleTravelServiceException;
import com.klm.casex01.service.beans.AirportsFareDetails;
import com.klm.casex01.service.beans.airports.Airport;
import com.klm.casex01.service.beans.airports.Airports;
import com.klm.casex01.service.beans.fares.Fares;
import com.klm.casex01.service.beans.token.Token;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.util.Optional.*;

@Slf4j
@RestController
@Timed(histogram = true)
@RequiredArgsConstructor
public class AirportsFareController {

    public static final String DEFAULT_SIZE = "25";
    public static final String DEFAULT_PAGE = "1";
    public static final String DEFAULT_LANG = "EN";
    public static final String DEFAULT_CURRENCY = "EUR";

    private final Tracer tracer;
    private final SimpleTravelService simpleTravelService;

    @RequestMapping(value = "/location", method = RequestMethod.GET, produces = "application/json")
    public Callable<ResponseEntity<Airports>> location(@RequestParam(value = "term", required = true) String term,
                                                       @RequestParam(value = "lang", defaultValue = DEFAULT_LANG) String lang,
                                                       @RequestParam(value = "page", defaultValue = DEFAULT_PAGE) Integer page,
                                                       @RequestParam(value = "size", defaultValue = DEFAULT_SIZE) Integer size) {
        log.info("Incoming location request, {} {} {} {}", term, lang, page, size);

        return () -> {
            Airports airports = simpleTravelService.getAirports(term, of(lang), of(page), of(size), empty());
            airports.setTerm(term);
            return ResponseEntity.status(HttpStatus.OK)
              .header("X-Trace-Id", tracer.currentSpan().context().traceIdString())
              .body(airports);
        };
    }

    @RequestMapping(value = "/fare", method = RequestMethod.GET, produces = "application/json")
    public Callable<ResponseEntity<AirportsFareDetails>> fare(@RequestParam(value = "origin", required = true) String origin,
                                      @RequestParam(value = "destination", required = true) String destination,
                                      @RequestParam(value = "lang", defaultValue = DEFAULT_LANG) String lang,
                                      @RequestParam(value = "currency", defaultValue = DEFAULT_CURRENCY) String currency) {
        log.info("Incoming fare request, {} {} {} {}", origin, destination, lang, currency);

        return () -> {
            Token token = simpleTravelService.getToken();
            try {
                CompletableFuture<Airport> originAirport = simpleTravelService.getAirportAsync(origin, of(lang), of(token));
                CompletableFuture<Airport> destinationAirport = simpleTravelService.getAirportAsync(destination, of(lang), of(token));
                CompletableFuture<Fares> fare = simpleTravelService.getFareAsync(origin, destination, of(currency), of(token));

                CompletableFuture.allOf(originAirport, destinationAirport, fare).join();

                return ResponseEntity.status(HttpStatus.OK)
                  .header("X-Trace-Id", tracer.currentSpan().context().traceIdString())
                  .body(new AirportsFareDetails(originAirport.get(), destinationAirport.get(), fare.get()));
            } catch (final InterruptedException | ExecutionException e) {
                log.error("Error while fetching airports and fare", e);
                throw new SimpleTravelServiceException("Error while fetching airports and fare details", e);
            }
        };
    }

}
