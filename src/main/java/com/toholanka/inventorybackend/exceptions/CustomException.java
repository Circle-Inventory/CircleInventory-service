package com.toholanka.inventorybackend.exceptions;

public class CustomException extends IllegalArgumentException{
    public CustomException(String msg) {
        super(msg);
    }
}
