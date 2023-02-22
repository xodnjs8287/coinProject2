package com.example.coinProject.coin.dto;

import com.example.coinProject.coin.domain.Coin;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CoinRequest {
    private String market;
    private String korName;
    private String engName;
    private BigDecimal tradePrice;

    public Coin toCoin() {
        return Coin.builder()
            .market(market)
            .korName(korName)
            .engName(engName)
            .tradePrice(tradePrice)
            .build();
    }
}
