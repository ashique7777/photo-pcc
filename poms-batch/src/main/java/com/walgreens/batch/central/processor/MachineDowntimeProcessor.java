package com.walgreens.batch.central.processor;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.walgreens.batch.central.bean.MachineDowntimeDataBean;
import com.walgreens.batch.central.bean.MachineDwntmSplitDataBean;
import com.walgreens.batch.central.bo.MachineDowntimeBO;
import com.walgreens.batch.central.factory.OMSBOFactory;

public class MachineDowntimeProcessor implements ItemProcessor<MachineDowntimeDataBean, List<MachineDwntmSplitDataBean>> {

	/**
	 * LOGGER for Logging
	 */
	private static final Logger lOGGER = LoggerFactory
			.getLogger(MachineDowntimeProcessor.class);
	
	/**
     * oMSBOFactory
     */
	@Autowired
	private OMSBOFactory OMSBOFactory;

	@Override
	public List<MachineDwntmSplitDataBean> process(MachineDowntimeDataBean item) throws Exception {
		if (lOGGER.isDebugEnabled()) {
			lOGGER.debug(" Entering process method of MachineDowntimeProcessor ");
		}
		List<MachineDwntmSplitDataBean> machineHistrySplitList = new ArrayList<MachineDwntmSplitDataBean>();
		try {
			MachineDowntimeBO machineDowntimeBO = OMSBOFactory.getMachineBO();
			machineHistrySplitList = machineDowntimeBO.processDowntimeRecords(item);
		} catch (Exception e) {
			lOGGER.error(" Error occoured at process method of MachineDowntimeProcessor - ", e);
		} finally {
			if (lOGGER.isDebugEnabled()) {
				lOGGER.debug(" Exiting process method of MachineDowntimeProcessor ");
			}
		}
		return machineHistrySplitList;
	}
}
