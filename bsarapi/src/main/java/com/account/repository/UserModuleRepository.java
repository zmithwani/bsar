package com.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.account.model.UserModule;

public interface UserModuleRepository extends JpaRepository<UserModule, Long> {

}
