package com.account.dao;

import java.util.List;

import com.account.model.User;

public interface UserDAO {

	public boolean saveUser(User account);

	public List<User> getUsers();

	public boolean deleteUser(User account);

	public List<User> getUserById(User account);

	public User getUserByName(User account);

	public boolean updateUser(User account);

	public boolean lockUser(User account);

	public boolean unlockUser(User account);

}
