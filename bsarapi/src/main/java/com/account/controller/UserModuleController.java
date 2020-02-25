package com.account.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.account.model.Module;
import com.account.model.ModuleActivity;
import com.account.model.ModuleSchedule;
import com.account.model.UserModule;
import com.account.model.dto.StudentDTO;
import com.account.repository.ModuleActivityRepository;
import com.account.repository.ModuleRepository;
import com.account.repository.ModuleScheduleRepository;
import com.account.service.UserModuleService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/api")
public class UserModuleController {

	@Autowired
	private UserModuleService userModuleService;

	@Autowired
	private ModuleRepository moduleRepository;

	@Autowired
	private ModuleActivityRepository moduleActivityRepository;

	@Autowired
	private ModuleScheduleRepository moduleScheduleRepository;

	@GetMapping("usermodule/{userid}")
	public List<StudentDTO> allUserModule(@PathVariable("userid") int userid) {

		UserModule usModule = new UserModule();
		usModule.setUserId(userid);
		List<UserModule> userModules = userModuleService.getUserModule(usModule);
		List<StudentDTO> studentDetails = new ArrayList<StudentDTO>();

		for (UserModule userModule : userModules) {

			Module module = moduleRepository.findByModuleId(userModule.getModuleId());

			List<ModuleActivity> moduleActivities = moduleActivityRepository.findByModuleId(userModule.getModuleId());
			for (ModuleActivity moduleActivity : moduleActivities) {
				StudentDTO studentDetail = new StudentDTO();
				studentDetail.setUserId(userModule.getUserId());
				studentDetail.setModuleId(userModule.getModuleId());
				studentDetail.setModuleName(module.getModuleName());
				studentDetail.setModuleActivity(moduleActivity.getModuleActivity());
				List<ModuleSchedule> moduleSchedules = moduleScheduleRepository
						.findBymoduleScheduleId(moduleActivity.getModuleActivityId());
				for (ModuleSchedule moduleSchedule : moduleSchedules) {
					studentDetail.setModuleSchedule(moduleSchedule.getModuleScheduled());
				}
				studentDetails.add(studentDetail);
			}

		}

		return studentDetails;
	}

	@GetMapping("moduleall/{userid}")
	public List<StudentDTO> allModule(@PathVariable("userid") int userid) {

		UserModule usModule = new UserModule();
		usModule.setUserId(userid);
		List<UserModule> userModules = userModuleService.getUserModule(usModule);
		List<StudentDTO> studentDetails = new ArrayList<StudentDTO>();
		String moduleNames = "";

		for (UserModule userModule : userModules) {

			Module module = moduleRepository.findByModuleId(userModule.getModuleId());
			StudentDTO studentDetail = new StudentDTO();
			studentDetail.setUserId(userModule.getUserId());
			studentDetail.setModuleId(userModule.getModuleId());
			studentDetail.setModuleName(module.getModuleName());

			studentDetails.add(studentDetail);
			moduleNames = moduleNames + module.getModuleName() + ";";
		}

		List<Module> modules = moduleRepository.findAll();
		for (Module module : modules) {
			if (!moduleNames.contains(module.getModuleName())) {

				StudentDTO studentDetail = new StudentDTO();
				studentDetail.setModuleId(module.getModuleId());
				studentDetail.setModuleName(module.getModuleName());

				studentDetails.add(studentDetail);
			}
		}
		return studentDetails;
	}
}
