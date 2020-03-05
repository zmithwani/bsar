package com.account.service;

import com.account.model.Module;
import com.account.model.ModuleActivity;
import com.account.model.ModuleSchedule;
import com.account.model.dto.StudentDTO;

public interface ModuleService {

	public boolean saveModule(Module module);

	public boolean saveModuleActivity(ModuleActivity moduleact);

	public boolean saveModuleSchedule(ModuleSchedule module);

	public Module getModuleByName(Module module);

	public ModuleActivity getModuleActivityById(int module);

	public Module getModuleByCode(Module module);

	public Module getModuleByModuleName(String module);

}
