package com.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.account.model.UserType;

public interface UserTypeRepository extends JpaRepository<UserType, Long> {

	UserType findByUserTypeId(Long id);

}
