package com.pragma.powerup.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRestaurantAssignmentRequestDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("employeeId")
    private Long employeeId;

    @JsonProperty("restaurantId")
    private Long restaurantId;

    @JsonProperty("ownerId")
    private Long ownerId;
}
