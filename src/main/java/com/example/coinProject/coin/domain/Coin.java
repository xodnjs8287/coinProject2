package com.example.coinProject.coin.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Coin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer coinNo;
    @Column(nullable = false, unique = true)
    private String market;
    @Column(nullable = false, unique = true)
    private String korName;
    @Column(nullable = false, unique = true)
    private String engName;
    @Column(nullable = false)
    private BigDecimal tradePrice;
}
