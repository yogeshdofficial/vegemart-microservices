package io.github.yogeshdofficial.product_service;

import io.github.yogeshdofficial.product_service.product.exceptions.ProductNotFoundException;
import io.github.yogeshdofficial.product_service.shared.dtos.ApiErrorDto;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
  private static final String SERVICE_NAME = "product service";

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ApiErrorDto> handleUnhandledException(Exception ex, WebRequest request) {
    return new ResponseEntity<>(
        (ApiErrorDto.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .errors(List.of(ex.getMessage()))
            .serviceName(SERVICE_NAME)
            .timestamp(Instant.now())
            .build()),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      @NonNull MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
    // log.error("");
    List<String> errors = new ArrayList<>();
    for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
      errors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
    }
    for (ObjectError objectError : ex.getBindingResult().getGlobalErrors()) {
      errors.add(objectError.getObjectName() + ": " + objectError.getDefaultMessage());
    }
    return new ResponseEntity<>(
        (ApiErrorDto.builder()
            .status(status)
            .errors(errors)
            .serviceName(SERVICE_NAME)
            .timestamp(Instant.now())
            .build()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ProductNotFoundException.class)
  protected ResponseEntity<ApiErrorDto> handleProductNotFoundException(
      ProductNotFoundException ex, WebRequest request) {
    return new ResponseEntity<>(
        (ApiErrorDto.builder()
            .status(HttpStatus.NOT_FOUND)
            .errors(List.of(ex.getMessage()))
            .serviceName(SERVICE_NAME)
            .timestamp(Instant.now())
            .build()),
        HttpStatus.NOT_FOUND);
  }
  // @ExceptionHandler(Exception.class)
  // ProblemDetail handleUnhandledException(final Exception ex) {
  // ProblemDetail problemDetail =
  // ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
  // ex.getMessage());
  // problemDetail.setTitle("Internal server error");
  // problemDetail.setProperty("timestanp", Instant.now());
  // problemDetail.setProperty("service", SERVICE_NAME);
  // log.error(ex.getMessage());
  // return problemDetail;
  // }

  // @ExceptionHandler(ProductNotFoundException.class)
  // ResponseEntity<ApiErrorDto> handleProductNotFoundException(final
  // ProductNotFoundException ex) {
  // // ProblemDetail problemDetail =
  // // ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
  // // problemDetail.setTitle("Not found");
  // // problemDetail.setProperty("timestanp", Instant.now());
  // // log.error(ex.getMessage());
  // // problemDetail.setProperty("service", SERVICE_NAME);
  // // return problemDetail;
  // }
}
