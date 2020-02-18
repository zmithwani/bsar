package com.account.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity

@Table(name = "fingerprint")
public class FingerPrint {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int fingerPrintId;
	private int userId;
	private Timestamp createdAt;
	private String createdBy;
	private Timestamp updatedAt;
	private String updatedBy;
	
	public int getFingerPrintId() {
		return fingerPrintId;
	}
	public int getUserId() {
		return userId;
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
	public void setFingerPrintId(int fingerPrintId) {
		this.fingerPrintId = fingerPrintId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
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
