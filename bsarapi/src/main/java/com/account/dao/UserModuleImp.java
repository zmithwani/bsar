package com.account.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.account.model.UserModule;
import com.account.repository.UserModuleRepository;

@Repository
public class UserModuleImp implements UserModuleDAO {

	@Autowired
	private UserModuleRepository usermoduleRepository;

	@Override
	public List<UserModule> getUserModule(UserModule userModule) {
		List<UserModule> list = usermoduleRepository.findByUserId(userModule.getUserId());
		return list;
	}

}
