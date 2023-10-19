package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RegisterRequestDto {
    @NotNull(message = "Nombre es obligatorio")
    private String name;

    @NotNull(message = "Apellido es obligatorio")
    private String lastname;

    @NotNull(message = "Número de identificación es obligatorio")
    @Pattern(regexp = "\\d+(\\d+)?", message = "El numero de identificacion debe ser numerico")
    private String documentNumber;

    @NotNull(message = "Telefono es obligatorio")
    @Size(max = 13, message = "El numero de telefono tiene un maximo de 13 caracteres")
    @Pattern(regexp = "[+]?\\d+(\\d+)?", message = "Formato incorrecto de numero de telefono")
    private String phone;

    @NotNull(message = "Email es obligatorio")
    @Email(message = "Debe tener una direccion valida de Email")
    private String email;

    @NotNull(message = "Contraseña es obligatorio")
    private String password;
}
