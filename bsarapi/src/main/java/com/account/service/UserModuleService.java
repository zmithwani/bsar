package com.account.service;

import java.util.List;

import com.account.model.UserModule;

public interface UserModuleService {

	public List<UserModule> getUserModule(UserModule userModule);

	public boolean saveUserModule(UserModule userModule);

	public List<UserModule> getUserModuleByUserId(int userId);

	public boolean deleteUserModuleByUserId(UserModule userId);

}
