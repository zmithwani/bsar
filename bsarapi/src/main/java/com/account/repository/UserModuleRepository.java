package com.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.account.model.UserModule;

public interface UserModuleRepository extends JpaRepository<UserModule, Long> {

	List<UserModule> findByUserId(int userId);

	List<UserModule> findByModuleId(int moduleId);

	List<UserModule> findByUserIdOrderByModuleId(int userId);

}
