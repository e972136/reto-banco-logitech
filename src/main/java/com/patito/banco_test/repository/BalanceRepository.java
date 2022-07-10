package com.patito.banco_test.repository;

import com.patito.banco_test.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance,Integer> {
    public Balance findByaccount(String account);
}
