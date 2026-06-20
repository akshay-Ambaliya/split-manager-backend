package com.splitManager.splitmanager.repository;

import com.splitManager.splitmanager.entity.ExpenseSplit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ExpenseSplitRepository extends JpaRepository<ExpenseSplit, Long> {

    List<ExpenseSplit> findByExpenseId(Long expenseId);

    @Query("SELECT SUM(es.amount) FROM ExpenseSplit es WHERE es.user.id = :userId")
    BigDecimal getTotalOwed(@Param("userId") Long userId);

    @Query("""
            SELECT es
            FROM ExpenseSplit es
            WHERE es.expense.group.id = :groupId
            """)
    List<ExpenseSplit> findByGroupId(@Param("groupId") Long groupId);
}