package com.demo.Entities;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Pharmacy" , indexes= {
		@Index(name = "idx_pharmacy_latitude" , columnList = "latitude"),
		@Index(name = "idx_pharmacy_longitude" , columnList = "longitude")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pharmacy {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String address;
	private double latitude;
	private double longitude;
	private String phone;
	private String imageUrl;
	private String imagePublicId;
	private boolean deliverySupport;
	
	@Transient
	private double distance;
	
	@OneToMany(mappedBy = "pharmacy")
	private List<Inventory> inventories;
	
	@OneToMany(mappedBy = "pharmacy")
	private List<Order> orders;
	
	@OneToOne
	private User owner;
	
}
