package com.tunahan.starter.DTO;

import com.tunahan.starter.model.TransactionStatus;
import com.tunahan.starter.model.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponseDto {
    private Integer id;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionStatus status;
    private LocalDateTime timestamp;
    private Integer fromAccountId;  // Account objesi değil, sadece ID göster
    private Integer toAccountId;
}
