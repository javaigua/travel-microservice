package com.klm.casex01.service.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.klm.casex01.service.beans.airports.Airport;
import com.klm.casex01.service.beans.fares.Fares;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "origin",
  "destination",
  "fare"
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirportsFareDetails {

    @JsonProperty("origin")
    private Airport origin;
    @JsonProperty("destination")
    private Airport destination;
    @JsonProperty("fare")
    private Fares fare;

}
