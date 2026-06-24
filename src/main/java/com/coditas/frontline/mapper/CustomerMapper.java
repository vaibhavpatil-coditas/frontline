package com.coditas.frontline.mapper;

import com.coditas.frontline.dto.request.CustomerRequest;
import com.coditas.frontline.dto.response.CustomerResponse;
import com.coditas.frontline.dto.response.UserResponse;
import com.coditas.frontline.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toCustomer(CustomerRequest request);

    CustomerResponse toCustomerResponse(Customer savedCustomer);
}
