package com.account.model.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.account.model.Module;
import com.account.model.ModuleActivity;
import com.account.model.ModuleSchedule;

public class StudentDTO {

	private int userId;
	private int moduleId;
	private int activityId;
	private int scheduleId;
	private String moduleName;
	private String moduleCode;
	private String moduleActivity;
	private Timestamp moduleSchedule;
	private String fingerPrint;

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

	public String getFingerPrint() {
		return fingerPrint;
	}

	public void setFingerPrint(String fingerPrint) {
		this.fingerPrint = fingerPrint;
	}

}
