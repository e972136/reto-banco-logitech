package com.patito.banco_test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class SolicitudCliente {

    @NotEmpty
    String name;

    @NotEmpty
    String lastName;

    LocalDate birthday;

    @NotEmpty
    String phone;

    @Email
    String email;

    @NotEmpty
    String address;
}
