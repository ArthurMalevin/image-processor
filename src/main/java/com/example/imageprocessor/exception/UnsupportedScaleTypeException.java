package com.example.imageprocessor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UnsupportedScaleTypeException extends RuntimeException {

  public UnsupportedScaleTypeException(String message) {
    super(message);
  }
}
