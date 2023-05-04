package com.bsiemion.medical.exception;

import org.springframework.http.HttpStatus;

public class PatientNotFoundException extends PatientException{
    public PatientNotFoundException(HttpStatus httpStatus) {
        super("Patient not found", httpStatus);
    }
}
