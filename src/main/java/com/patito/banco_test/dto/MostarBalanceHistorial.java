package com.patito.banco_test.dto;

import com.patito.banco_test.entity.Balance;
import com.patito.banco_test.entity.BalanceTransactions;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor(staticName = "of")
public class MostarBalanceHistorial {
    String account;
    BigDecimal balance;
    List<RespuestaItemHistorial> listaTransacciones;
}
