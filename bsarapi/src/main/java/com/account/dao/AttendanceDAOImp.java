package com.account.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.account.model.Attendance;
import com.account.repository.AttendanceRepository;

@Repository
public class AttendanceDAOImp implements AttendanceDAO {

	@Autowired
	private AttendanceRepository attenRepository;

	@Override
	public boolean saveAttendance(Attendance atten) {
		boolean status = false;
		try {
			attenRepository.save(atten);
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

}
