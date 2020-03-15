package com.account.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.account.model.User;
import com.account.model.UserType;
import com.account.model.dto.AttendanceDTO;
import com.account.repository.UserRepository;
import com.account.repository.UserTypeRepository;
import com.account.service.UserService;
import com.account.util.Constant;
import com.account.util.EmailUtil;
import com.account.util.RandomString;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/api")
public class Controller {

	private static final Logger log = LoggerFactory.getLogger(Controller.class);

	@Autowired
	private UserService accountService;

	@Autowired
	private UserTypeRepository userTypeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JavaMailSender sender;

	// @Value("${port}")
	// public String port;
	//
	// @Value("${fromEmail}")
	// public String fromEmail;
	//
	// @Value("${password}")
	// public String mailPassword;
	//
	// @Value("${host}")
	// public String host;

	@PostMapping("login")
	public User login(@RequestBody User account) {
		log.info(account.getUsername());
		log.info(account.getPassword());

		User user = accountService.getUserByName(account);
		// log.info(user.getPassword());

		if (user != null && user.getPassword().trim().equals(account.getPassword().trim()) && user.getLocked() != null
				&& user.getLocked().trim().equals(Constant.ACTIVE)) {
			log.info(user.getLocked());
			return user;
		}
		return null;
	}

	@PostMapping("logout")
	public String logout(@RequestBody User account) {
		return Constant.STATUS_SUCCESS;
	}

