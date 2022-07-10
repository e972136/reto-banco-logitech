package com.patito.banco_test.dto;

import com.patito.banco_test.entity.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class RespuestaItemHistorial {
    Date creadoEn;
    String account;
    BigDecimal amount;
}
