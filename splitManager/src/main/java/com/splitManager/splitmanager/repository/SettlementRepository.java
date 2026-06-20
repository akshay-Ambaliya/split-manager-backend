package com.splitManager.splitmanager.repository;

import com.splitManager.splitmanager.entity.Settlement;
import com.splitManager.splitmanager.entity.User;
import com.splitManager.splitmanager.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {
    @Query("""
        SELECT COALESCE(SUM(s.amount), 0)
        FROM Settlement s
        WHERE s.payer.id = :userId
    """)
    BigDecimal getTotalSettledPaid(@Param("userId") Long userId);

    @Query("""
        SELECT COALESCE(SUM(s.amount), 0)
        FROM Settlement s
        WHERE s.receiver.id = :userId
    """)
    BigDecimal getTotalSettledReceived(@Param("userId") Long userId);
}