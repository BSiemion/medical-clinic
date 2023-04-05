package com.bsiemion.medical.exception;

public class PatientNotFoundException extends PatientException{
    public PatientNotFoundException() {
        super("Patient not found");
    }
}
