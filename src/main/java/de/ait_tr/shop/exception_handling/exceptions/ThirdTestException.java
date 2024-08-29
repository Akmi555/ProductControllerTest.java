package de.ait_tr.shop.exception_handling.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class ThirdTestException extends RuntimeException{
    public ThirdTestException(String message) {
        super(message);
    }
}
