package com.walgreens.batch.central.writer;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.MachineDwntmSplitDataBean;
import com.walgreens.batch.central.utility.MachineDowntimeQuery;

public class EquipmentItemWriter implements ItemWriter<List<MachineDwntmSplitDataBean>> {

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	/**
	 * logger to log the details.
	 */
	private static final Logger lOGGER = LoggerFactory.getLogger(EquipmentItemWriter.class);
	@Override
	public void write(List<? extends List<MachineDwntmSplitDataBean>> items) throws Exception {
		if(lOGGER.isDebugEnabled()){
			lOGGER.debug(" Entering write method of EquipmentItemWriter ");
			}
		try {
		String insertEquipmentHistryTbleQuery = MachineDowntimeQuery.insertEquipmentDowntimeHistryDtlsQuery();
		String deleteEquipmentHistryDtlsQuery = MachineDowntimeQuery.deleteEquipmentDowntimeHistryDtlsQuery();
		String setProcessIndQuery = MachineDowntimeQuery.setProcessIndQueryEquipment();
		for (List<MachineDwntmSplitDataBean> splitBeanList : items) {
			for (MachineDwntmSplitDataBean dataBeanItem : splitBeanList) {
				try {
					if(lOGGER.isDebugEnabled()){
						lOGGER.debug(" Inserting Downtime Id : " +dataBeanItem.getDowntimeId() + " in Database ");
					}
				String checkQuery = MachineDowntimeQuery.getCheckQueryEquipment();
				Object[] param = new Object[]{dataBeanItem.getDowntimeId(),dataBeanItem.getDailyDwntmStart()};
				int count = jdbcTemplate.queryForObject(checkQuery, param, Integer.class);
				if(count != 0) {
					jdbcTemplate.update(deleteEquipmentHistryDtlsQuery, new Object[] {dataBeanItem.getDowntimeId()});					
				} 
				Object[] params = this.getParams(dataBeanItem);
				jdbcTemplate.update( insertEquipmentHistryTbleQuery, params);
				jdbcTemplate.update(setProcessIndQuery, new Object[]{dataBeanItem.getDowntimeId()});
				} catch (Exception e) {
					  if(lOGGER.isErrorEnabled()) {
						lOGGER.error(" Exception occoured while Inserting Downtime Id : " +dataBeanItem.getDowntimeId() + " in Database ", e);
				}
			  }
			}
		}	
		} catch (Exception e) {
			if(lOGGER.isErrorEnabled()){
				lOGGER.error(" Exception occoured at write method of EquipmentItemWriter - ", e);
			}
		}finally{
			if(lOGGER.isInfoEnabled()){
				lOGGER.info(" Exiting write method of EquipmentItemWriter ");
			}
		}
	}	
	/**
	 * 
	 * @param dataBeanItem
	 * @return
	 */
	private Object[] getParams(MachineDwntmSplitDataBean dataBeanItem) {
		if(lOGGER.isDebugEnabled()) {
			 lOGGER.debug(" Entering getParams method of EquipmentItemWriter ");
		  }
		Object[] params = null;
		try {
			params = new Object[] { dataBeanItem.getDowntimeId(), dataBeanItem.getInstanceId(),
					dataBeanItem.getDowntimeReasonId(), dataBeanItem.getDowntimeEventId(),
					dataBeanItem.getStartDttm(), dataBeanItem.getEstimatedEndDttm(),
					dataBeanItem.getActualEndDttm(), dataBeanItem.getBeginEmployeeId(),
					dataBeanItem.getEndEmployeeId(), dataBeanItem.getWorkqueueStatus(),
					dataBeanItem.getDowntimeReason(), dataBeanItem.getDailyDwntmStart(),
					dataBeanItem.getDailyDwntmEnd(), dataBeanItem.getDuration(),
					dataBeanItem.getIdCreated(), dataBeanItem.getDateTimeCreated(),
					dataBeanItem.getIdModified(), dataBeanItem.getDateTimeModified()};
		} catch (Exception e) {
			if(lOGGER.isErrorEnabled()) {
				lOGGER.error(" Exception occoured at getParams method of EquipmentItemWriter - "+ e.getMessage());
			 }
		} finally {
			if(lOGGER.isDebugEnabled()) {
				 lOGGER.debug(" Exiting getParams method of EquipmentItemWriter ");
			  }
		}
		return params;
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
}
