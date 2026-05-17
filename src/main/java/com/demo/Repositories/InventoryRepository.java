package com.demo.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.Entities.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory , Long> {
	
	@Query("SELECT i FROM Inventory i JOIN FETCH i.medicine  "
			+ "WHERE i.pharmacy.id = :pharmaId "
			+ "AND i.quantity <= 5")
	List<Inventory> FindLowStockMedicinesInPharmacy(@Param("pharmaId") Long pharmaId);
	
	
	@Query("SELECT i FROM Inventory i JOIN FETCH i.medicine  "
			+ "WHERE i.pharmacy.id = :pharmaId ")
	public List<Inventory> GetMedicinesInPharma(@Param("pharmaId") Long pharmaId);
	
	@Query("SELECT i FROM Inventory i "
			+ "WHERE i.pharmacy.id = :pharmaId "
			+ "AND i.medicine.id = :medicineId")
	public Optional<Inventory> findInventoryByIDs
				(@Param("pharmaId")Long pharmaId ,@Param("medicineId") Long medicineId);
	
	
	@Query("SELECT COUNT(i) > 0 FROM Inventory i "
			+ "WHERE i.pharmacy.id = :pharmaId "
			+ "AND i.medicine.id = :medicineId "
			+ "AND i.quantity >= :quan")
	public boolean IsMedicineAvailabel(@Param("pharmaId")Long pharmaId ,
										@Param("medicineId") Long medicineId ,
										@Param("quan") int quantity);
}
