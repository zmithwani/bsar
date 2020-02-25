package com.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.account.model.ModuleActivity;
import com.account.model.dto.StudentDTO;

public interface ModuleActivityRepository extends JpaRepository<ModuleActivity, Long> {

	List<ModuleActivity> findByModuleId(int moduleId);

	ModuleActivity findByModuleActivityId(int moduleActivityId);


}
