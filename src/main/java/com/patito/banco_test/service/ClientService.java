package com.patito.banco_test.service;

import com.patito.banco_test.dto.Respuesta;
import com.patito.banco_test.dto.SolicitudCliente;
import com.patito.banco_test.entity.Client;
import com.patito.banco_test.repository.ClientRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository repository;

    public ResponseEntity<Respuesta> guardarCliente(SolicitudCliente solicitudCliente){
        Client client = Client.builder()
                .name(solicitudCliente.getName())
                .birthday(solicitudCliente.getBirthday())
                .phone(solicitudCliente.getPhone())
                .email(solicitudCliente.getEmail())
                .address(solicitudCliente.getAddress())
                .build();
        client=repository.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(Respuesta.of("success",client));

    }

    public ResponseEntity<Respuesta> traerTodos() {
        List<Client> all = repository.findAll();
        if(all.isEmpty()){
            return ResponseEntity.ok(Respuesta.of("error","no hay clientes"));
        }
        return ResponseEntity.ok(Respuesta.of("success",all));
    }
}