	@PostMapping("forgot")
	public User forgot(@RequestBody User account) {
		User user = accountService.getUserByName(account);
		if (user != null && user.getLocked() != null && user.getLocked().equals(Constant.ACTIVE)) {
			RandomString randomString = new RandomString(8, ThreadLocalRandom.current());
			String password = randomString.nextString();
			user.setPassword(password);
			userRepository.save(user);
			try {
				sendForgotPassword(user);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return user;
		}

		return null;
	}

	@PostMapping("saveuser")
	public boolean saveAccount(@RequestBody User account) {
		boolean status = false;
		User user = accountService.getUserByName(account);
		if (user == null) {
			RandomString randomString = new RandomString(8, ThreadLocalRandom.current());
			String password = randomString.nextString();
			account.setPassword(password);
			account.setLocked(Constant.ACTIVE);
			Date date = new Date();
			account.setCreatedAt(new Timestamp(date.getTime()));
			try {
				sendEmail(account);
			} catch (Exception e) {
				e.printStackTrace();
			}
			status = accountService.saveUser(account);
		}
		return status;
	}

	@GetMapping("userlist")
	public List<User> allUsers() {
		List<User> users = accountService.getUsers();

		for (User user : users) {
			Long id = user.getUserTypeId();
			UserType userType = userTypeRepository.findByUserTypeId(id);
			user.setRoleName(userType.getUserTypeName());

		}

		return accountService.getUsers();
	}

	@DeleteMapping("deleteuser/{userid}")
	public boolean deleteUser(@PathVariable("userid") int userid, User account) {
		account.setUserId(userid);
		return accountService.deleteUser(account);
	}

	@GetMapping("user/{userid}")
	public List<User> allUserByID(@PathVariable("userid") int userid) {

		User account = new User();
		account.setUserId(userid);
		List<User> accounts = accountService.getUserById(account);
		return accounts;

	}

	@PostMapping("updateuser/{userid}")
	public boolean updateAccount(@RequestBody User account, @PathVariable("userid") int userid) {
		boolean status = false;
		User userExist = userRepository.findByUserId(userid);
		if (userExist != null) {
			account.setUserId(userid);
			account.setPassword(userExist.getPassword());
			account.setLocked(userExist.getLocked());
			account.setFingerPrint(userExist.getFingerPrint());
			account.setUpdatedAt(new Timestamp(new Date().getTime()));
			status = accountService.updateUser(account);
		}

		return status;
	}

	@PostMapping("lockuser/{userid}")
	public boolean lockUser(@RequestBody User account, @PathVariable("userid") int userid) {
		boolean status = false;
		User userExist = userRepository.findByUserId(userid);
		if (userExist != null) {
			userExist.setLocked(Constant.LOCKED);
			userExist.setUpdatedAt(new Timestamp(new Date().getTime()));
			status = accountService.lockUser(userExist);
		}
		return status;
	}

	@PostMapping("unlockuser/{userid}")
	public boolean unlockUser(@RequestBody User account, @PathVariable("userid") int userid) {
		boolean status = false;
		User userExist = userRepository.findByUserId(userid);
		if (userExist != null) {
			userExist.setLocked(Constant.ACTIVE);
			userExist.setUpdatedAt(new Timestamp(new Date().getTime()));
			status = accountService.lockUser(userExist);
		}
		return status;
	}

	@PostMapping("fingerprint/{userid}")
	public boolean FingerPrint(@RequestBody User account, @PathVariable("userid") int userid) {
		boolean status = false;
		User userExist = userRepository.findByUserId(userid);
		if (userExist != null) {
			account.setUserId(userid);
			account.setUsername(userExist.getUsername());
			account.setPassword(userExist.getPassword());
			account.setLocked(userExist.getLocked());
			account.setEmailAddress(userExist.getEmailAddress());
			account.setUserTypeId(userExist.getUserTypeId());
			account.setUpdatedAt(new Timestamp(new Date().getTime()));
			account.setFingerPrint(account.getFingerPrint());
			status = accountService.fingerPrint(account);
		}

		return status;
	}

	@PostMapping("changePassword/{userid}")
	public boolean ChangePassword(@RequestBody User account, @PathVariable("userid") int userid) {
		boolean status = false;

		if (account.getPassword().equals(account.getReEnterPassword())) {
			User userExist = userRepository.findByUserId(userid);
			if (userExist != null) {

				account.setUserId(userid);
				account.setUsername(userExist.getUsername());
				account.setPassword(account.getPassword());
				account.setLocked(userExist.getLocked());
				account.setEmailAddress(userExist.getEmailAddress());
				account.setUserTypeId(userExist.getUserTypeId());
				account.setUpdatedAt(new Timestamp(new Date().getTime()));
				account.setFingerPrint(userExist.getFingerPrint());
				account.setUpdatedAt(new Timestamp(new Date().getTime()));
				try {
					changePassword(account);
				} catch (Exception e) {
					e.printStackTrace();
				}
				status = accountService.changePassword(account);
			}
		}

		return status;
	}

	@GetMapping("sendemail/{users}/{moduleName}")
	public boolean attendEmail(@PathVariable("users") String users, @PathVariable("moduleName") String moduleName) {
		boolean status = true;

	//	System.out.println(users);
	//	System.out.println(moduleName);

		String[] userArr = users.split(",");
		for (String user : userArr) {
			try {
				User userFound = userRepository.findByUsername(user.trim());
				if (userFound != null) {
					sendEmailAttendance(userFound, moduleName);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return status;
	}

	private void sendEmail(User user) throws Exception {
		MimeMessage message = sender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		// helper.setTo("sivakarthika128@gmail.com");
		helper.setTo(user.getEmailAddress());
		helper.setText("Dear " + user.getUsername() + "," + "\n\n" + "Your Password: " + user.getPassword() + "\n\n"
				+ " Regards" + "\n\n" + " Bsar");
		helper.setSubject("User Registration");

		sender.send(helper.getMimeMessage());
	}

	private void sendForgotPassword(User user) throws Exception {
		MimeMessage message = sender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		// helper.setTo("sivakarthika128@gmail.com");
		helper.setTo(user.getEmailAddress());
		helper.setText("Dear " + user.getUsername() + "," + "\n\n" + "Your Password is Reset: " + user.getPassword()
				+ "\n\n" + " Regards" + "\n\n" + " Bsar");
		helper.setSubject("Forgot Password");

		sender.send(helper.getMimeMessage());
	}

	private void changePassword(User user) throws Exception {
		MimeMessage message = sender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setTo(user.getEmailAddress());
		helper.setText("Dear " + user.getUsername() + "," + "\n\n" + "Your Password is changed: " + user.getPassword()
				+ "\n\n" + " Regards" + "\n\n" + " Bsar");
		helper.setSubject("Password Change");

		sender.send(helper.getMimeMessage());
	}

	private void sendEmailAttendance(User user, String module) throws Exception {
		MimeMessage message = sender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setTo(user.getEmailAddress());
		helper.setText("Dear " + user.getUsername() + "," + "\n\n" + "You have not attended the activities of the : "
				+ module + "\n\n" + " Regards" + "\n\n" + " Bsar");
		helper.setSubject("Non Attendance");

		sender.send(helper.getMimeMessage());
	}

}
