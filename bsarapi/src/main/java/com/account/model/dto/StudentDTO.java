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
	private String moduleActivityOne;

	private Timestamp moduleSchedule;
	private int moduleFrequency;
	private String moduleTimeOne;
	private String moduleTimeTwo;
	private String moduleTimeThree;
	private String moduleTimeFour;
	private String moduleTimeFive;
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

	public String getModuleActivityOne() {
		return moduleActivityOne;
	}

	public int getModuleFrequency() {
		return moduleFrequency;
	}

	public String getModuleTimeOne() {
		return moduleTimeOne;
	}

	public String getModuleTimeTwo() {
		return moduleTimeTwo;
	}

	public String getModuleTimeThree() {
		return moduleTimeThree;
	}

	public String getModuleTimeFour() {
		return moduleTimeFour;
	}

	public String getModuleTimeFive() {
		return moduleTimeFive;
	}

	public void setModuleActivityOne(String moduleActivityOne) {
		this.moduleActivityOne = moduleActivityOne;
	}

	public void setModuleFrequency(int moduleFrequency) {
		this.moduleFrequency = moduleFrequency;
	}

	public void setModuleTimeOne(String moduleTimeOne) {
		this.moduleTimeOne = moduleTimeOne;
	}

	public void setModuleTimeTwo(String moduleTimeTwo) {
		this.moduleTimeTwo = moduleTimeTwo;
	}

	public void setModuleTimeThree(String moduleTimeThree) {
		this.moduleTimeThree = moduleTimeThree;
	}

	public void setModuleTimeFour(String moduleTimeFour) {
		this.moduleTimeFour = moduleTimeFour;
	}

	public void setModuleTimeFive(String moduleTimeFive) {
		this.moduleTimeFive = moduleTimeFive;
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
