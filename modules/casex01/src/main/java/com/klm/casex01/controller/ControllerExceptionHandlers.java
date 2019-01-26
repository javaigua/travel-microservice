package com.klm.casex01.controller;

import com.klm.casex01.service.SimpleTravelServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandlers {

    @ExceptionHandler(HttpClientErrorException.class)
    public HttpEntity handleHttpClientErrorException(HttpClientErrorException e) {
        log.error("Error on http client", e);
        return new ResponseEntity(e.getMessage(), e.getStatusCode());
    }

    @ExceptionHandler(RestClientException.class)
    public HttpEntity handleRestClientException(RestClientException e) {
        log.error("Error on rest client", e);
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public HttpEntity handleResourceAccessException(ResourceAccessException e) {
        log.error("Error while accessing resource", e);
        return new ResponseEntity(e.getMessage(), HttpStatus.GATEWAY_TIMEOUT);
    }

    @ExceptionHandler(ConnectionPoolTimeoutException.class)
    public HttpEntity handleConnectionPoolTimeoutException(ConnectionPoolTimeoutException e) {
        log.error("Error on connection pool", e);
        return new ResponseEntity("Try again later", HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public HttpEntity handleAsyncRequestTimeoutException(AsyncRequestTimeoutException e) {
        log.error("Error on async req", e);
        return new ResponseEntity(e.getMessage(), HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler(SimpleTravelServiceException.class)
    public HttpEntity handleSimpleTravelServiceException(SimpleTravelServiceException e) {
        log.error("Error on simple travel service", e);
        return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
