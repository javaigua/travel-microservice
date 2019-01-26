
package com.klm.casex01.service.beans.airports;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "_embedded",
    "page"
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Airports {

    @JsonProperty("term")
    private String term;
    @JsonProperty("_embedded")
    private Embedded embedded;
    @JsonProperty("page")
    private Page page;

}
