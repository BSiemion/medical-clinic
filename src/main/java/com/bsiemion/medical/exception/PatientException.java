package com.bsiemion.medical.exception;

import org.springframework.http.HttpStatus;

public class PatientException extends MedicalClinicException{
    public PatientException(String message, HttpStatus httpStatus){
        super(message, httpStatus);
    }
}
