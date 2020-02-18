package com.account.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@IdClass(UserModuleKey.class)
@Table(name = "usermodule")
public class UserModule {

	@Id
	int userId;
	@Id
	int moduleId;
	private Timestamp createdAt;
	private String createdBy;
	private Timestamp updatedAt;
	private String updatedBy;
	public int getUserId() {
		return userId;
	}
	public int getModuleId() {
		return moduleId;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
}
