package com.example.coinProject.price.controller;

//import com.example.coinProject.price.service.PriceService;
import com.example.coinProject.coin.domain.Coin;
import com.example.coinProject.coin.dto.coin.CoinResponse;
import com.example.coinProject.coin.service.CoinService;
import com.example.coinProject.price.service.PriceService;
import com.example.coinProject.price.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/price")
public class PriceController {

    private final CoinService coinService;

    private final PriceService priceService;

    private final TradeService tradeService;

    @GetMapping
    public Double getCurrentPrice() {
        return tradeService.getCurrentPrice().doubleValue();
    }

    @GetMapping("/rsi")
    public Map<String,Double> getRsi() {

        return priceService.getAllMarketsRsi();
    }
}
