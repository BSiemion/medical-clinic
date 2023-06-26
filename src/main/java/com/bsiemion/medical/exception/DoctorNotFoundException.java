package com.bsiemion.medical.exception;

import org.springframework.http.HttpStatus;

public class DoctorNotFoundException extends MedicalClinicException{
    public DoctorNotFoundException(HttpStatus httpStatus){
        super("Doctor not found", httpStatus);
    }
}
