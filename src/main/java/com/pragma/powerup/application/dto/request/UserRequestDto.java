package com.pragma.powerup.application.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
public class UserRequestDto {
    @NotNull(message = "El nombre es obligatorio")
    private String name;

    @NotNull(message = "El apellido es obligatorio")
    private String lastName;

    @NotNull(message = "El número de documento de identidad es obligatorio")
    private String numberId;

    @NotNull(message = "El número de celular es obligatorio")
    @Size(max = 13, message = "El número de celular puede tener máximo 13 caracteres")
    @Pattern(regexp = "[+]?\\d+([\\\\s-]?\\\\d+)?", message = "Está usando un formato incorrecto para el número de celular")
    private String numberPhone;


    private Date dateBirth;

    @NotNull(message = "La dirección de correo es obligatoria")
    @Email(message = "Debe tener una dirección de correo valida ")
    private String email;

    @NotNull(message = " La Contraseña es obligatoria")
    private String password;

    private Long roleId;
}
