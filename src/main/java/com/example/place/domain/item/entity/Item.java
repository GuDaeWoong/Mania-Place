package com.example.place.domain.item.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
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
import jakarta.validation.constraints.Min;
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

	// 재고 감소
	public void decreaseStock(int quantity){
		if(this.count < quantity){
			throw new CustomException(ExceptionCode.OUT_OF_STOCK);
		}
		this.count -= quantity;
	}

	// 주문 취소로 인한 재고 증가
	public void increaseStock(int quantity) {
		this.count += quantity;
	}


}
