package com.subastas.virtual.exception.custom;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String s) {
        super(s);
    }

    public NotFoundException(String element, int id) {
        super(String.format("We could not find %s with id %d", element, id));
    }
}
