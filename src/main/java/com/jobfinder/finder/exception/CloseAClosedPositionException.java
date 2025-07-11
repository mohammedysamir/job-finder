package com.jobfinder.finder.exception;

public class CloseAClosedPositionException extends RuntimeException {
  String prefix = "You cannot close an already closed position %s";
  String message;

  public CloseAClosedPositionException(String positionTitle) {
    message = String.format(prefix, positionTitle);
  }

  @Override
  public String getMessage() {
    return message;
  }
}
