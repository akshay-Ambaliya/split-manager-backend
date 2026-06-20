package com.splitManager.splitmanager.repository;

import com.splitManager.splitmanager.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
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

    List<Settlement> findByPayerIdAndReceiverIdAndGroupId(
            Long payerId,
            Long receiverId,
            Long groupId
    );
}