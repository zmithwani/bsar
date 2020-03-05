package com.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.account.model.Module;
import com.account.model.dto.StudentDTO;

public interface ModuleRepository extends JpaRepository<Module, Long> {

	Module findByModuleId(int moduleId);

	Module findByModuleCode(String moduleCode);

	Module findByModuleName(String moduleName);

}
