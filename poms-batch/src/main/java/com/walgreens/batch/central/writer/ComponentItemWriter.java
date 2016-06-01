package com.walgreens.batch.central.writer;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.MachineDwntmSplitDataBean;
import com.walgreens.batch.central.utility.MachineDowntimeQuery;
import com.walgreens.common.exception.PhotoOmniException;

public class ComponentItemWriter implements ItemWriter<List<MachineDwntmSplitDataBean>> {

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	/**
	 * logger to log the details.
	 */
	private static final Logger lOGGER = LoggerFactory.getLogger(ComponentItemWriter.class);
	
	
	@Override
	public void write(List<? extends List<MachineDwntmSplitDataBean>> items) throws PhotoOmniException {
		if(lOGGER.isDebugEnabled()){
			lOGGER.debug(" Entering write method of ComponentItemWriter ");
			}
		try {
		String insertComponentHistryTbleQuery = MachineDowntimeQuery.insertComponentDowntimeHistryDtlsQuery();
		String deleteComponentHistryDtlsQuery = MachineDowntimeQuery.deleteComponentDowntimeHistryDtlsQuery();
		String setProcessIndQuery = MachineDowntimeQuery.setProcessIndQueryComponent();
		for (List<MachineDwntmSplitDataBean> splitBeanList : items) {
		for (MachineDwntmSplitDataBean dataBeanItem : splitBeanList) {
			try {
				if(lOGGER.isDebugEnabled()){
					lOGGER.debug(" Inserting Downtime Id : " +dataBeanItem.getDowntimeId() + " in Database ");
				}
			String checkQuery = MachineDowntimeQuery.getCheckQueryComponent();
			Object[] param = new Object[]{dataBeanItem.getDowntimeId(),dataBeanItem.getDailyDwntmStart()};
			int count = jdbcTemplate.queryForObject(checkQuery, param, Integer.class);
			if(count != 0) {
				jdbcTemplate.update(deleteComponentHistryDtlsQuery, new Object[] {dataBeanItem.getDowntimeId()});					
			} 
			Object[] params = this.getParams(dataBeanItem);
			jdbcTemplate.update( insertComponentHistryTbleQuery, params);
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
				lOGGER.error("  Exception occured in write method of ComponentItemWriter ", e);
				}
	} finally{
		if(lOGGER.isDebugEnabled()){
			lOGGER.debug(" Exiting write method of ComponentItemWriter ");
			}
		}
	}
	
	
	/**
	 * 
	 * @param dataBeanItem
	 * @return
	 * @throws PhotoOmniException 
	 */
	private Object[] getParams(MachineDwntmSplitDataBean dataBeanItem) {
		if(lOGGER.isDebugEnabled()) {
			 lOGGER.debug(" Entering getParams method of ComponentItemWriter ");
		  }
		Object[] params = null;
		try {
			params = new Object[] {dataBeanItem.getDowntimeId(), dataBeanItem.getEquipmentInstanceId(),
					dataBeanItem.getComponentId(), dataBeanItem.getDowntimeReasonId(),
					dataBeanItem.getDowntimeEventId(), dataBeanItem.getStartDttm(),
					dataBeanItem.getEstimatedEndDttm(), dataBeanItem.getActualEndDttm(),
					dataBeanItem.getBeginEmployeeId(), dataBeanItem.getEndEmployeeId(),
					dataBeanItem.getWorkqueueStatus(), dataBeanItem.getDowntimeReason(),
					dataBeanItem.getDailyDwntmStart(), dataBeanItem.getDailyDwntmEnd(),
					dataBeanItem.getDuration(), dataBeanItem.getIdCreated(),
					dataBeanItem.getDateTimeCreated(), dataBeanItem.getIdModified(),
					dataBeanItem.getDateTimeModified()};
		} catch (Exception e) {
			if(lOGGER.isErrorEnabled()) {
				lOGGER.error(" Exception occoured at getParams method of ComponentItemWriter - " , e);
			 }
		} finally {
			if(lOGGER.isDebugEnabled()) {
				 lOGGER.debug(" Exiting getParams method of ComponentItemWriter ");
			  }
		}
		return params;
	}
	
	
	/**
	 * @return the jdbcTemplate
	 */
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}


	/**
	 * @param jdbcTemplate the jdbcTemplate to set
	 */
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
