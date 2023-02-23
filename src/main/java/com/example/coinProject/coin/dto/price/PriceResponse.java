package com.example.coinProject.coin.dto.price;

import com.example.coinProject.coin.domain.Price;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PriceResponse {

    @JsonProperty("trade_price")
    private BigDecimal price;

    private String market;

    @JsonProperty("candle_date_time_kst")
    private LocalDateTime candleDateTime;

    public PriceResponse(Price price) {
        this.price = price.getPrice();
        this.market = price.getMarket();
        this.candleDateTime = price.getCandleDateTime();
    }

    public String getMarket(String market) {
        return this.market = market;
    }
}
