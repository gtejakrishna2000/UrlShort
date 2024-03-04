package com.example.UrlShort.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddCustomerDto {


   private String username;
   private String password;
   private String role;
   private String email;
   private String first_name;
   private String last_name;

}
