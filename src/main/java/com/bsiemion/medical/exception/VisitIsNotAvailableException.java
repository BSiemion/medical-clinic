package com.bsiemion.medical.exception;

import org.springframework.http.HttpStatus;

public class VisitIsNotAvailableException extends MedicalClinicException{
    public VisitIsNotAvailableException(String message, HttpStatus httpStatus){
        super(message, httpStatus);
    }
}
