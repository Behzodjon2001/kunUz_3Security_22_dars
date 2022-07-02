package com.company.exp;

public class AlreadyExistPhone extends RuntimeException {
    public AlreadyExistPhone(String massage) {
        super(massage);
    }
}
