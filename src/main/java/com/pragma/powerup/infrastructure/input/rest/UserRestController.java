package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.AuthenticationRequestDto;
import com.pragma.powerup.application.dto.request.EmployeeRestaurantAssignmentRequestDto;
import com.pragma.powerup.application.dto.request.UserRequestDto;
import com.pragma.powerup.application.dto.response.JwtResponseDto;
import com.pragma.powerup.application.dto.response.ResponseDto;
import com.pragma.powerup.application.dto.response.UserResponseDto;
import com.pragma.powerup.application.handler.IJwtHandler;
import com.pragma.powerup.application.handler.IUserHandler;
import com.pragma.powerup.common.exception.LegalAgeException;
import com.pragma.powerup.common.exception.OwnerNotAssociatedException;
import com.pragma.powerup.common.exception.RepeatUserException;
import com.pragma.powerup.common.exception.UnauthorizedRoleException;
import com.pragma.powerup.infrastructure.input.rest.Plazoleta.IPlazoletaFeignClient;
import com.pragma.powerup.infrastructure.configuration.FeingClientInterceptorImp;
import io.swagger.v3.oas.annotations.*;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionStatus;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@OpenAPIDefinition(
        info = @Info(
                title = "Documentación de la API del microservicios usuarios *nuevo cambio*",
                version = "1.0",
                description = "Esta API permite registrar nuevos usuarios en el sistema, validando previamente si quien intenta hacer el registro cumple los requisitos. Los usuarios pueden ser propietarios, empleados o clientes."
        )
)
public class UserRestController {
    private final IUserHandler userHandler;
    private final IPlazoletaFeignClient plazoletaFeignClient;
    private final IJwtHandler jwtHandler;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Operation(
            summary = "Iniciar sesión de usuario",
            operationId = "login",
            description = "Permite que un usuario inicie sesión en el sistema proporcionando sus credenciales." +
                    "Autenticación: El endpoint /login utiliza la autenticación de nombre de usuario y contraseña para autenticar a los usuarios.\n" +
                    "Autorización: El endpoint /login utiliza el token JWT para autorizar a los usuarios a acceder a los recursos protegidos.\n" +
                    "Encriptación: Las contraseñas de los usuarios se almacenan encriptadas en la base de datos usando el algoritmo BCrypt.\n" +
                    "Control de acceso: El acceso a la API se limita a los usuarios autorizados usando el token JWT.\n" +
                    "Monitoreo y respuesta a incidentes: El servidor monitorea el tráfico de la API para detectar ataques. Si se detecta un ataque, el servidor responde tomando medidas para mitigar el ataque." +
                    "+\n **Ejemplo de Solicitud:**",
            security = {
                    @SecurityRequirement(
                            name = "bearerAuth"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario autenticado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta. Verifique los datos enviados.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas. El usuario no ha sido autenticado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(
            @Parameter(name = "authenticationRequestDto", description = "Este parámetro representa la implementación de la clase AuthenticationRequestDto, la cual tiene dos atributos de tipo string: email y password.", schema = @Schema(implementation = AuthenticationRequestDto.class))
            @RequestBody AuthenticationRequestDto authenticationRequestDto) {
        return ResponseEntity.ok(userHandler.login(authenticationRequestDto));
    }

    @Operation(
            summary = "Agregar un nuevo propietario",
            operationId = "registerOwner",
            description = "Permite que un usuario con rol de administrador registre un nuevo usuario con rol de propietario.\n" +
                    "Autenticación\n" +
                    "Requiere un token de acceso (bearer token) en la cabecera de la solicitud para identificar al usuario.\n" +
                    "Los usuarios deben iniciar sesión y obtener un token válido para acceder.\n" +
                    "Autorización\n" +
                    "Solo los usuarios con el rol de \"ADMINISTRADOR\" pueden utilizar este endpoint.\n" +
                    "Se verifica la autorización antes de procesar la solicitud." +
                    "\n **Ejemplo de Solicitud:**",
            security = {
                    @SecurityRequirement(
                            name = "bearerAuth"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Propietario registrado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación de datos. Verifique los datos enviados.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado. No tiene permisos para realizar esta acción.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Prohibido. No tiene los permisos adecuados para acceder a esta funcionalidad.", content = @Content),
            @ApiResponse(responseCode = "409", description = "El usuario ya existe.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor. Contacte al equipo de soporte.", content = @Content)
    })
    @PostMapping("/register_owner")
    public ResponseEntity<ResponseDto> registerOwner(
            @Parameter(
                    name = "userRequestDto",
                    description = "Este parámetro representa la implementación de la clase UserRequestDto, que contiene los datos del nuevo usuario a registrar.",
                    schema = @Schema(implementation = UserRequestDto.class)
            )
            @Valid @RequestBody UserRequestDto userRequestDto, BindingResult bindingResult) {
        ResponseDto responseDto = new ResponseDto();
        if (userRequestDto.getDateBirth() == null) {
            responseDto.setError(true);
            responseDto.setMessage("La fecha de nacimiento es obligatoria.");
            responseDto.setData(null);
            return ResponseEntity.badRequest().body(responseDto);
        }
        return register(userRequestDto, bindingResult, "PROPIETARIO", "ADMINISTRADOR");
    }

    @Operation(
            summary = "Agregar un nuevo cliente",
            operationId = "registerClient",
            description = "Permite que cualquier usuario, sin que sea obligatorio estar logeado, pueda registrar un usuario con rol cliente.\n" +
                    "\n**Ejemplo de Solicitud:**"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente registrado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación de datos. Verifique los datos enviados.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado. No tiene permisos para realizar esta acción.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Prohibido. No tiene los permisos adecuados para acceder a esta funcionalidad.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Error de validación de datos. Los datos enviados no son correctos.", content = @Content),
            @ApiResponse(responseCode = "409", description = "El usuario ya existe. Ya hay un usuario registrado con esta información.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor. Contacte al equipo de soporte.", content = @Content)
    })
    @PostMapping("/register_client")
    public ResponseEntity<ResponseDto> registerClient(
            @Parameter(
                    name = "userRequestDto",
                    description = "Este parámetro representa la implementación de la clase UserRequestDto, que contiene los datos del nuevo usuario a registrar.",
                    schema = @Schema(implementation = UserRequestDto.class)
            )
            @Valid @RequestBody UserRequestDto userRequestDto, BindingResult bindingResult) {
        userRequestDto.setRoleId(4L);
        return register(userRequestDto, bindingResult, "CLIENTE", "");
    }
    @Operation(
            summary = "Agregar un nuevo empleado",
            operationId = "registerEmployee",
            description = "Permite que un usuario con rol de propietario registre un nuevo usuario con rol de empleado, se valida que el empleado quede asociado al restaurante del propietario.\n" +
                    "Autenticación\n" +
                    "Requiere un token de acceso (bearer token) en la cabecera de la solicitud para identificar al usuario.\n" +
                    "Los usuarios deben iniciar sesión y obtener un token válido para acceder.\n" +
                    "Autorización\n" +
                    "Solo los usuarios con el rol de \"PROPIETARIO\" pueden utilizar este endpoint.\n" +
                    "Se verifica la autorización antes de procesar la solicitud." +
                    "\n **Ejemplo de Solicitud:**",
            security = {
                    @SecurityRequirement(
                            name = "bearerAuth"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado registrado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación de datos. Verifique los datos enviados.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado. No tiene permisos para realizar esta acción.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Prohibido. No tiene los permisos adecuados para acceder a esta funcionalidad.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Error de validación de datos. Los datos enviados no son correctos.", content = @Content),
            @ApiResponse(responseCode = "409", description = "El usuario ya existe. Ya hay un usuario registrado con esta información.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor. Contacte al equipo de soporte.", content = @Content)
    })
    @PostMapping("/register_employee")
    public ResponseEntity<ResponseDto> registerEmployee(
            @Parameter(
                    name = "userRequestDto",
                    description = "Este parámetro representa la implementación de la clase UserRequestDto, que contiene los datos del nuevo usuario a registrar.",
                    schema = @Schema(implementation = UserRequestDto.class)
            )
            @Valid @RequestBody UserRequestDto userRequestDto, BindingResult bindingResult) {
        userRequestDto.setRoleId(3L);
        return register(userRequestDto, bindingResult, "EMPLEADO", "PROPIETARIO");
    }

    private ResponseEntity<ResponseDto> register(UserRequestDto userRequestDto, BindingResult bindingResult, String roleRequest, String authorizedRole) {

        ResponseDto responseDto = new ResponseDto();
        if (!roleRequest.equals("CLIENTE")) {
            if (!getAuthorizedRole(authorizedRole)) {
                return handleBadRequest(responseDto, "Usted no tiene autorización para realizar este registro ");
            }
        }

        if (("PROPIETARIO".equals(roleRequest) && userRequestDto.getRoleId() != 2) ||
                ("EMPLEADO".equals(roleRequest) && userRequestDto.getRoleId() != 3)) {
            return handleBadRequest(responseDto, "No tiene permitido crear un usuario con el rol ingresado");
        }

        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult);
        }
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            UserResponseDto userResponseDto = userHandler.register(userRequestDto);
            if (roleRequest.equals("EMPLEADO")) {
                saveEmployeeRestaurantAssignmentRequestDto(userResponseDto);
            }
            transactionManager.commit(transactionStatus);
            responseDto.setError(false);
            responseDto.setMessage("Usuario resgistrado con exito");
            responseDto.setData(userResponseDto);

        } catch (OwnerNotAssociatedException o) {
            transactionManager.rollback(transactionStatus);
            return handleBadRequest(responseDto, "No se encontró ningún restaurante asociado al propietario logeado");
        } catch (LegalAgeException l) {
            return handleBadRequest(responseDto, "El usuario es menor de edad");
        } catch (RepeatUserException r) {
            return handleBadRequest(responseDto, "Ya existe un usuario registrado con este email");
        } catch (UnauthorizedRoleException a) {
            return handleBadRequest(responseDto, "Rol no autorizado para realizar el registro");
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
            return handleBadRequest(responseDto, "Error del servidor");
        }
        return ResponseEntity.ok(responseDto);
    }

    private ResponseEntity<ResponseDto> handleValidationErrors(BindingResult bindingResult) {
        List<String> errors = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ResponseDto responseDto = new ResponseDto();
        responseDto.setError(true);
        responseDto.setMessage("Error en las validaciones al crear un plato");
        responseDto.setData(errors);

        return ResponseEntity.badRequest().body(responseDto);
    }

    private ResponseEntity<ResponseDto> handleBadRequest(ResponseDto responseDto, String errorMessage) {
        responseDto.setError(true);
        responseDto.setMessage(errorMessage);
        responseDto.setData(null);

        return ResponseEntity.badRequest().body(responseDto);
    }

    private boolean getAuthorizedRole(@PathVariable String authorizedRole) {
        String tokenHeader = FeingClientInterceptorImp.getBearerTokenHeader();
        String[] split = tokenHeader.split("\\s+");
        String ownerEmail = jwtHandler.extractUserName(split[1]);
        UserResponseDto ownerResponse = userHandler.getByEmail(ownerEmail);
        return ownerResponse.getRole().getName().equals(authorizedRole);
    }

    private ResponseEntity<ResponseDto> saveEmployeeRestaurantAssignmentRequestDto(UserResponseDto userResponseDto) {
        Long restaurantId;
        String tokenHeader = FeingClientInterceptorImp.getBearerTokenHeader();
        String[] split = tokenHeader.split("\\s+");
        String ownerEmail = jwtHandler.extractUserName(split[1]);
        UserResponseDto ownerResponse = userHandler.getByEmail(ownerEmail);
        EmployeeRestaurantAssignmentRequestDto employeeRestaurantAssignmentRequestDto = new EmployeeRestaurantAssignmentRequestDto();
        try {
            restaurantId = plazoletaFeignClient.getIdRestaurantByOwnerId(ownerResponse.getId());
        } catch (Exception e) {
            throw new OwnerNotAssociatedException();
        }
        employeeRestaurantAssignmentRequestDto.setRestaurantId(restaurantId);
        employeeRestaurantAssignmentRequestDto.setEmployeeId(userResponseDto.getId());
        employeeRestaurantAssignmentRequestDto.setOwnerId(ownerResponse.getId());
        return plazoletaFeignClient.saveRestaurantEmployee(employeeRestaurantAssignmentRequestDto);


    }

}