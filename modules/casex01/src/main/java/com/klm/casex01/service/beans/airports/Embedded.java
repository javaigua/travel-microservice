
package com.klm.casex01.service.beans.airports;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "locations"
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Embedded {

    @JsonProperty("locations")
    private List<Airport> locations;

}
