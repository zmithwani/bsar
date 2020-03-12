package com.account.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.account.model.Attendance;
import com.account.model.Module;
import com.account.model.ModuleActivity;
import com.account.model.ModuleSchedule;
import com.account.model.User;
import com.account.model.UserModule;
import com.account.model.UserType;
import com.account.model.dto.AttendanceDTO;
import com.account.model.dto.StudentDTO;
import com.account.repository.AttendanceRepository;
import com.account.repository.ModuleActivityRepository;
import com.account.repository.ModuleRepository;
import com.account.repository.ModuleScheduleRepository;
import com.account.repository.UserModuleRepository;
import com.account.repository.UserRepository;
import com.account.repository.UserTypeRepository;
import com.account.service.AttendanceService;
import com.account.service.UserModuleService;
import com.machinezoo.sourceafis.FingerprintImage;
import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/api")
public class AttendanceController {

	private static final Logger log = LoggerFactory.getLogger(Controller.class);

	@Autowired
	private AttendanceRepository attenRepository;

	@Autowired
	private AttendanceService attenService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserTypeRepository userTypeRepository;

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

	@PostMapping("saveattendance")
	public boolean saveAccount(@RequestBody Attendance atten) {
		boolean status = false;

		System.out.println(atten.getFingerPrint());

		User user = userRepository.findByUserId(atten.getUserId());

		if (user != null && user.getFingerPrint() != null && atten.getFingerPrint() != null) {

			byte[] probeImage;
			try {
				probeImage = user.getFingerPrint().getBytes();

				byte[] candidateImage = atten.getFingerPrint().getBytes();

				FingerprintTemplate probe = new FingerprintTemplate(new FingerprintImage().dpi(500).decode(probeImage));

				FingerprintTemplate candidate = new FingerprintTemplate(
						new FingerprintImage().dpi(500).decode(candidateImage));

				double score = new FingerprintMatcher().index(probe).match(candidate);

				double threshold = 40;
				boolean matches = score >= threshold;

				if (matches) {
					status = attenService.saveAttendance(atten);
				}

			} catch (Exception e) {

				e.printStackTrace();
			}

		}

		return status;
	}

	@GetMapping("stuatten/{userid}/{fromDate}/{toDate}")
	public List<AttendanceDTO> attendance(@PathVariable("userid") int userid, @PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate) throws ParseException {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		UserModule usModule = new UserModule();
		usModule.setUserId(userid);

		List<AttendanceDTO> attenDetails = new ArrayList<AttendanceDTO>();

		List<UserModule> userModules = userModuleService.getUserModule(usModule);
		for (UserModule userModule : userModules) {

			List<UserModule> usermodules = userModuleRepository.findByModuleId(userModule.getModuleId());

			String assignedUsers = "";
			String assignedUser = "";
			String notAttended = "";

			for (UserModule usrModule : usermodules) {
				User user = userRepository.findByUserId(usrModule.getUserId());

				if (user != null && user.getUserTypeId() == 3) {
					assignedUser = assignedUser + user.getUsername() + ", ";
				}

			}
			assignedUsers = assignedUser;
			if (assignedUsers.length() > 2) {
				assignedUsers = assignedUsers.substring(0, assignedUsers.length() - 2);
			}
			notAttended = assignedUsers;

			Module module = moduleRepository.findByModuleId(userModule.getModuleId());

			List<ModuleActivity> moduleActivities = moduleActivityRepository.findByModuleId(userModule.getModuleId());
			for (ModuleActivity moduleActivity : moduleActivities) {
				AttendanceDTO attenDetail = new AttendanceDTO();
				attenDetail.setUserId(userModule.getUserId());
				attenDetail.setModuleId(userModule.getModuleId());
				attenDetail.setModuleName(module.getModuleName());
				attenDetail.setModuleCode(module.getModuleCode());
				attenDetail.setModuleActivity(moduleActivity.getModuleActivity());
				attenDetail.setActivityId(moduleActivity.getModuleActivityId());
				attenDetail.setAssigned(assignedUsers);
				attenDetail.setNonAttended(notAttended);

				boolean scheduleFound = true;
				List<ModuleSchedule> moduleSchedules = moduleScheduleRepository
						.findBymoduleActivityId(moduleActivity.getModuleActivityId());

				if (moduleSchedules.size() == 0) {
					scheduleFound = false;
				}
				for (ModuleSchedule moduleSchedule : moduleSchedules) {

					if (fromDate != null && toDate != null && !fromDate.equals("null") && !toDate.equals("null")) {
						if (scheduleFound && moduleSchedule.getModuleScheduled() != null
								&& df.parse(fromDate).after(df.parse(new SimpleDateFormat("yyyy-MM-dd")
										.format(moduleSchedule.getModuleScheduled())))) {
							scheduleFound = false;
						}
						if (scheduleFound && moduleSchedule.getModuleScheduled() != null
								&& df.parse(toDate).before(df.parse(new SimpleDateFormat("yyyy-MM-dd")
										.format(moduleSchedule.getModuleScheduled())))) {
							scheduleFound = false;
						}
					}
					attenDetail.setModuleSchedule(moduleSchedule.getModuleScheduled());
					attenDetail.setScheduleId(moduleSchedule.getModuleScheduleId());
				}
				if (scheduleFound) {
					attenDetails.add(attenDetail);
				}
			}
		}
		return attenDetails;
	}
}
