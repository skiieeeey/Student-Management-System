package com.sky.studentmanagement.exception;

public class CustomException {

    public static class AttributeException extends Exception{
        public AttributeException(String message){
            super(message);
        }
    }
}
