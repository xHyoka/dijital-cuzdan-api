package com.tunahan.starter.DTO;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserPasswordDto {
    @NotBlank(message = "Eski şifre kısmı boş olamaz")
    private String oldPassword;

    @NotBlank(message = "Yeni şifre kısmı boş olamaz")
    private String newPassword;
}
