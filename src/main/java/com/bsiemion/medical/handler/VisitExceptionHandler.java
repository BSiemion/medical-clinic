package com.bsiemion.medical.handler;

import com.bsiemion.medical.exception.VisitIllegalDateException;
import com.bsiemion.medical.exception.VisitIsNotAvailableException;
import com.bsiemion.medical.exception.VisitNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class VisitExceptionHandler {
    @ExceptionHandler(VisitIllegalDateException.class)
    public ResponseEntity<String> visitIllegalDateExceptionResponse(VisitIllegalDateException visitIllegalDateException){
        return ResponseEntity.status(400).body(visitIllegalDateException.getMessage());
    }

    @ExceptionHandler(VisitNotFoundException.class)
    public ResponseEntity<String> visitNotFoundExceptionResponse(VisitNotFoundException visitNotFoundException){
        return ResponseEntity.status(404).body(visitNotFoundException.getMessage());
    }

    @ExceptionHandler(VisitIsNotAvailableException.class)
    public ResponseEntity<String> visitIsNotAvailableExceptionResponse(VisitIsNotAvailableException visitIsNotAvailableException){
        return ResponseEntity.status(400).body(visitIsNotAvailableException.getMessage());
    }
}
