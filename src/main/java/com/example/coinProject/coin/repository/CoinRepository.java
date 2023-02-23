package com.example.coinProject.coin.repository;

import com.example.coinProject.coin.domain.Coin;
import com.example.coinProject.coin.dto.coin.CoinResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoinRepository extends JpaRepository<Coin, Integer> {

    Optional<CoinResponse> findCoinByMarket (String market);

}
