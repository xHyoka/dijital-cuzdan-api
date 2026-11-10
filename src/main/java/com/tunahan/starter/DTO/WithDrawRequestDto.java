package com.tunahan.starter.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class WithDrawRequestDto {

    @NotNull
    @Positive
    private BigDecimal amount;
}
