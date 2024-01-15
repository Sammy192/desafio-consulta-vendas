package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.ReportMinDTO;
import com.devsuperior.dsmeta.dto.SummaryMinDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {


    @Query(value = "SELECT new com.devsuperior.dsmeta.dto.ReportMinDTO(obj.id, obj.date, obj.amount, obj.seller.name) " +
            "FROM Sale obj " +
            "WHERE obj.date BETWEEN :min AND :max " +
            "AND UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name))",
            countQuery = "SELECT COUNT(obj) " +
                    "FROM Sale obj " +
                    "JOIN obj.seller " +
                    "WHERE obj.date BETWEEN :min AND :max " +
                    "AND UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name))")
    Page<ReportMinDTO> searchSalesByPeriod(LocalDate min, LocalDate max, String name, Pageable pageable);

    //versao selecionando tudo do obj e usando join fetch

  /*  @Query(value = "SELECT obj " +
            "FROM Sale obj " +
            "JOIN FETCH obj.seller " +
            "WHERE obj.date BETWEEN :min AND :max " +
            "AND UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name))",
            countQuery = "SELECT COUNT(obj) " +
                    "FROM Sale obj " +
                    "JOIN obj.seller " +
                    "WHERE obj.date BETWEEN :min AND :max " +
                    "AND UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name))")
    Page<Sale> searchSalesByPeriod(LocalDate min, LocalDate max, String name, Pageable pageable);*/

    @Query("SELECT new com.devsuperior.dsmeta.dto.SummaryMinDTO(obj.seller.name, SUM(obj.amount)) " +
            "FROM Sale obj " +
            "WHERE obj.date BETWEEN :min AND :max " +
            "GROUP BY obj.seller.name")
    List<SummaryMinDTO> searchTotalSales(LocalDate min, LocalDate max);
}
