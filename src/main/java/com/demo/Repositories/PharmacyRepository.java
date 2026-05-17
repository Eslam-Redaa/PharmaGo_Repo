package com.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.Entities.Pharmacy;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {

	@Query(value = "SELECT * FROM Pharmacy p " +
	        "WHERE (p.latitude BETWEEN :minlat AND :maxlat) " +
	        "AND (p.longitude BETWEEN :minlong AND :maxlong)", nativeQuery = true)
	List<Pharmacy> FindPharmaciesInWithBoundingBox(@Param("maxlat") double maxlat , @Param("minlat") double minlat
									,@Param("maxlong") double maxlong , @Param("minlong") double minlong);
	
	
	@Query(value = "SELECT p.* FROM Pharmacy p "
			+ "JOIN Inventory i ON p.id = i.pharmacy_id "
			+ "WHERE i.medicine_id = :medicineId "
			+ "AND i.quantity > 0 "
			+ "AND (p.latitude BETWEEN :minlat AND :maxlat)"
			+ "AND (p.longitude BETWEEN :minlong AND :maxlong)" 
			, nativeQuery = true)
	List<Pharmacy> FindNearbyPharmaciesForMedicine( @Param("medicineId") Long medicineId
			,@Param("maxlat") double maxlat , @Param("minlat") double minlat
			,@Param("maxlong") double maxlong , @Param("minlong") double minlong);
}
