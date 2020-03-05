package com.account.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.account.model.User;
import com.account.repository.UserRepository;

@Repository
public class UserDAOImp implements UserDAO {

	@Autowired
	private UserRepository accountRepository;

	@Override
	public boolean saveUser(User account) {
		boolean status = false;
		try {
			accountRepository.save(account);
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public List<User> getUsers() {
		List<User> list = accountRepository.findAll();
		return list;
	}

	@Override
	public boolean deleteUser(User account) {
		boolean status = false;
		try {
			accountRepository.delete(account);
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public List<User> getUserById(User account) {

		List<User> list = new ArrayList<User>();
		User user = accountRepository.findByUserId(account.getUserId());
		if (user != null) {
			list.add(user);
		}
		return list;
	}

	@Override
	public User getUserByName(User account) {
		User list = accountRepository.findByUsername(account.getUsername());
		return list;
	}

	@Override
	public boolean updateUser(User account) {
		boolean status = false;
		try {
			accountRepository.save(account);
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public boolean lockUser(User account) {
		boolean status = false;
		try {
			accountRepository.save(account);
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public boolean unlockUser(User account) {
		boolean status = false;
		try {
			accountRepository.save(account);
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public boolean fingerPrint(User account) {
		boolean status = false;
		try {
			accountRepository.save(account);
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

}
