package com.account.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.bind.DatatypeConverter;

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

		try {
			List<User> users = userRepository.findAll();

			for (User user : users) {

				if (user != null && user.getFingerPrint() != null && atten.getFingerPrint() != null) {
					
					
					String path = System.getProperty("java.io.tmpdir");
					
					System.out.println(path);

					String img1 = user.getUsername() + "probe." + saveImage(user.getFingerPrint(), path + user.getUsername()+ "probe.");

					String img2 =  user.getUsername() + "candidate."
							+ saveImage(atten.getFingerPrint(), path + user.getUsername() + "candidate.");
					

					FingerprintTemplate probe = new FingerprintTemplate(new FingerprintImage().dpi(500)
							.decode(Files.readAllBytes(Paths.get(path + img1))));
					
					FingerprintTemplate candidate = new FingerprintTemplate(new FingerprintImage().dpi(500)
							.decode(Files.readAllBytes(Paths.get(path + img2))));
					

					double score = new FingerprintMatcher().index(probe).match(candidate);

					double threshold = 40;
					boolean matches = score >= threshold;

					System.out.println(matches);

					if (matches) {

						List<Attendance> attendances = attenRepository
								.findByUserIdAndModuleIdAndModuleActivityIdAndModuleScheduleId(user.getUserId(),
										atten.getModuleId(), atten.getModuleActivityId(), atten.getModuleScheduleId());
						
						
						if (attendances.isEmpty()) {
							atten.setUserId(user.getUserId());
							status = attenService.saveAttendance(atten);

						} else {

							status = true;
						}
						break;

					} else {
						status = false;
					}

				}
			}
		} catch (Exception e) {

			// status = attenService.saveAttendance(atten);
			e.printStackTrace();

		}

		return status;
	}

	@GetMapping("stuatten/{userid}/{fromDate}/{toDate}/{percentage}")
	public List<AttendanceDTO> attendance(@PathVariable("userid") int userid, @PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate, @PathVariable("percentage") int percentage) throws ParseException {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DecimalFormat df2 = new DecimalFormat("#.##");
		UserModule usModule = new UserModule();
		usModule.setUserId(userid);
		Integer totalActivities = 0;

		List<AttendanceDTO> attenDetails = new ArrayList<AttendanceDTO>();

		List<UserModule> userModules = userModuleService.getUserModule(usModule);
		for (UserModule userModule : userModules) {

			List<UserModule> usermodules = userModuleRepository.findByModuleId(userModule.getModuleId());

			String usersAttended = "";
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

				
				boolean scheduleFound = true;
				List<ModuleSchedule> moduleSchedules = moduleScheduleRepository
						.findBymoduleActivityId(moduleActivity.getModuleActivityId());

				if (moduleSchedules.size() == 0) {
					scheduleFound = false;
				}
				for (ModuleSchedule moduleSchedule : moduleSchedules) {
					
					usersAttended = "";
					List<Attendance> attendances = attenRepository
							.findByModuleActivityIdAndModuleScheduleId(moduleActivity.getModuleActivityId(),moduleSchedule.getModuleScheduleId());
					for (Attendance attendance : attendances) {
						User user = userRepository.findByUserId(attendance.getUserId());
						if (user.getUserTypeId() == 3) {
							usersAttended = usersAttended + user.getUsername() + ", ";
						}
					}

					if (usersAttended.length() > 2) {
						usersAttended = usersAttended.substring(0, usersAttended.length() - 2);
					}

					String[] assigned = assignedUsers.split(",");
					notAttended = "";
					for (String assign : assigned) {
						if (!usersAttended.contains(assign.trim())) {
							notAttended = notAttended + assign + ", ";
						}
					}
					if (notAttended.length() > 2) {
						notAttended = notAttended.substring(0, notAttended.length() - 2);
					}


					scheduleFound = true;
					if (fromDate != null && toDate != null && !fromDate.equals("null") && !toDate.equals("null")) {
						if (scheduleFound && moduleSchedule.getModuleScheduled() != null
								&& df.parse(fromDate).after(df.parse(new SimpleDateFormat("yyyy-MM-dd")
										.format(zeroTime(moduleSchedule.getModuleScheduled()))))) {
							scheduleFound = false;
						}
						if (scheduleFound && moduleSchedule.getModuleScheduled() != null
								&& df.parse(toDate).before(df.parse(new SimpleDateFormat("yyyy-MM-dd")
										.format(zeroTime(moduleSchedule.getModuleScheduled()))))) {
							scheduleFound = false;
						}
					}

					AttendanceDTO attenDetail = new AttendanceDTO();
					attenDetail.setUserId(userModule.getUserId());
					attenDetail.setModuleId(userModule.getModuleId());
					attenDetail.setModuleName(module.getModuleName());
					attenDetail.setModuleCode(module.getModuleCode());
					attenDetail.setModuleActivity(moduleActivity.getModuleActivity());
					attenDetail.setActivityId(moduleActivity.getModuleActivityId());
					attenDetail.setNonAttended(notAttended);

					attenDetail.setAttended(usersAttended);
					attenDetail.setAssigned(assignedUsers);
					attenDetail.setModuleSchedule(moduleSchedule.getModuleScheduled());
					attenDetail.setScheduleId(moduleSchedule.getModuleScheduleId());
					if (scheduleFound) {
						attenDetails.add(attenDetail);
					}
				}

			}
		}

		totalActivities = attenDetails.size();
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (AttendanceDTO attendanceDto : attenDetails) {
			String[] nonAttend = attendanceDto.getNonAttended().split(",");
			for (String assign : nonAttend) {
				int count = 1;
				if (map.get(assign.trim()) != null) {
					count = map.get(assign.trim()) + 1;
				}
				map.put(assign.trim(), count);
			}
		}
		Integer[] arr = new Integer[map.size()];
		String belowPercent = "";
		String belowList = "";
		Iterator it = map.entrySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();

