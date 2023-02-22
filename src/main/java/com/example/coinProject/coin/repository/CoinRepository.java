package com.example.coinProject.coin.repository;

import com.example.coinProject.coin.domain.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<Coin, Integer> {

}
