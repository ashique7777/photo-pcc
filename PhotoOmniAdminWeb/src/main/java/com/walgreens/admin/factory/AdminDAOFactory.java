package com.walgreens.admin.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.walgreens.admin.dao.AdminWebEnvConfigDAO;
import com.walgreens.admin.dao.EmployeeDAO;
import com.walgreens.admin.dao.KioskReportsDAO;
import com.walgreens.admin.dao.MachineDAO;
import com.walgreens.admin.dao.MachineReportDAO;

@Component
@Scope("singleton")
public class AdminDAOFactory {
	
	@Autowired
	@Qualifier("AdminWebEnvConfigDAO")
	private AdminWebEnvConfigDAO adminWebEnvConfigDAO;
	
	@Autowired
	@Qualifier("EmployeeDAO")
	private EmployeeDAO employeeDAO;
	
	@Autowired
	@Qualifier("MachineDowntimeDAO")
	private MachineDAO machineDAO;

	
	@Autowired
	@Qualifier("MachineReportDAO")
	private MachineReportDAO machineReportDAO;

	@Autowired
	private KioskReportsDAO kioskReportsDAO;

	
	public KioskReportsDAO getKioskReportsDAO() {
		return kioskReportsDAO;
	}
	public void setKioskReportsDAO(KioskReportsDAO kioskReportsDAO) {
		this.kioskReportsDAO = kioskReportsDAO;
	}
	public EmployeeDAO getEmployeeDAO() {
		return employeeDAO;
	}
	public void setEmployeeDAO(EmployeeDAO employeeDAO) {
		this.employeeDAO = employeeDAO;
	}
	/**
	 * This method finds the DAO class
	 * @return DAO class
	 */
	public MachineDAO getMachineDwntmDao() {
		return machineDAO;
	}
	/**
	 * @return the machineDAO
	 */
	public MachineDAO getMachineDAO() {
		return machineDAO;
	}
	/**
	 * @param machineDAO the machineDAO to set
	 */
	public void setMachineDAO(MachineDAO machineDAO) {
		this.machineDAO = machineDAO;
	}
	/**
	 * @return the machineReportDAO
	 */
	public MachineReportDAO getMachineReportDAO() {
		return machineReportDAO;
	}
	/**
	 * @param machineReportDAO the machineReportDAO to set
	 */
	public void setMachineReportDAO(MachineReportDAO machineReportDAO) {
		this.machineReportDAO = machineReportDAO;
	}
	public AdminWebEnvConfigDAO getAdminWebEnvConfigDAO() {
		return adminWebEnvConfigDAO;
	}
	public void setAdminWebEnvConfigDAO(AdminWebEnvConfigDAO adminWebEnvConfigDAO) {
		this.adminWebEnvConfigDAO = adminWebEnvConfigDAO;
	}
}
