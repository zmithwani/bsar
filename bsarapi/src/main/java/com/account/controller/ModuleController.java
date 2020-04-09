package com.account.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@Autowired
	private ModuleRepository moduleRepository;

	@PostMapping("savemodule")
	public boolean saveModule(@RequestBody StudentDTO studentDTO) {
		boolean status = false;

		Module module = new Module();
		module.setModuleName(studentDTO.getModuleName());
		module.setModuleCode(studentDTO.getModuleCode());
		Date date = new Date();
		module.setCreatedAt(new Timestamp(date.getTime()));
		module.setStatus(Constant.ACTIVE);
		Module moduleName = moduleService.getModuleByName(module);
		Module moduleCode = moduleService.getModuleByCode(module);

		if (moduleName == null && moduleCode == null) {

			status = moduleService.saveModule(module);
		}
		Module moduleNames = moduleService.getModuleByModuleName(module.getModuleName());

		if (moduleNames != null) {
			if (studentDTO.getModuleActivity() != null && !studentDTO.getModuleActivity().equals("")) {
				ModuleActivity activity = new ModuleActivity();
				activity.setModuleId(moduleNames.getModuleId());
				activity.setModuleActivity(studentDTO.getModuleActivity());
				activity.setCreatedAt(new Timestamp(date.getTime()));
				status = moduleService.saveModuleActivity(activity);

				ModuleActivity moduleActivityId = moduleService.getModuleActivityById(activity.getModuleActivityId());
				if (moduleActivityId != null) {

					if (studentDTO.getModuleFrequency() > 0) {
						for (int i = 0; i < 12; i++) {
							Date stDate = addDays(studentDTO.getModuleFrequency() * 7 * i,
									studentDTO.getModuleSchedule());
							ModuleSchedule schedule = new ModuleSchedule();
							schedule.setModuleActivityId(moduleActivityId.getModuleActivityId());
							// schedule.setModuleScheduled(studentDTO.getModuleSchedule());
							schedule.setModuleScheduled(new Timestamp(stDate.getTime()));
							schedule.setCreatedAt(new Timestamp(date.getTime()));
							status = moduleService.saveModuleSchedule(schedule);
						}
					} else {
						ModuleSchedule schedule = new ModuleSchedule();
						schedule.setModuleActivityId(moduleActivityId.getModuleActivityId());
						schedule.setModuleScheduled(studentDTO.getModuleSchedule());
						schedule.setCreatedAt(new Timestamp(date.getTime()));
						status = moduleService.saveModuleSchedule(schedule);
					}
				}
			}
			if (studentDTO.getModuleActivityOne() != null && !studentDTO.getModuleActivityOne().equals("")) {
				ModuleActivity activity = new ModuleActivity();
				activity.setModuleId(moduleNames.getModuleId());
				activity.setModuleActivity(studentDTO.getModuleActivityOne());
				activity.setCreatedAt(new Timestamp(date.getTime()));
				status = moduleService.saveModuleActivity(activity);

				ModuleActivity moduleActivityId = moduleService.getModuleActivityById(activity.getModuleActivityId());
				if (moduleActivityId != null) {

					if (studentDTO.getModuleFrequency() > 0) {
						for (int i = 0; i < 12; i++) {
							Date stDate = addDays(studentDTO.getModuleFrequency() * 7 * i,
									studentDTO.getModuleSchedule());
							ModuleSchedule schedule = new ModuleSchedule();
							schedule.setModuleActivityId(moduleActivityId.getModuleActivityId());
							// schedule.setModuleScheduled(studentDTO.getModuleSchedule());
							schedule.setModuleScheduled(new Timestamp(stDate.getTime()));
							schedule.setTimeOne(studentDTO.getModuleTimeOne());
							schedule.setTimeTwo(studentDTO.getModuleTimeTwo());
							schedule.setTimeThree(studentDTO.getModuleTimeThree());
							schedule.setTimeFour(studentDTO.getModuleTimeFour());
							schedule.setTimeFive(studentDTO.getModuleTimeFive());
							schedule.setCreatedAt(new Timestamp(date.getTime()));
							status = moduleService.saveModuleSchedule(schedule);
						}
					} else {
						ModuleSchedule schedule = new ModuleSchedule();
						schedule.setModuleActivityId(moduleActivityId.getModuleActivityId());
						schedule.setModuleScheduled(studentDTO.getModuleSchedule());
						schedule.setCreatedAt(new Timestamp(date.getTime()));
						status = moduleService.saveModuleSchedule(schedule);
					}
				}
			}
		}

		return status;
	}

	@GetMapping("modulelist")
	public List<Module> allModule() {

		return moduleService.getModuleList();

	}

	@PostMapping("enablemodule/{moduleid}")
	public boolean enableModule(@RequestBody Module module, @PathVariable("moduleid") int moduleid) {
		boolean status = false;
		Module moduleExist = moduleRepository.findByModuleId(moduleid);
		if (moduleExist != null) {
			moduleExist.setStatus(Constant.ACTIVE);
			moduleExist.setUpdatedAt(new Timestamp(new Date().getTime()));
			status = moduleService.enableModule(moduleExist);
		}
		return status;
	}

	@PostMapping("disablemodule/{moduleid}")
	public boolean disableModule(@RequestBody Module module, @PathVariable("moduleid") int moduleid) {
		boolean status = false;
		Module moduleExist = moduleRepository.findByModuleId(moduleid);
		if (moduleExist != null) {
			moduleExist.setStatus(Constant.LOCKED);
			moduleExist.setUpdatedAt(new Timestamp(new Date().getTime()));
			status = moduleService.enableModule(moduleExist);
		}
		return status;
	}

	private Date addDays(int days, Date date) {

		Date currentDate = date;

		// convert date to calendar
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);

		// manipulate date
		c.add(Calendar.DATE, days);

		// convert calendar to date
		Date updateDate = c.getTime();

		return updateDate;

	}

}
