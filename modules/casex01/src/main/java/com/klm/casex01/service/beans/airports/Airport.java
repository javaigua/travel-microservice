package com.klm.casex01.service.beans.airports;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "code",
    "name",
    "description",
    "coordinates",
    "parent"
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Airport {

    @JsonProperty("code")
    private String code;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("coordinates")
    private Coordinates coordinates;
    @JsonProperty("parent")
    private Parent parent;

}
