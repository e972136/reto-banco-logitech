package com.patito.banco_test.repository;

import com.patito.banco_test.entity.BalanceTransactions;
import com.patito.banco_test.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BalanceTransactionsRepository extends JpaRepository<BalanceTransactions,Integer> {
    List<BalanceTransactions> findByclient(Client client);
}
