package com.account.model.dto;

import java.sql.Timestamp;

public class AttendanceDTO {
	
	private int userId;
	private int moduleId;
	private int activityId;
	private int scheduleId;
	private String moduleName;
	private String moduleCode;
	private String moduleActivity;
	private Timestamp moduleSchedule;
	private String assigned;
	private String attended;
	private String nonAttended;
	private String belowPercent;
	private String belowList;
	private Timestamp fromDate;
	private Timestamp toDate;
	private int percentage;
	
	
	public int getPercentage() {
		return percentage;
	}
	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getBelowList() {
		return belowList;
	}
	public void setBelowList(String belowList) {
		this.belowList = belowList;
	}
	public String getBelowPercent() {
		return belowPercent;
	}
	public void setBelowPercent(String belowPercent) {
		this.belowPercent = belowPercent;
	}
	public int getModuleId() {
		return moduleId;
	}
	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}
	public int getActivityId() {
		return activityId;
	}
	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}
	public int getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getModuleCode() {
		return moduleCode;
	}
	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}
	public String getModuleActivity() {
		return moduleActivity;
	}
	public void setModuleActivity(String moduleActivity) {
		this.moduleActivity = moduleActivity;
	}
	public Timestamp getModuleSchedule() {
		return moduleSchedule;
	}
	public void setModuleSchedule(Timestamp moduleSchedule) {
		this.moduleSchedule = moduleSchedule;
	}
	public String getAssigned() {
		return assigned;
	}
	public void setAssigned(String assigned) {
		this.assigned = assigned;
	}
	public String getAttended() {
		return attended;
	}
	public void setAttended(String attended) {
		this.attended = attended;
	}
	public String getNonAttended() {
		return nonAttended;
	}
	public void setNonAttended(String nonAttended) {
		this.nonAttended = nonAttended;
	}
	public Timestamp getFromDate() {
		return fromDate;
	}
	public void setFromDate(Timestamp fromDate) {
		this.fromDate = fromDate;
	}
	public Timestamp getToDate() {
		return toDate;
	}
	public void setToDate(Timestamp toDate) {
		this.toDate = toDate;
	}
	
	
	

}
