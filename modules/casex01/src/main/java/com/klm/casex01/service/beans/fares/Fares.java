package com.klm.casex01.service.beans.fares;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "amount",
  "currency",
  "origin",
  "destination"
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fares {

    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("origin")
    private String origin;
    @JsonProperty("destination")
    private String destination;

}