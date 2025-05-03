package com.compedia.exceptions;

public class NotValidTokenException extends RuntimeException{
    public NotValidTokenException(String message){
        super(message);
    }
}
