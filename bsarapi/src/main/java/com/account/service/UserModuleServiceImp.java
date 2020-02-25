package com.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.account.dao.UserModuleDAO;
import com.account.model.UserModule;

@Service
@Transactional
public class UserModuleServiceImp implements UserModuleService {

	@Autowired
	private UserModuleDAO usermoduledao;

	
	@Override
	public List<UserModule> getUserModule(UserModule userModule) {
		return usermoduledao.getUserModule(userModule);
	}

}
