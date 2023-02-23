package com.example.coinProject.coin.repository;

import com.example.coinProject.coin.domain.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, Integer> {

}
