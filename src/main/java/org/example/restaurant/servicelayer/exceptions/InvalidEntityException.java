package org.example.restaurant.servicelayer.exceptions;

public class InvalidEntityException extends ServiceLayerException {
    public InvalidEntityException(String message) {
        super(message);
    }
}
