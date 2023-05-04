package com.bsiemion.medical.exception;

import org.springframework.http.HttpStatus;

public class VisitIllegalDateException extends MedicalClinicException{
    public VisitIllegalDateException(String message, HttpStatus httpStatus){
        super(message, httpStatus);
    }
}
