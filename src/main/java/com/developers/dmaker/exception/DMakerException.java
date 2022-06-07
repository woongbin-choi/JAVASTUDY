package com.developers.dmaker.exception;

import lombok.Getter;

@Getter
public class DMakerException extends RuntimeException{
  private DMakerErrorCode dMakerErrorCode;
  private String detailMessage;
}
