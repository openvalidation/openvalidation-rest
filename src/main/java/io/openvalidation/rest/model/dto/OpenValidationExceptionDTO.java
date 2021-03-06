package io.openvalidation.rest.model.dto;

import io.openvalidation.rest.model.dto.astDTO.Range;

public class OpenValidationExceptionDTO {
  private String message;
  private Range range;

  public OpenValidationExceptionDTO() {}

  public OpenValidationExceptionDTO(String message, Range range) {
    this.message = message;
    this.range = range;
  }

  public String getMessage() {
    return message;
  }

  public Range getRange() {
    return range;
  }
}
