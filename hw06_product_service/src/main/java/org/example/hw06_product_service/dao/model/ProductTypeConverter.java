package org.example.hw06_product_service.dao.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ProductTypeConverter implements AttributeConverter<ProductType, Short> {

    @Override
    public Short convertToDatabaseColumn(ProductType productType) {
        return productType.getId();
    }

    @Override
    public ProductType convertToEntityAttribute(Short value) {
        return ProductType.fromValue(value);
    }
}
