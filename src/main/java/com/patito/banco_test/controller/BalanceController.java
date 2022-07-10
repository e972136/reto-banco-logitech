package com.patito.banco_test.controller;

import com.patito.banco_test.dto.Respuesta;
import com.patito.banco_test.dto.RespuestaItemHistorial;
import com.patito.banco_test.dto.SolicitudCliente;
import com.patito.banco_test.dto.SolicitudTransaccion;
import com.patito.banco_test.entity.Balance;
import com.patito.banco_test.entity.BalanceTransactions;
import com.patito.banco_test.entity.Client;
import com.patito.banco_test.repository.BalanceRepository;
import com.patito.banco_test.repository.BalanceTransactionsRepository;
import com.patito.banco_test.repository.ClientRepository;
import com.patito.banco_test.service.BalanceService;
import com.patito.banco_test.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/banco")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService service;
    private final ClientService clientService;

    @GetMapping("/todos_clientes")
    ResponseEntity<Respuesta> getAllClients(){
        return clientService.traerTodos();
    }

    @PostMapping("/acreditar")
    public ResponseEntity<Respuesta> transaccion(@Valid @RequestBody SolicitudTransaccion solicitudTransaccion, BindingResult result){
        if(result.hasErrors()){
            String errores = this.formatMessage(result);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Respuesta.of("error",errores));
        }
        return service.acreditar(solicitudTransaccion);
    }

    @GetMapping("/historial/{idCliente}")
    public ResponseEntity<Respuesta> obtenerHistorial(@PathVariable(value = "idCliente",required = true) Integer idCliente,
                                                      @RequestParam(value = "account", required = false) String account)
    {
        return service.obtenerHistorial(idCliente,account);
    }

    @PostMapping("/cliente")
    public ResponseEntity<Respuesta> obtenerHistorial(@Valid @RequestBody SolicitudCliente solicitudCliente, BindingResult result){
        if(result.hasErrors()){
            String errores = this.formatMessage(result);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Respuesta.of("error",errores));
        }
        return clientService.guardarCliente(solicitudCliente);
    }

    String formatMessage(BindingResult result){

        String collect = result.getFieldErrors().stream().map(err -> {
            return err.getField() + "->" + err.getDefaultMessage();
        }).collect(Collectors.joining(","));
        return collect;
    }
}
