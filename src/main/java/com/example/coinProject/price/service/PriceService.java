package com.example.coinProject.price.service;

import com.example.coinProject.coin.repository.CoinRepository;
import com.example.coinProject.coin.service.Feign;
import com.example.coinProject.common.LimitedQueue;
import com.example.coinProject.price.dto.PriceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PriceService {

    private static final String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern(
        "yyyy-MM-dd HH:mm:ss"));
    private static final String yesterday = LocalDateTime.now().minusDays(1)
        .format(DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss"));
    private static final int UNIT = 5;
    private static final double EMA = (double) 1 / 14;
    private static final int COUNT = 200;

    private final Feign feign;
    private final CoinRepository coinRepository;
    LimitedQueue<BigDecimal> upQueue = new LimitedQueue<>(200);
    LimitedQueue<BigDecimal> downQueue = new LimitedQueue<>(200);


    @Transactional
    public Double getRsi() {
        List<PriceResponse> todayPrices = feign.getTradePrice(UNIT, "KRW-BTC",
            now, COUNT);
        List<PriceResponse> yesterdayPrices = feign.getTradePrice(UNIT, "KRW-BTC",
            yesterday, COUNT);

        for (int i = 0; i < 200; i++) {
            BigDecimal todayPrice = todayPrices.get(i).getPrice();
            BigDecimal yesterdayPrice = yesterdayPrices.get(i).getPrice();

            BigDecimal difference = todayPrice.subtract(yesterdayPrice);
            System.out.println("difference = " + difference);
            if (difference.longValue() > 0) {
                upQueue.add(difference);
                downQueue.add(BigDecimal.valueOf(0));
            } else if (difference.longValue() < 0) {
                upQueue.add(BigDecimal.valueOf(0));
                downQueue.add(difference.abs());
            } else {
                upQueue.add(difference);
                downQueue.add(difference);
            }
        }

        double test = upQueue.stream().mapToDouble(it -> it.doubleValue()).sum() / 200;
        double test2 = downQueue.stream().mapToDouble(it -> it.doubleValue()).sum() / 200;
        System.out.println("test = " + test);
        System.out.println("test2 = " + test2);

        double rs2 = test / test2;
        double rsi2 = rs2 / (1+rs2);
        System.out.println("rsi2 = " + rsi2);

        double v = getAU().doubleValue();
        double v1 = getAD().doubleValue();
        System.out.println("v = " + v);
        System.out.println("v1 = " + v1);
        double rs = getAU().doubleValue() / getAD().doubleValue();
        return 100 - (100 / (1 + rs));
    }

    private String minusMinute(LocalDateTime date, int unit) {
        return date.minusMinutes(unit).format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );
    }

    private BigDecimal getAU() {
        BigDecimal upEma = BigDecimal.valueOf(0);
        if (!CollectionUtils.isEmpty(upQueue)) {
            upEma = upQueue.poll();
            if (upQueue.size() > 1) {
                for (BigDecimal upElement : upQueue) {
                    upEma = (upElement.multiply(BigDecimal.valueOf(EMA))).add(upEma.multiply(
                        BigDecimal.valueOf(1 - EMA)));
                }
            }
        }
        return upEma;
    }

    private BigDecimal getAD() {
        BigDecimal downEma = BigDecimal.valueOf(0);
        if (!CollectionUtils.isEmpty(downQueue)) {
            downEma = downQueue.poll();
            if (downQueue.size() > 1) {
                for (BigDecimal downElement : downQueue) {
                    downEma = (downElement.multiply(BigDecimal.valueOf(EMA))).add(downEma.multiply(
                        BigDecimal.valueOf(1 - EMA)));
                }
            }
        }
        return downEma;
    }
}