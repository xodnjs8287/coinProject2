package com.example.coinProject.coin.dto;

import com.example.coinProject.coin.domain.Coin;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CoinResponse {

    private int coinNo;
    private String market;
    @JsonProperty("korean_name")
    private String korName;
    @JsonProperty("english_name")
    private String engName;
    @JsonProperty("trade_price")
    private BigDecimal tradePrice;

    public CoinResponse(Coin coin) {
        this.coinNo = coin.getCoinNo();
        this.market = coin.getMarket();
        this.korName = coin.getKorName();
        this.engName = coin.getEngName();
        this.tradePrice = coin.getTradePrice();
    }
}
