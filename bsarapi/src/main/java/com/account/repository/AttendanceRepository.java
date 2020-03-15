package com.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.account.model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	List<Attendance> findByModuleActivityId(int moduleActivityId);


	List<Attendance> findByUserIdAndModuleActivityId(int userId, int moduleActivityId);


	List<Attendance> findByUserId(int userId);


	List<Attendance> findByUserIdAndModuleActivityIdAndModuleScheduleId(int userId, int moduleActivityId,
			int moduleScheduleId);

}
