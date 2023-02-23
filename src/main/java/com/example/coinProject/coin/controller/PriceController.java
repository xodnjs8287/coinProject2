package com.example.coinProject.coin.controller;

import com.example.coinProject.coin.dto.price.PriceResponse;
import com.example.coinProject.coin.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;


    @GetMapping("/savePrice")
    public List<PriceResponse> getCoinPrice() {
        return priceService.getCoinPrice();
    }
}
