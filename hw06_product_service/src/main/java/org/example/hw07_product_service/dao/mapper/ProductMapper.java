package org.example.hw07_product_service.dao.mapper;

import org.example.hw07_product_service.dao.model.Product;
import org.example.product_service.dao.model.generated.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    ProductDto productToProductDto(Product product);

    List<ProductDto> productsToProductDtos(List<Product> products);
}
