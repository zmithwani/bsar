package com.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.account.model.Module;
public interface ModuleRepository extends JpaRepository<Module, Long> {

}
