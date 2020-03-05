package com.account.dao;

import java.util.List;

import com.account.model.User;
import com.account.model.UserModule;

public interface UserModuleDAO {

	public List<UserModule> getUserModule(UserModule userModule);

	public boolean saveUserModule(UserModule userModule);

	public List<UserModule> getUserModuleByUserId(int userId);

	public boolean deleteUserModuleByUserId(UserModule userId);

}
