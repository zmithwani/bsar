package com.account.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity

@Table(name = "moduleschedule")
public class ModuleSchedule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int moduleScheduleId;
	private int moduleActivityId;
	private Timestamp moduleScheduled;
	private String timeOne;
	private String timeTwo;
	private String timeThree;
	private String timeFour;
	private String timeFive;
	private Timestamp createdAt;
	private String createdBy;
	private Timestamp updatedAt;
	private String updatedBy;

	public int getModuleScheduleId() {
		return moduleScheduleId;
	}

	public int getModuleActivityId() {
		return moduleActivityId;
	}

	public Timestamp getModuleScheduled() {
		return moduleScheduled;
	}

	public String getTimeOne() {
		return timeOne;
	}

	public String getTimeTwo() {
		return timeTwo;
	}

	public String getTimeThree() {
		return timeThree;
	}

	public String getTimeFour() {
		return timeFour;
	}

	public String getTimeFive() {
		return timeFive;
	}

	public void setTimeOne(String timeOne) {
		this.timeOne = timeOne;
	}

	public void setTimeTwo(String timeTwo) {
		this.timeTwo = timeTwo;
	}

	public void setTimeThree(String timeThree) {
		this.timeThree = timeThree;
	}

	public void setTimeFour(String timeFour) {
		this.timeFour = timeFour;
	}

	public void setTimeFive(String timeFive) {
		this.timeFive = timeFive;
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

	public void setModuleScheduleId(int moduleScheduleId) {
		this.moduleScheduleId = moduleScheduleId;
	}

	public void setModuleActivityId(int moduleActivityId) {
		this.moduleActivityId = moduleActivityId;
	}

	public void setModuleScheduled(Timestamp moduleScheduled) {
		this.moduleScheduled = moduleScheduled;
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
