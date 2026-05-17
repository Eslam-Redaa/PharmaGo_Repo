package com.demo.Entities;

import java.sql.Timestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Inventory", indexes = {
		@Index(name = "idx_inventory_pharmacyId" , columnList = "pharmacy_id"),
		@Index(name = "idx_inventory_medicineId" , columnList = "medicine_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "pharmacy_id")
	private Pharmacy pharmacy;
	
	@ManyToOne
	@JoinColumn(name = "medicine_id")
	private Medicine medicine;
	
	private int quantity;
	//private double price;
	
	@UpdateTimestamp
	private Timestamp lastUpdated;
}
