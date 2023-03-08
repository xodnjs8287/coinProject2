package com.example.coinProject.price.domain;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Price {
    BigDecimal yesterdayPrice;
    BigDecimal todayPrice;
}