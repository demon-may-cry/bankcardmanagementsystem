package com.effectivemobile.cardmanagement.repository;

import com.effectivemobile.cardmanagement.model.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Дмитрий Ельцов
 **/
@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
