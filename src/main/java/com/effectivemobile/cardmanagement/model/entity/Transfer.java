package com.effectivemobile.cardmanagement.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Дмитрий Ельцов
 **/
@Entity
@Table(name = "transfers")
@Getter
@Setter
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Card fromCard;

    @ManyToOne
    private Card toCard;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "transfer_date")
    private LocalDateTime transferDate = LocalDateTime.now();
}
