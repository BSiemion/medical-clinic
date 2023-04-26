package com.bsiemion.medical.exception;

public class VisitNotFoundException extends RuntimeException{
    public VisitNotFoundException(){
        super("Visit not found");
    }
}
