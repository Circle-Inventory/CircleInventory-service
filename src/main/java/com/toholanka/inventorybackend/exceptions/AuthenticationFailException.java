package com.toholanka.inventorybackend.exceptions;

public class AuthenticationFailException extends IllegalArgumentException  {
    public AuthenticationFailException(String msg){
        super(msg);
    }
}
