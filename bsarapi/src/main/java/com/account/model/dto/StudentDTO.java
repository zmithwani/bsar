package com.account.model.dto;

import java.sql.Timestamp;

public class StudentDTO {

	private int userId;
	private int moduleId;
	private int moduleActivityId;
	private String moduleName;
	private String moduleCode;
	private String moduleActivity;
	private Timestamp moduleSchedule;
	private String attendance;

	
	

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getModuleId() {
		return moduleId;
	}

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleActivity() {
		return moduleActivity;
	}

	public void setModuleActivity(String moduleActivity) {
		this.moduleActivity = moduleActivity;
	}

	public String getAttendance() {
		return attendance;
	}

	public void setAttendance(String attendance) {
		this.attendance = attendance;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public Timestamp getModuleSchedule() {
		return moduleSchedule;
	}

	public void setModuleSchedule(Timestamp moduleSchedule) {
		this.moduleSchedule = moduleSchedule;
	}

	public int getModuleActivityId() {
		return moduleActivityId;
	}

	public void setModuleActivityId(int moduleActivityId) {
		this.moduleActivityId = moduleActivityId;
	}

	
	
}
