package com.splitManager.splitmanager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User payer;

    @ManyToOne
    private User receiver;

    @ManyToOne
    private Group group;

    private BigDecimal amount;

    private LocalDateTime settlementDate;
}