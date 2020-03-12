package com.account.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.account.model.Attendance;
import com.account.model.Module;
import com.account.model.ModuleActivity;
import com.account.model.ModuleSchedule;
import com.account.model.User;
import com.account.model.UserModule;
import com.account.model.dto.StudentDTO;
import com.account.repository.AttendanceRepository;
import com.account.repository.ModuleActivityRepository;
import com.account.repository.ModuleRepository;
import com.account.repository.ModuleScheduleRepository;
import com.account.repository.UserModuleRepository;
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
	private UserModuleRepository userModuleRepository;

	@Autowired
	private ModuleActivityRepository moduleActivityRepository;

	@Autowired
	private ModuleScheduleRepository moduleScheduleRepository;

	@Autowired
	private AttendanceRepository attenRepository;

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
				studentDetail.setModuleCode(module.getModuleCode());
				studentDetail.setModuleActivity(moduleActivity.getModuleActivity());
				studentDetail.setActivityId(moduleActivity.getModuleActivityId());

				List<ModuleSchedule> moduleSchedules = moduleScheduleRepository
						.findBymoduleActivityId(moduleActivity.getModuleActivityId());

				for (ModuleSchedule moduleSchedule : moduleSchedules) {

					studentDetail.setModuleSchedule(moduleSchedule.getModuleScheduled());
					studentDetail.setScheduleId(moduleSchedule.getModuleScheduleId());
				}

				List<Attendance> attendance = attenRepository
						.findByModuleActivityId(moduleActivity.getModuleActivityId());
				for (Attendance atten : attendance) {
					studentDetail.setFingerPrint(atten.getFingerPrint());
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

	@PostMapping("saveusermodule")
	public boolean moduleUser(@RequestParam int userId, String moduleName) {
		boolean status = false;
		List<UserModule> userModules = userModuleService.getUserModuleByUserId(userId);
		for (UserModule module : userModules) {
			status = userModuleService.deleteUserModuleByUserId(module);
		}

		String[] moduleNames = moduleName.split(",");
		for (int i = 0; i < moduleNames.length; i++) {
			Module module = moduleRepository.findByModuleName(moduleNames[i]);
			UserModule userModule = new UserModule();
			userModule.setUserId(userId);
			userModule.setModuleId(module.getModuleId());
			Date date = new Date();
			userModule.setCreatedAt(new Timestamp(date.getTime()));
			status = userModuleService.saveUserModule(userModule);
		}

		return status;
	}

}
