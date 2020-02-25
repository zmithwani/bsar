package com.account.dao;

import com.account.model.Module;
import com.account.model.ModuleActivity;
import com.account.model.ModuleSchedule;

public interface ModuleDAO {

	public boolean saveModule(Module module);

	public boolean saveModuleActivity(ModuleActivity module);

	public boolean saveModuleSchedule(ModuleSchedule module);

	public Module getModuleByName(Module module);

	public ModuleActivity getModuleActivityById(int module);

	public Module getModuleByCode(Module module);

	public Module getModuleByModuleName(String module);

}
