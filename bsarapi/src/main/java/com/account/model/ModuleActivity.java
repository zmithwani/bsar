package com.account.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity

@Table(name = "moduleactivity")
public class ModuleActivity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int moduleActivityId;
	private int moduleId;
	private String moduleActivity;
	private Timestamp createdAt;
	private String createdBy;
	private Timestamp updatedAt;
	private String updatedBy;
	public int getModuleActivityId() {
		return moduleActivityId;
	}
	public int getModuleId() {
		return moduleId;
	}
	public String getModuleActivity() {
		return moduleActivity;
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
	public void setModuleActivityId(int moduleActivityId) {
		this.moduleActivityId = moduleActivityId;
	}
	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}
	public void setModuleActivity(String moduleActivity) {
		this.moduleActivity = moduleActivity;
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
