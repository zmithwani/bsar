package com.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.account.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String name);

	User findByUserId(int id);

	User findByUsername(int userId);

}
