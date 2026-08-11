package com.tunahan.starter.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Data

public class AccountResponseDto {
    private Integer id;
    private BigDecimal balance;
    private String AccountNumber;

}
