package com.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.account.dao.AttendanceDAO;
import com.account.model.Attendance;


@Service
@Transactional
public class AttendanceServiceImp implements AttendanceService {

	
	@Autowired
	private AttendanceDAO attendao;
	
	public boolean saveAttendance(Attendance atten) {
		return attendao.saveAttendance(atten);
	}

}
