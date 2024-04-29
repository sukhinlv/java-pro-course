package org.example.dao.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProductType {
    ACCOUNT(1, "Счет"),
    CARD(2, "Карта");

    private final short id;
    private final String name;

    ProductType(int accountTypeId, String accountTypeName) {
        this.id = (short) accountTypeId;
        this.name = accountTypeName;
    }

    @JsonValue
    public short getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @JsonCreator
    public static ProductType fromValue(short id) {
        for (ProductType value : ProductType.values()) {
            if (value.getId() == id) {
                return value;
            }
        }
        throw new IllegalArgumentException(
                String.format("id %d for %s not supported", id,
                        Thread.currentThread().getStackTrace()[1].getClassName()));
    }

    public static ProductType fromValue(String name) {
        for (ProductType value : ProductType.values()) {
            if (value.getName().equalsIgnoreCase(name)) {
                return value;
            }
        }
        throw new IllegalArgumentException(
                String.format("name %s for %s not supported", name,
                        Thread.currentThread().getStackTrace()[1].getClassName()));
    }
}
