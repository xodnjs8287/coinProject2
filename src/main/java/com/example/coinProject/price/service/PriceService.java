package com.example.coinProject.price.service;

import com.example.coinProject.coin.LimitedQueue;
import com.example.coinProject.coin.domain.Coin;
import com.example.coinProject.coin.repository.CoinRepository;
import com.example.coinProject.coin.service.Feign;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Queue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PriceService {

    private static final int UNIT = 1;
    private static final double EMA = (double) 1 / 14;
    private final Feign feign;
    private final CoinRepository coinRepository;
    private final Queue<BigDecimal> upQueue = new LimitedQueue<>(200);
    private final Queue<BigDecimal> downQueue = new LimitedQueue<>(200);

    @Transactional
    public Double getRsi() {
        List<Coin> coins = coinRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);

        for (Coin coin : coins) {
            BigDecimal yesterdayPrice = feign.getTradePrice(UNIT, coin.getMarket(),
                yesterday.toString()).getPrice();
            BigDecimal todayPrice = feign.getTradePrice(UNIT, coin.getMarket(),
                now.toString()).getPrice();

            BigDecimal difference = todayPrice.subtract(yesterdayPrice);
            if(difference.longValue() > 0) {
                upQueue.add(difference);
            }else if(difference.longValue() < 0) {
                downQueue.add(difference);
            }else {
                upQueue.add(difference);
                downQueue.add(difference);
            }
        }
        double rs = getAU().doubleValue() / getAD().doubleValue();
        return 100 - (100 / (rs + 1));
    }

    private BigDecimal getAU() {
        BigDecimal upEma = BigDecimal.valueOf(0);
        if(!CollectionUtils.isEmpty(upQueue)) {
            upEma = upQueue.poll();
            if(upQueue.size() > 1) {
                for (BigDecimal upElement : upQueue) {
                    upEma = (upElement.multiply(BigDecimal.valueOf(EMA))).add(upEma.multiply(
                        BigDecimal.valueOf(1-EMA)));
                }
            }
        }
        return upEma;
    }

    private BigDecimal getAD() {
        BigDecimal downEma = BigDecimal.valueOf(0);
        if(!CollectionUtils.isEmpty(downQueue)) {
            downEma = downQueue.poll();
            if(downQueue.size() > 1) {
                for (BigDecimal downElement : downQueue) {
                    downEma = (downElement.multiply(BigDecimal.valueOf(EMA))).add(downEma.multiply(
                        BigDecimal.valueOf(1-EMA)));
                }
            }
        }
        return downEma;
    }
}