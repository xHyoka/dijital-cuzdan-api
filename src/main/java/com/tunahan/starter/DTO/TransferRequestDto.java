package com.tunahan.starter.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data


public class TransferRequestDto {

    private Integer toAccountId;
    @NotNull
    @Positive
    private BigDecimal amount;
}
