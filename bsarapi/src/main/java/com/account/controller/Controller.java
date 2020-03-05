package com.account.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${port}")
	public String port;

	@Value("${fromEmail}")
	public String fromEmail;

	@Value("${password}")
	public String mailPassword;

	@Value("${host}")
	public String host;

	@PostMapping("login")
	public User login(@RequestBody User account) {
		log.info(account.getUsername());
		log.info(account.getPassword());
		User user = accountService.getUserByName(account);
		log.info(user.getPassword());
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
				sendEmailReset(user);
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
				sendCreateUser(account);
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

		System.out.println(accounts.size());
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

	private void sendCreateUser(User user) throws Exception {

		ExecutorService emailExecutor = Executors.newCachedThreadPool();
		emailExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {

					String subject = "Account Registration";
					String body = "Dear " + user.getUsername() + "," + "<br /> <br />"
							+ "Your username and password are " + user.getUsername() + " and " + user.getPassword()
							+ "<br /> <br />" + "Thank You";
					EmailUtil emailUtil = new EmailUtil();
					String stat = emailUtil.sendEmail(host, fromEmail, mailPassword, user.getEmailAddress(), port,
							subject, body);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	private void sendEmailReset(User user) throws Exception {

		ExecutorService emailExecutor = Executors.newCachedThreadPool();
		emailExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {

					String subject = "Password reset";
					String body = "Dear " + user.getUsername() + "," + "<br /> <br />" + "Your password is reset to "
							+ user.getPassword() + "." + "<br /> <br />" + "Thank You";
					EmailUtil emailUtil = new EmailUtil();
					String stat = emailUtil.sendEmail(host, fromEmail, mailPassword, user.getEmailAddress(), port,
							subject, body);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

}
