package com.koinsys.wallapop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koinsys.wallapop.entities.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

}
