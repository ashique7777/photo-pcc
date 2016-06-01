package com.walgreens.batch.central.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.walgreens.batch.central.bo.MachineDowntimeBO;
import com.walgreens.batch.central.bo.OrderUtilBO;
import com.walgreens.batch.central.bo.TimeAttendanceBO;




@Component
@Scope("singleton")
public class OMSBOFactory {

	@Autowired
	@Qualifier("OrderUtilBO")
	private OrderUtilBO orderUtilBO;
	
	@Autowired
	@Qualifier("MachineDowntimeBO")
	private MachineDowntimeBO machineBO;
	
	@Autowired
	@Qualifier("TimeAttendanceBO")
	private TimeAttendanceBO timeAttendanceBO;
	
	/**
	 * @return the orderUtilBO
	 */
	public OrderUtilBO getOrderUtilBO() {
		return orderUtilBO;
	}

	/**
	 * @param orderUtilBO the orderUtilBO to set
	 */
	public void setOrderUtilBO(OrderUtilBO orderUtilBO) {
		this.orderUtilBO = orderUtilBO;
	}

	/**
	 * @return the machineBO
	 */
	public MachineDowntimeBO getMachineBO() {
		return machineBO;
	}

	/**
	 * @param machineBO the machineBO to set
	 */
	public void setMachineBO(MachineDowntimeBO machineBO) {
		this.machineBO = machineBO;
	}

	/**
	 * @return the timeAttendanceBO
	 */
	public TimeAttendanceBO getTimeAttendanceBO() {
		return timeAttendanceBO;
	}

	/**
	 * @param timeAttendanceBO the timeAttendanceBO to set
	 */
	public void setTimeAttendanceBO(TimeAttendanceBO timeAttendanceBO) {
		this.timeAttendanceBO = timeAttendanceBO;
	}

	
	
	
}
