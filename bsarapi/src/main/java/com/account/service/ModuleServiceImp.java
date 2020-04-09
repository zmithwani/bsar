package com.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.account.dao.ModuleDAO;
import com.account.model.Module;
import com.account.model.ModuleActivity;
import com.account.model.ModuleSchedule;
import com.account.model.dto.StudentDTO;

@Service
@Transactional
public class ModuleServiceImp implements ModuleService {

	@Autowired
	private ModuleDAO moduledao;

	public boolean saveModule(Module module) {
		return moduledao.saveModule(module);
	}

	public boolean saveModuleActivity(ModuleActivity module) {
		return moduledao.saveModuleActivity(module);
	}

	public boolean saveModuleSchedule(ModuleSchedule module) {
		return moduledao.saveModuleSchedule(module);
	}

	public Module getModuleByName(Module module) {
		return moduledao.getModuleByName(module);
	}

	public ModuleActivity getModuleActivityById(int module) {
		return moduledao.getModuleActivityById(module);
	}

	public Module getModuleByCode(Module module) {
		return moduledao.getModuleByCode(module);
	}

	public Module getModuleByModuleName(String module) {
		return moduledao.getModuleByModuleName(module);
	}

	public List<Module> getModuleList() {
		return moduledao.getModuleList();
	}

	public boolean enableModule(Module module) {
		return moduledao.enableModule(module);
	}

	public boolean disableModule(Module module) {
		return moduledao.disableModule(module);
	}

}
