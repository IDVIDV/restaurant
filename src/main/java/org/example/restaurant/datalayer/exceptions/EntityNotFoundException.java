package org.example.restaurant.datalayer.exceptions;

public class EntityNotFoundException extends DataLayerException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
