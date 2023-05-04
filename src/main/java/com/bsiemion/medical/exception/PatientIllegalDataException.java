package com.bsiemion.medical.exception;

import org.springframework.http.HttpStatus;

public class PatientIllegalDataException extends PatientException{

    public PatientIllegalDataException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
