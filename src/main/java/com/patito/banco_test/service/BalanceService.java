package com.patito.banco_test.service;

import com.patito.banco_test.dto.MostarBalanceHistorial;
import com.patito.banco_test.dto.Respuesta;
import com.patito.banco_test.dto.RespuestaItemHistorial;
import com.patito.banco_test.dto.SolicitudTransaccion;
import com.patito.banco_test.entity.Balance;
import com.patito.banco_test.entity.BalanceTransactions;
import com.patito.banco_test.entity.Client;
import com.patito.banco_test.repository.BalanceRepository;
import com.patito.banco_test.repository.BalanceTransactionsRepository;
import com.patito.banco_test.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final ClientRepository clientRepository;
    private final BalanceTransactionsRepository balanceTransactionsRepository;
    private final BalanceRepository balanceRepository;

    public ResponseEntity<Respuesta> acreditar(SolicitudTransaccion solicitudTransaccion ){
        Client client = clientRepository.findById(solicitudTransaccion.getClienteId()).orElse(null);

        if(client==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Respuesta.of("error","Cliente no existe"));
        }
        Balance balance = balanceRepository.findByaccount(solicitudTransaccion.getAccount());
        if(balance!=null){
            if(balance.getClient().getId()!=client.getId())
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Respuesta.of("error","No coincide cuenta con cliente"));
            }
        }else{
            balance = Balance.builder()
                    .balance(BigDecimal.ZERO)
                    .client(client)
                    .creadoEn(new Date())
                    .account(solicitudTransaccion.getAccount()).build();

            balance = balanceRepository.save(balance);
        }
        BigDecimal monto = balance.getBalance();
        monto = monto.add(solicitudTransaccion.getAmount());
        balance.setBalance(monto);
        balance = balanceRepository.save(balance);
        BalanceTransactions balanceTransactions = BalanceTransactions.builder()
                .client(client)
                .amount(solicitudTransaccion.getAmount())
                .account(balance.getId())
                .creadoEn(new Date())
                .build();
        balanceTransactionsRepository.save(balanceTransactions);
        return ResponseEntity.ok(Respuesta.of("success",balance));
    }

    public ResponseEntity<Respuesta> obtenerHistorial(Integer idCliente,String account){
        Client client = clientRepository.findById(idCliente).orElse(null);
        if(client==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Respuesta.of("error","Cliente no existe"));
        }
        List<BalanceTransactions> all = balanceTransactionsRepository.findByclient(client);
        Map<Balance, List<RespuestaItemHistorial>> collect = all.stream()
//                .filter(x -> x.getClient().getId().equals(idCliente))
                .map(r -> new RespuestaItemHistorial(r.getCreadoEn(), balanceRepository.findById(r.getAccount()).get().getAccount(), r.getAmount()))
                .collect(Collectors.groupingBy(r -> balanceRepository.findByaccount(r.getAccount())));


        Map<String,Object> salida = new TreeMap<>();
        salida.put("Cliente",client);

        List<MostarBalanceHistorial> cuentas= new ArrayList<>();
        for(Balance balances: collect.keySet()){
            if(account==null) {
                MostarBalanceHistorial of = MostarBalanceHistorial.of(balances.getAccount(), balances.getBalance(), collect.get(balances));
                cuentas.add(of);
            }else {
                if(balances.getAccount().equals(account)){
                    MostarBalanceHistorial of = MostarBalanceHistorial.of(balances.getAccount(), balances.getBalance(), collect.get(balances));
                    cuentas.add(of);
                }
            }
        }
        salida.put("Cuentas ",cuentas);

//        if(account!=null){
//            List<RespuestaItemHistorial> respuestaItemHistorials = collect.get(account);
//            collect.clear();
//            collect.put(account,respuestaItemHistorials);
//        }
//        if(collect.isEmpty()){
//            return ResponseEntity.ok(Respuesta.of("error","No hay datos"));
//        }

//        salida.put("Transacciones",collect);
        return ResponseEntity.ok(Respuesta.of("success",salida));
    }
}
