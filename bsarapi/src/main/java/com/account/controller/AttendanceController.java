package com.account.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.account.model.dto.AttendanceDTO;
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

					String img1 = user.getUsername() + "probe."
							+ saveImage(user.getFingerPrint(), path + user.getUsername() + "probe.");

					String img2 = user.getUsername() + "candidate."
							+ saveImage(atten.getFingerPrint(), path + user.getUsername() + "candidate.");

					FingerprintTemplate probe = new FingerprintTemplate(
							new FingerprintImage().dpi(500).decode(Files.readAllBytes(Paths.get(path + img1))));

					FingerprintTemplate candidate = new FingerprintTemplate(
							new FingerprintImage().dpi(500).decode(Files.readAllBytes(Paths.get(path + img2))));

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
			String assignedUsersModules = "";
			String assignedUser = "";
			String assignedUserModule = "";
			String notAttended = "";
			String notAttendedModuleWise = "";

			for (UserModule usrModule : usermodules) {
				User user = userRepository.findByUserId(usrModule.getUserId());

				if (user != null && user.getUserTypeId() == 3) {
					assignedUser = assignedUser + user.getUsername() + ", ";
					assignedUserModule = assignedUserModule + user.getUsername() + "-" + usrModule.getModuleId() + ", ";

				}

			}
			assignedUsers = assignedUser;
			assignedUsersModules = assignedUserModule;
			if (assignedUsers.length() > 2) {
				assignedUsers = assignedUsers.substring(0, assignedUsers.length() - 2);
			}
			if (assignedUsersModules.length() > 2) {
				assignedUsersModules = assignedUsersModules.substring(0, assignedUsersModules.length() - 2);
			}
			notAttended = assignedUsers;
			notAttendedModuleWise = assignedUsersModules;

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

					notAttendedModuleWise = "";
					List<Attendance> attendances = attenRepository.findByModuleActivityIdAndModuleScheduleId(
							moduleActivity.getModuleActivityId(), moduleSchedule.getModuleScheduleId());
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

							Module moduleFound = moduleRepository.findByModuleId(moduleActivity.getModuleId());
							if (moduleFound != null) {
								notAttendedModuleWise = notAttendedModuleWise + assign + "-"
										+ moduleFound.getModuleName() + ", ";
							}
						}
					}
					if (notAttended.length() > 2) {
						notAttended = notAttended.substring(0, notAttended.length() - 2);
					}
					if (notAttendedModuleWise.length() > 2) {
						notAttendedModuleWise = notAttendedModuleWise.substring(0, notAttendedModuleWise.length() - 2);
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
					attenDetail.setNonAttendedModuleWise(notAttendedModuleWise);

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
			// String[] nonAttend = attendanceDto.getNonAttended().split(",");
			String[] nonAttend = attendanceDto.getNonAttendedModuleWise().split(",");
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

			String name = (String) pair.getKey();
			if (name.indexOf("-") > 0) {
				name = name.substring(0, name.indexOf("-"));
			}

			User user = userRepository.findByUsername(name);
			UserModule usModule1 = new UserModule();
			usModule1.setUserId(user.getUserId());
			List<UserModule> userModules1 = userModuleService.getUserModule(usModule1);
			for (UserModule userModule : userModules1) {
				// Double perc = (double) (getActivitesCount((String)
				// pair.getKey(), fromDate, toDate,
				// userModule.getModuleId()) - (Integer) pair.getValue())
				// / getActivitesCount((String) pair.getKey(), fromDate, toDate,
				// userModule.getModuleId()) * 100;
				Double perc = (double) ((Integer) pair.getValue())
						/ getActivitesCount(name, fromDate, toDate, userModule.getModuleId()) * 100;
				if (perc <= percentage) {
					String temp1 = "";
					temp1 = temp1 + "" + (String) pair.getKey();
					Module mod = moduleRepository.findByModuleId(userModule.getModuleId());
					// if (mod != null) {
					// belowPercent = belowPercent + " (";
					// }
					temp1 = temp1 + " ("
							+ (df2.format(((double) (getActivitesCount(name, fromDate, toDate, userModule.getModuleId())
									- (Integer) pair.getValue())
									/ getActivitesCount(name, fromDate, toDate, userModule.getModuleId())) * 100))
							+ " %)" + ", ";
					if (!belowPercent.contains(temp1)) {
						belowPercent = belowPercent + temp1;
					}
					String temp = (String) pair.getKey();
					if (temp.indexOf("-") > 0) {
						temp = temp.substring(0, temp.indexOf("-"));
					}
					if (!belowList.contains(temp)) {
						belowList = belowList + "" + temp + ", ";
					}
				}
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
			// if (i == 0) {
			attendanceDto.setBelowPercent(belowPercent);
			attendanceDto.setBelowList(belowList);
			// }
			i++;
		}
		return attenDetails;
	}

	@GetMapping("stuattensumm/{userid}/{fromDate}/{toDate}/{percentage}")
	public List<AttendanceDTO> attendanceSummary(@PathVariable("userid") int userid,
			@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate,
			@PathVariable("percentage") int percentage) throws ParseException {

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
			String assignedUsersModules = "";
			String assignedUser = "";
			String assignedUserModule = "";
			String notAttended = "";
			String notAttendedModuleWise = "";

			for (UserModule usrModule : usermodules) {
				User user = userRepository.findByUserId(usrModule.getUserId());

				if (user != null && user.getUserTypeId() == 3) {
					assignedUser = assignedUser + user.getUsername() + ", ";
					assignedUserModule = assignedUserModule + user.getUsername() + "-" + usrModule.getModuleId() + ", ";

				}

			}
			assignedUsers = assignedUser;
			assignedUsersModules = assignedUserModule;
			if (assignedUsers.length() > 2) {
				assignedUsers = assignedUsers.substring(0, assignedUsers.length() - 2);
			}
			if (assignedUsersModules.length() > 2) {
				assignedUsersModules = assignedUsersModules.substring(0, assignedUsersModules.length() - 2);
			}
			notAttended = assignedUsers;
			notAttendedModuleWise = assignedUsersModules;

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

					notAttendedModuleWise = "";
					List<Attendance> attendances = attenRepository.findByModuleActivityIdAndModuleScheduleId(
							moduleActivity.getModuleActivityId(), moduleSchedule.getModuleScheduleId());
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

							Module moduleFound = moduleRepository.findByModuleId(moduleActivity.getModuleId());
							if (moduleFound != null) {
								notAttendedModuleWise = notAttendedModuleWise + assign + "-"
										+ moduleFound.getModuleName() + ", ";
							}
						}
					}
					if (notAttended.length() > 2) {
						notAttended = notAttended.substring(0, notAttended.length() - 2);
					}
					if (notAttendedModuleWise.length() > 2) {
						notAttendedModuleWise = notAttendedModuleWise.substring(0, notAttendedModuleWise.length() - 2);
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
					attenDetail.setNonAttendedModuleWise(notAttendedModuleWise);

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
			// String[] nonAttend = attendanceDto.getNonAttended().split(",");
			String[] nonAttend = attendanceDto.getNonAttendedModuleWise().split(",");
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

			String name = (String) pair.getKey();
			if (name.indexOf("-") > 0) {
				name = name.substring(0, name.indexOf("-"));
			}

			User user = userRepository.findByUsername(name);
			UserModule usModule1 = new UserModule();
			usModule1.setUserId(user.getUserId());
			List<UserModule> userModules1 = userModuleService.getUserModule(usModule1);
			for (UserModule userModule : userModules1) {
				// Double perc = (double) (getActivitesCount((String)
				// pair.getKey(), fromDate, toDate,
				// userModule.getModuleId()) - (Integer) pair.getValue())
				// / getActivitesCount((String) pair.getKey(), fromDate, toDate,
				// userModule.getModuleId()) * 100;
				Double perc = (double) ((Integer) pair.getValue())
						/ getActivitesCount(name, fromDate, toDate, userModule.getModuleId()) * 100;
				if (perc <= percentage) {
					String temp1 = "";
					temp1 = temp1 + "" + (String) pair.getKey();
					Module mod = moduleRepository.findByModuleId(userModule.getModuleId());
					// if (mod != null) {
					// belowPercent = belowPercent + " (";
					// }
					temp1 = temp1 + " ("
							+ (df2.format(((double) (getActivitesCount(name, fromDate, toDate, userModule.getModuleId())
									- (Integer) pair.getValue())
									/ getActivitesCount(name, fromDate, toDate, userModule.getModuleId())) * 100))
							+ " %)" + ", ";
					if (!belowPercent.contains(temp1)) {
						belowPercent = belowPercent + temp1;
					}
					String temp = (String) pair.getKey();
					if (temp.indexOf("-") > 0) {
						temp = temp.substring(0, temp.indexOf("-"));
					}
					if (!belowList.contains(temp)) {
						belowList = belowList + "" + temp + ", ";
					}
				}
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
		HashMap<String, String> module = new HashMap<String, String>();
		List<AttendanceDTO> attendances = new ArrayList<AttendanceDTO>();
		for (AttendanceDTO attendanceDto : attenDetails) {
			attendanceDto.setBelowPercent(belowPercent);
			attendanceDto.setBelowList(belowList);

			if (module.get(attendanceDto.getModuleName()) == null) {

				module.put(attendanceDto.getModuleName(), attendanceDto.getModuleName());
				String name = module.get(attendanceDto.getModuleName());
				String tempPerc = "";
				String tempBelowLi = "";
				if (attendanceDto.getBelowPercent() != null) {
					String[] belowPercentSu = attendanceDto.getBelowPercent().split(",");
					for (String str : belowPercentSu) {

						String temp = str.substring(str.indexOf("-")+1, str.indexOf("("));
						String student = str.substring(0, str.indexOf("-"));
						if (temp.trim().equals(attendanceDto.getModuleName()) && name.equals(attendanceDto.getModuleName())) {
							tempPerc = tempPerc + str + ", ";
							tempBelowLi = tempBelowLi + student + ", ";
						}
					}
					if (tempPerc.length() > 2) {
						tempPerc = tempPerc.substring(0, tempPerc.length() - 2);
					}
					if (tempBelowLi.length() > 2) {
						tempBelowLi = tempBelowLi.substring(0, tempBelowLi.length() - 2);
					}
					attendanceDto.setBelowPercent(tempPerc);
					tempPerc = tempPerc.replaceAll(" ","");
					tempPerc = tempPerc.replaceAll("%","");
					attendanceDto.setBelowList(tempPerc);
				}
				attendances.add(attendanceDto);
			}

			// if (i == 0) {

			// }
			i++;
		}
		return attendances;
	}

	private Integer getActivitesCount(String userName, String fromDate, String toDate, int moduleId) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		User user = userRepository.findByUsername(userName);
		UserModule usModule = new UserModule();
		usModule.setUserId(user.getUserId());
		Integer totalActivities = 0;

		List<AttendanceDTO> attenDetails = new ArrayList<AttendanceDTO>();

		List<UserModule> userModules = userModuleService.getUserModule(usModule);
		for (UserModule userModule : userModules) {
			if (userModule.getModuleId() != moduleId) {
				continue;
			}

			List<ModuleActivity> moduleActivities = moduleActivityRepository.findByModuleId(userModule.getModuleId());

			for (ModuleActivity moduleActivity : moduleActivities) {

				boolean scheduleFound = true;
				List<ModuleSchedule> moduleSchedules = moduleScheduleRepository
						.findBymoduleActivityId(moduleActivity.getModuleActivityId());

				if (moduleSchedules.size() == 0) {
					scheduleFound = false;
				}
				for (ModuleSchedule moduleSchedule : moduleSchedules) {

					scheduleFound = true;
					try {
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
					} catch (Exception e) {
						scheduleFound = false;
					}

					if (scheduleFound) {
						totalActivities = totalActivities + 1;
					}
				}

			}
		}

		return totalActivities;
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
