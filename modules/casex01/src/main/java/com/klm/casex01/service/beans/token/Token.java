package com.klm.casex01.service.beans.token;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "access_token",
  "token_type",
  "expires_in",
  "scope"
})
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")
    private Integer expiresIn;
    @JsonProperty("scope")
    private String scope;

    @Override
    public String toString() {
        int tokenLen = Optional.ofNullable(accessToken).orElse("").length();
        return new StringBuilder()
          .append("Token{")
          .append("accessToken='").append("******").append(tokenLen >= 5 ? accessToken.substring(tokenLen - 5, tokenLen) : "").append('\'')
          .append(", expiresIn=").append(expiresIn)
          .append('}').toString();
    }
}