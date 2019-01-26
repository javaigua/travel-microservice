package com.klm.casex01.service;

import org.springframework.core.NestedRuntimeException;

public class SimpleTravelServiceException extends NestedRuntimeException {

    private static final long serialVersionUID = -4084444984163796577L;

    public SimpleTravelServiceException(String msg) {
        super(msg);
    }

    public SimpleTravelServiceException(String msg, Throwable ex) {
        super(msg, ex);
    }

}
