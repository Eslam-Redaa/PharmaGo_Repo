package com.demo.Repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.Entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
	@Query("SELECT o FROM Order o WHERE o.pharmacy.id = :pharmaId"
			+ " AND CAST(o.createdAt AS date) = :date")
	List<Order> findOrdersByPharmacyAndDate
			(@Param("pharmaId") Long pharmaId ,@Param("date") LocalDate date);

}
