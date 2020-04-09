package com.account.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.account.model.Module;
import com.account.model.ModuleActivity;
import com.account.model.ModuleSchedule;
import com.account.model.User;
import com.account.repository.ModuleActivityRepository;
import com.account.repository.ModuleRepository;
import com.account.repository.ModuleScheduleRepository;

@Repository
public class ModuleDAOImp implements ModuleDAO {

	@Autowired
	private ModuleRepository moduleRepository;

	@Autowired
	private ModuleActivityRepository moduleActivityRepository;
	@Autowired
	private ModuleScheduleRepository moduleScheduleRepository;

	@Override
	public boolean saveModule(Module account) {
		boolean status = false;
		try {
			moduleRepository.save(account);
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public boolean saveModuleActivity(ModuleActivity module) {
		boolean status = false;
		try {
			moduleActivityRepository.save(module);
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public boolean saveModuleSchedule(ModuleSchedule module) {
		boolean status = false;
		try {
			moduleScheduleRepository.save(module);
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public Module getModuleByName(Module module) {
		Module list = moduleRepository.findByModuleName(module.getModuleName());
		return list;
	}

	@Override
	public ModuleActivity getModuleActivityById(int module) {
		ModuleActivity list = moduleActivityRepository.findByModuleActivityId(module);
		return list;
	}

	@Override
	public Module getModuleByCode(Module module) {
		Module list = moduleRepository.findByModuleCode(module.getModuleName());
		return list;
	}

	@Override
	public Module getModuleByModuleName(String module) {
		Module list = moduleRepository.findByModuleName(module);
		return list;
	}

	@Override
	public List<Module> getModuleList() {
		List<Module> list = moduleRepository.findAll();
		return list;
	}

	@Override
	public boolean enableModule(Module module) {
		boolean status = false;
		try {
			moduleRepository.save(module);
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public boolean disableModule(Module module) {
		boolean status = false;
		try {
			moduleRepository.save(module);
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

}
