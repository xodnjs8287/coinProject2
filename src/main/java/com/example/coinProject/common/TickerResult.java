package com.example.coinProject.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class TickerResult {

    private String type;
    private String code;
    @JsonProperty("trade_price")
    private BigDecimal tradePrice;
}