//			 System.out.println(pair.getKey());
//			 System.out.println(pair.getValue());
//			 System.out.println(totalActivities);
//			 System.out.println(totalActivities - (Integer) pair.getValue());
//			 System.out.println((double) (totalActivities - (Integer)
//			 pair.getValue()) / totalActivities);
			Double perc = (double) (totalActivities - (Integer) pair.getValue()) / totalActivities * 100;
			if (perc < percentage) {
				belowPercent = belowPercent + "" + (String) pair.getKey();
				belowPercent = belowPercent + "("
						+ (df2.format(((double) (totalActivities - (Integer) pair.getValue()) / totalActivities) * 100))
						+ " %)" + ", ";
				belowList = belowList + "" + (String) pair.getKey() + ", ";
			}
			i++;
		}
		if (belowList.length() > 2) {
			belowList = belowList.substring(0, belowList.length() - 2);
		}
		if (belowPercent.length() > 2) {
			belowPercent = belowPercent.substring(0, belowPercent.length() - 2);
		}
		i = 0;
		for (AttendanceDTO attendanceDto : attenDetails) {
			if (i == 0) {
				attendanceDto.setBelowPercent(belowPercent);
				attendanceDto.setBelowList(belowList);
			}
			i++;
		}
		return attenDetails;
	}

	public Date zeroTime(final Date date) {
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-Mm-dd HH:mm:ss");
		// not SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
		Calendar calendarDM = Calendar.getInstance();
		calendarDM.setTime(date);
		calendarDM.set(Calendar.HOUR, 0);
		calendarDM.set(Calendar.MINUTE, 0);
		calendarDM.set(Calendar.SECOND, 0);
		return calendarDM.getTime();

	}

	public String saveImage(String base, String filePath) {
		String base64String = base;
		String[] strings = base64String.split(",");
		String extension;
		switch (strings[0]) {// check image's extension
		case "data:image/jpeg;base64":
			extension = "jpeg";
			break;
		case "data:image/png;base64":
			extension = "png";
			break;
		default:// should write cases for more images types
			extension = "jpg";
			break;
		}
		// convert base64 string to binary data
		byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);
		String path = filePath + extension;
		File file = new File(path);
		try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
			outputStream.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return extension;
	}

}
