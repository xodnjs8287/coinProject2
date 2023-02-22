package com.example.coinProject.coin.controller;

import com.example.coinProject.coin.dto.CoinResponse;
import com.example.coinProject.coin.service.CoinService;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coin")
@RequiredArgsConstructor
@Slf4j
public class CoinController {

    private final CoinService coinService;

    @GetMapping
    public List<CoinResponse> getMarket() {
        return coinService.getCoinResponse();
    }

    @GetMapping("/{unit}")
    public List<CoinResponse> getCoinPrice(@PathVariable("unit") int unit, @RequestParam(name = "market") String market) {
        return coinService.getCoinPrice(unit, market);
    }
}
