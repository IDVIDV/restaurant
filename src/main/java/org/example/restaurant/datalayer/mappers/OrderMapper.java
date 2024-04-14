package org.example.restaurant.datalayer.mappers;

public class OrderMapper {
    private static final OrderMapper INSTANCE = new OrderMapper();

    public static OrderMapper getInstance() {
        return INSTANCE;
    }
    private OrderMapper() {}
}
