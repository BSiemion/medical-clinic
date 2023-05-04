package com.bsiemion.medical.exception;

import org.springframework.http.HttpStatus;

public class VisitNotFoundException extends MedicalClinicException{
    public VisitNotFoundException(HttpStatus httpStatus){
        super("Visit not found", httpStatus);
    }
}
