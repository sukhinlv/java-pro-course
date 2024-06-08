package org.example.hw07_product_service.mapper;

import org.example.payment_service.dao.model.generated.PaymentDto;
import org.mapstruct.Mapper;

@Mapper
public interface PaymentMapper {

    org.example.product_service.dao.model.generated.PaymentDto paymentPaymentDtoToPaymentDto(PaymentDto paymentDto);
}
