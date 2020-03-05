package com.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.account.dao.UserDAO;
import com.account.model.User;

@Service
@Transactional
public class UserServiceImp implements UserService {

	@Autowired
	private UserDAO accountdao;

	public boolean saveUser(User account) {
		return accountdao.saveUser(account);
	}

	public List<User> getUsers() {
		return accountdao.getUsers();
	}

	public boolean deleteUser(User account) {
		return accountdao.deleteUser(account);
	}

	public List<User> getUserById(User account) {
		return accountdao.getUserById(account);
	}

	public User getUserByName(User account) {
		return accountdao.getUserByName(account);
	}

	public boolean updateUser(User account) {
		return accountdao.updateUser(account);
	}

	public boolean lockUser(User account) {
		return accountdao.lockUser(account);
	}

	public boolean unlockUser(User account) {
		return accountdao.unlockUser(account);
	}

	@Override
	public boolean fingerPrint(User account) {
		return accountdao.fingerPrint(account);
	}

}
