package com.account.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.account.model.User;
import com.account.model.UserModule;
import com.account.repository.UserModuleRepository;

@Repository
public class UserModuleImp implements UserModuleDAO {

	@Autowired
	private UserModuleRepository usermoduleRepository;

	@Override
	public List<UserModule> getUserModule(UserModule userModule) {
		List<UserModule> list = usermoduleRepository.findByUserIdOrderByModuleId(userModule.getUserId());
		return list;
	}

	@Override
	public boolean saveUserModule(UserModule userModule) {
		boolean status = false;
		try {
			usermoduleRepository.save(userModule);
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public List<UserModule> getUserModuleByUserId(int userId) {
		List<UserModule> userModules = usermoduleRepository.findByUserId(userId);
		return userModules;
	}

	@Override
	public boolean deleteUserModuleByUserId(UserModule userId) {
		boolean status = false;
		try {
			usermoduleRepository.delete(userId);
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

}
