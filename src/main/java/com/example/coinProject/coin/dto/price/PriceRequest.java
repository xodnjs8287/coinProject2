package com.example.coinProject.coin.dto.price;

import com.example.coinProject.coin.domain.Coin;
import com.example.coinProject.coin.domain.Price;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PriceRequest {

    private BigDecimal price;

    private String market;

    private LocalDateTime candleDateTime;

    public Price toPrice() {
        return Price.builder().
                price(price)
                .market(market)
                .candleDateTime(candleDateTime)
                .build();


    }


}
