package com.bsiemion.medical.handler;

import com.bsiemion.medical.exception.PatientException;
import com.bsiemion.medical.exception.PatientNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PatientExceptionHandler {
    @ExceptionHandler(PatientException.class)
    public ResponseEntity<String> patientExceptionErrorResponse(PatientException patientException){
        return ResponseEntity.status(400).body(patientException.getMessage());
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<String> patientNotFoundExceptionErrorResponse(PatientNotFoundException patientNotFoundException){
        return ResponseEntity.status(404).body(patientNotFoundException.getMessage());
    }
}
