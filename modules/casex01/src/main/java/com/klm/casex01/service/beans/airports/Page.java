
package com.klm.casex01.service.beans.airports;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "size",
    "totalElements",
    "totalPages",
    "number"
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page {

    @JsonProperty("size")
    private Integer size;
    @JsonProperty("totalElements")
    private Integer totalElements;
    @JsonProperty("totalPages")
    private Integer totalPages;
    @JsonProperty("number")
    private Integer number;

}
