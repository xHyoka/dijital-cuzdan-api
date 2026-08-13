package com.tunahan.starter.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRegisterDto {
    @NotBlank(message = "Kullanıcı adı boş olamaz")
    private String username;

    @NotBlank(message = "Şifre kısmı boş olamaz")
    private String password;

    @NotBlank(message = "TC Kimlik adı boş olamaz")
    private String tcKimlikNo;

    @Email(message = "Geçerli bir email giriniz")
    @NotBlank(message = "Email boş olamaz")
    private String email;
}
