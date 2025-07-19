package com.example.place.common.entity;

import java.time.LocalDateTime;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class SoftDeleteEntity extends BaseEntity {

	private boolean isDeleted;

	private LocalDateTime deletedAt;

	public void delete() {
		this.isDeleted = true;
		this.deletedAt = LocalDateTime.now();
	}

	public Boolean isDeleted() {
		return isDeleted;
	}
}