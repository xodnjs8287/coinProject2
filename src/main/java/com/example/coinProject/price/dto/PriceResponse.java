package com.example.coinProject.price.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PriceResponse {

    @JsonProperty("trade_price")
    BigDecimal price;

    private Long timestamp;

}
