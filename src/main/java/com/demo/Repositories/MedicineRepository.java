package com.demo.Repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.demo.Entities.Medicine;

public interface MedicineRepository extends JpaRepository<Medicine , Long> {
	
	List<Medicine> findByName(String name);
	
	List<Medicine> findByCategory(String category);
	
	List<Medicine> findByrequiresPrescriptionTrue();
	
	List<Medicine> findByrequiresPrescriptionFalse();
	
	@Query("SELECT DISTINCT m.category FROM Medicine m")
	List<String> findCategoriesList();
	
	@Query("SELECT m FROM Medicine m WHERE LOWER(m.name) LIKE LOWER( CONCAT(:prefix , '%') )")
	List<Medicine> findByNameStartingWith(@Param("prefix") String prefix , Pageable pageable);
	
	@Query("SELECT m.price FROM Medicine m WHERE m.id = :medId ")
	double getMedicinePrice(@Param("medId") Long id);

}
