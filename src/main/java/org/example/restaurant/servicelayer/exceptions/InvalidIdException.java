package org.example.restaurant.servicelayer.exceptions;

public class InvalidIdException extends ServiceLayerException {
    public InvalidIdException(String message) {
        super(message);
    }
}
