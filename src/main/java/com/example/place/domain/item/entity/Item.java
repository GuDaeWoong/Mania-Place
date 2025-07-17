package com.example.place.domain.item.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.place.domain.Image.entity.Image;
import com.example.place.domain.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "items")
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	private String productName;
	private String productDescription;

	private Boolean is_official;
	private double price;
	private int count;

	@OneToMany(mappedBy = "item")
	private List<Image> images = new ArrayList<>();

}
