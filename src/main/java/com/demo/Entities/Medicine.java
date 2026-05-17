package com.demo.Entities;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Medicine" , indexes = {
		@Index(name = "idx_medicine_name" , columnList = "name"),
		@Index(name = "idx_medicine_category" , columnList = "category")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Medicine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	private String category;
	
	private String imageUrl;
	private String imagePublicId;
	
	private boolean requiresPrescription;
	
	@OneToMany(mappedBy="medicine")
	private List<Inventory> inventories;
	
	//@Transient
	private double price;
	
}
