package com.example.coinProject.coin.service;


import com.example.coinProject.coin.domain.Coin;
import com.example.coinProject.coin.domain.Price;
import com.example.coinProject.coin.dto.price.PriceRequest;
import com.example.coinProject.coin.dto.price.PriceResponse;
import com.example.coinProject.coin.repository.CoinRepository;
import com.example.coinProject.coin.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PriceService {

    private final PriceRepository priceRepository;

    private final CoinRepository coinRepository;

    private final Feign feign;

    @Transactional
    public List<PriceResponse> getCoinPrice() {


        List<Price> prices = new ArrayList<>();
        List<Coin> all = coinRepository.findAll();
        List<PriceResponse> tradePrice = new ArrayList<>();
        for (Coin coin : all) {
            tradePrice = feign.getTradePrice(1, coin.getMarket());
            for (PriceResponse priceResponse : tradePrice) {


                PriceRequest priceRequest = new PriceRequest(
                        priceResponse.getPrice(),
                        coin.getMarket(),
                        priceResponse.getCandleDateTime()

                );

                Price save = priceRepository.save(priceRequest.toPrice());

                prices.add(save);
            }
        }
        return prices.stream().map(price -> new PriceResponse(price)).collect(Collectors.toList());
    }


}
