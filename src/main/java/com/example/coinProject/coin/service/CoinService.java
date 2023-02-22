package com.example.coinProject.coin.service;

import com.example.coinProject.coin.dto.CoinResponse;
import com.example.coinProject.coin.repository.CoinRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CoinService {

    private final CoinRepository coinRepository;
    private final Feign feign;

    @Transactional
    public List<CoinResponse> getCoinPrice(int unit, String market) {
        return feign.getTradePrice(unit, market);
    }

    @Transactional
    public List<CoinResponse> getCoinResponse() {
        return feign.getMarketCode();
    }

}
