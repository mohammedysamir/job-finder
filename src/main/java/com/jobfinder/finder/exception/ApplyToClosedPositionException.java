package com.jobfinder.finder.exception;

public class ApplyToClosedPositionException extends RuntimeException {
  String prefix = "You cannot apply to a closed position %s";
  String message;

  public ApplyToClosedPositionException(String positionTitle) {
    message = String.format(prefix, positionTitle);
  }

  @Override
  public String getMessage() {
    return message;
  }
}
