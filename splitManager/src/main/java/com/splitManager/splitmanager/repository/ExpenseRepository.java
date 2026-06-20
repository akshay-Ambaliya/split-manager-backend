package com.splitManager.splitmanager.repository;

import com.splitManager.splitmanager.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Page<Expense> findByGroupId(Long groupId, Pageable pageable);

    List<Expense> findByGroupId(Long groupId);

    @Query("SELECT SUM(e.totalAmount) FROM Expense e WHERE e.paidBy.id = :userId")
    BigDecimal getTotalPaidByUser(@Param("userId") Long userId);
}