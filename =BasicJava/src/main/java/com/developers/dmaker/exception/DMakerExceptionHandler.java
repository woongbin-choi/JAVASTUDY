package com.developers.dmaker.exception;

import com.developers.dmaker.dto.DMakerErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

// 전역 exception handler
@Slf4j
@RestControllerAdvice
public class DMakerExceptionHandler {

  @ResponseStatus(value = HttpStatus.CONFLICT)
  @ExceptionHandler(DMakerException.class)
  public DMakerErrorResponse handleException (DMakerException e, HttpServletRequest request) {
    log.error("errorCode: {}, url: {}, message: {}",
      e.getDMakerErrorCode(), request.getRequestURL(), e.getDetailMessage());

    return DMakerErrorResponse.builder()
      .errorCode(e.getDMakerErrorCode())
      .errorMessage(e.getDetailMessage())
      .build();
  }

  @ExceptionHandler(value = {
    HttpRequestMethodNotSupportedException.class,
    MethodArgumentNotValidException.class
  })
  public DMakerErrorResponse handleBadRequest(Exception e, HttpServletRequest request){
    log.error(" url: {}, message: {}",
       request.getRequestURL(), e.getMessage());

    return DMakerErrorResponse.builder()
      .errorCode(DMakerErrorCode.INVALID_REQUEST)
      .errorMessage(DMakerErrorCode.INVALID_REQUEST.getMessage())
      .build();
  }

  @ExceptionHandler(Exception.class)
  public DMakerErrorResponse handleException(Exception e, HttpServletRequest request){
    log.error(" url: {}, message: {}",
      request.getRequestURL(), e.getMessage());

    return DMakerErrorResponse.builder()
      .errorCode(DMakerErrorCode.INTERNAL_SERVER_ERROR)
      .errorMessage(DMakerErrorCode.INTERNAL_SERVER_ERROR.getMessage())
      .build();
  }
}
