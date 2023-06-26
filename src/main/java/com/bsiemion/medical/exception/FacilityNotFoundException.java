package com.bsiemion.medical.exception;

import org.springframework.http.HttpStatus;

public class FacilityNotFoundException extends MedicalClinicException{
    public FacilityNotFoundException(HttpStatus httpStatus){
        super("Facility not found", httpStatus);
    }
}
