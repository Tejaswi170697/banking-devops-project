package com.bank.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequest {

    private AddressRequest address;

   @NotBlank(message = "Name is required")
    private String name;

   @Email(message = "Invalid email")
   @NotBlank(message = "Email is required")
    private String email;

   @NotBlank(message = "Phone is required")
   @Pattern(
           regexp = "^[0-9]{10}$",
           message = "Phone number must contain exactly 10 digits"
   )
    private String phone;
    private List<AccountRequest> accounts;
//constructor
//    public CustomerRequest() {
//    }
//constructor
//    public CustomerRequest(String name, String email, String phone) {
//        this.name = name;
//        this.email = email;
//        this.phone = phone;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
}