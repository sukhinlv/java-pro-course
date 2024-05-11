package org.example.hw06_product_service.mapper;

import org.example.payment_service.dao.model.generated.ProductDto;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {

    ProductDto productProductDtoToProductDto(org.example.product_service.dao.model.generated.ProductDto productDto);
}
