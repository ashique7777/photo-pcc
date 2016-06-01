package com.walgreens.batch.central.bo;

import java.text.ParseException;
import java.util.List;

import com.walgreens.batch.central.bean.MachineDowntimeDataBean;
import com.walgreens.batch.central.bean.MachineDwntmSplitDataBean;

public interface MachineDowntimeBO {
	
	/**
	 * This method process the downtime records and split them according to
	 *  downtime start date and  downtime end date
	 * 
	 * @param machineDataBean
	 * @return machineHistrySplitList
	 * @throws ParseException
	 */
	public List<MachineDwntmSplitDataBean> processDowntimeRecords(MachineDowntimeDataBean machineDataBean) throws ParseException;

}
