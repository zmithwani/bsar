package com.account.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.account.model.Module;
import com.account.model.ModuleActivity;
import com.account.model.ModuleSchedule;
import com.account.model.User;
import com.account.model.dto.StudentDTO;
import com.account.repository.ModuleActivityRepository;
import com.account.repository.ModuleRepository;
import com.account.repository.ModuleScheduleRepository;
import com.account.service.ModuleService;
import com.account.util.Constant;
import com.account.util.RandomString;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/api")
public class ModuleController {

	@Autowired
	private ModuleService moduleService;

	@PostMapping("savemodule")
	public boolean saveModule(@RequestBody StudentDTO studentDTO) {
		boolean status = false;

		Module module = new Module();
		module.setModuleName(studentDTO.getModuleName());
		module.setModuleCode(studentDTO.getModuleCode());
		Date date = new Date();
		module.setCreatedAt(new Timestamp(date.getTime()));
		Module moduleName = moduleService.getModuleByName(module);
		Module moduleCode = moduleService.getModuleByCode(module);

		if (moduleName == null && moduleCode == null) {

			status = moduleService.saveModule(module);
		}
		Module moduleNames = moduleService.getModuleByModuleName(module.getModuleName());

		if (moduleNames != null) {
			ModuleActivity activity = new ModuleActivity();
			activity.setModuleId(moduleNames.getModuleId());
			activity.setModuleActivity(studentDTO.getModuleActivity());
			activity.setCreatedAt(new Timestamp(date.getTime()));
			status = moduleService.saveModuleActivity(activity);

			ModuleActivity moduleActivityId = moduleService.getModuleActivityById(activity.getModuleActivityId());
			if (moduleActivityId != null) {
				ModuleSchedule schedule = new ModuleSchedule();
				schedule.setModuleActivityId(moduleActivityId.getModuleActivityId());
				schedule.setModuleScheduled(studentDTO.getModuleSchedule());
				schedule.setCreatedAt(new Timestamp(date.getTime()));
				status = moduleService.saveModuleSchedule(schedule);
			}
		}

		return status;
	}

}
