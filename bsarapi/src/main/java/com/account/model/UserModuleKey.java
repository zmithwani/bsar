package com.account.model;

import java.io.Serializable;

public class UserModuleKey implements Serializable {

	private static final long serialVersionUID = 1L;

	int userId;

	int moduleId;

	public int getUserId() {
		return userId;
	}

	public int getModuleId() {
		return moduleId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

}
