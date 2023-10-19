package com.pragma.powerup.infrastructure.input.rest.Plazoleta;

import com.pragma.powerup.application.dto.request.EmployeeRestaurantAssignmentRequestDto;
import com.pragma.powerup.application.dto.response.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "Plazoleta", path = "/api/v1/employeeRestaurant", url = "http://localhost:8082")
public interface IPlazoletaFeignClient {
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> saveRestaurantEmployee(@RequestBody EmployeeRestaurantAssignmentRequestDto employeeRestaurantAssignmentRequestDto);
    @GetMapping("/IdRestaurantByOwnerId/{ownerId}")
    public Long getIdRestaurantByOwnerId(@PathVariable Long ownerId);


}
