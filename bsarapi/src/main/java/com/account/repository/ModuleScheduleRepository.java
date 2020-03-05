package com.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.account.model.ModuleSchedule;
import com.account.model.dto.StudentDTO;

public interface ModuleScheduleRepository extends JpaRepository<ModuleSchedule, Long> {

	List<ModuleSchedule> findBymoduleActivityId(int moduleActivityId);

	ModuleSchedule findByModuleScheduleId(int userid);




}
