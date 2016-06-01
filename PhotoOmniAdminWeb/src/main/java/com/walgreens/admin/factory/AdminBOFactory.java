package com.walgreens.admin.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.walgreens.admin.bo.AdminWebEnvConfigBo;
import com.walgreens.admin.bo.EmployeeBO;
import com.walgreens.admin.bo.KioskReportsBO;
import com.walgreens.admin.bo.MachineBO;
import com.walgreens.admin.bo.MachineReportBO;

@Component
@Scope("singleton")
public class AdminBOFactory {
	
	@Autowired
	@Qualifier("AdminWebEnvConfigBo")
	private AdminWebEnvConfigBo adminWebEnvConfigBo;
	
	@Autowired
	@Qualifier("EmployeeBO")
	private EmployeeBO employeeBO;
	
	@Autowired
	@Qualifier("MachineReportBO")
	private MachineReportBO machineReportBO;
	
	@Autowired
	@Qualifier("MachineDowntimeBO")
	private MachineBO machineBO;
	
	@Autowired
	private KioskReportsBO kioskReportsBO;
	
	public KioskReportsBO getKioskReportsBO() {
		return kioskReportsBO;
	}
	public void setKioskReportsBO(KioskReportsBO kioskReportsBO) {
		this.kioskReportsBO = kioskReportsBO;
	}
	public EmployeeBO getEmployeeBO() {
		return employeeBO;
	}
	public void setEmployeeBO(EmployeeBO employeeBO) {
		this.employeeBO = employeeBO;
	}
	/**
	 * @return the machineReportBO
	 */
	public MachineReportBO getMachineReportBO() {
		return machineReportBO;
	}
	/**
	 * @param machineReportBO the machineReportBO to set
	 */
	public void setMachineReportBO(MachineReportBO machineReportBO) {
		this.machineReportBO = machineReportBO;
	}
	/**
	 * @return the machineBO
	 */
	public MachineBO getMachineBO() {
		return machineBO;
	}
	/**
	 * @param machineBO the machineBO to set
	 */
	public void setMachineBO(MachineBO machineBO) {
		this.machineBO = machineBO;
	}
	public AdminWebEnvConfigBo getAdminWebEnvConfigBo() {
		return adminWebEnvConfigBo;
	}
	public void setAdminWebEnvConfigBo(AdminWebEnvConfigBo adminWebEnvConfigBo) {
		this.adminWebEnvConfigBo = adminWebEnvConfigBo;
	}
}