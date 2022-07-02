package com.company.exp;

public class AlreadyExist extends RuntimeException{

    public AlreadyExist(String message) {
        super(message);
    }
}
