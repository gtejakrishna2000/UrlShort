package com.example.UrlShort.TransFormer;

import com.example.UrlShort.Dtos.AddCustomerDto;
import com.example.UrlShort.Models.Customer;
import lombok.Builder;

@Builder
public class CustomerTransformer {
    public static Customer BuildCustomer(AddCustomerDto addCustomerDto, String customerId, String password) {

        Customer customer=Customer.builder().customerId(customerId).first_name(addCustomerDto.getFirst_name())
                . last_name(addCustomerDto.getLast_name()).email(addCustomerDto.getEmail()).role(addCustomerDto.getRole())
                .username(addCustomerDto.getUsername()).password(password).build();
      return customer;
    }

}
