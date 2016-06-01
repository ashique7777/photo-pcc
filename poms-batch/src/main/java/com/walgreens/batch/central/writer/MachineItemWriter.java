package com.walgreens.batch.central.writer;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.MachineDwntmSplitDataBean;
import com.walgreens.batch.central.utility.MachineDowntimeQuery;

public class MachineItemWriter implements ItemWriter<List<MachineDwntmSplitDataBean>> {

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	/**
	 * logger to log the details.
	 */
	private static final Logger lOGGER = LoggerFactory.getLogger(MachineItemWriter.class);
	@Override
	public void write(List<? extends List<MachineDwntmSplitDataBean>> items) throws Exception {
		if(lOGGER.isDebugEnabled()){
			lOGGER.debug(" Entering write method of MachineItemWriter ");
		}
	try {
		String insertMachineHistryTbleQuery = MachineDowntimeQuery.insertMachineDowntimeHistryDtlsQuery();
		String deletemachineHistryDtlsQuery = MachineDowntimeQuery.deleteMachineDowntimeHistryDtlsQuery();
		String setProcessIndQuery = MachineDowntimeQuery.setProcessIndQueryMachine();
		for (List<MachineDwntmSplitDataBean> splitBeanList : items) {
			for (MachineDwntmSplitDataBean dataBeanItem : splitBeanList) {
			try {
				if(lOGGER.isDebugEnabled()){
					lOGGER.debug(" Inserting Downtime Id : " +dataBeanItem.getDowntimeId() + " in Database ");
				}
				String checkQuery = MachineDowntimeQuery.getCheckQueryMachine();
				Object[] param = new Object[]{dataBeanItem.getDowntimeId(),dataBeanItem.getDailyDwntmStart()};
				int count = jdbcTemplate.queryForObject(checkQuery, param, Integer.class);
				if(count != 0) {
					jdbcTemplate.update(deletemachineHistryDtlsQuery, new Object[] {dataBeanItem.getDowntimeId()});					
				} 
				Object[] params = this.getParamsForInsertQuery(dataBeanItem);
				jdbcTemplate.update(insertMachineHistryTbleQuery, params);
				jdbcTemplate.update(setProcessIndQuery, new Object[]{dataBeanItem.getDowntimeId()});
			  } catch (Exception e) {
				  if(lOGGER.isErrorEnabled()) {
						lOGGER.error(" Exception occoured while Inserting Downtime Id : " +dataBeanItem.getDowntimeId() + " in Database ", e);
				}
			  }
			}
		}
	} catch (Exception e) {
		if(lOGGER.isErrorEnabled()) {
			lOGGER.error(" Exception occoured at write method of MachineItemWriter - ", e);
		 }
	 } finally{
		if(lOGGER.isDebugEnabled()) {
			 lOGGER.debug(" Exiting write method of MachineItemWriter ");
		  }
	 }	
	}
	
	/**
	 * 
	 * @param dataBeanItem
	 * @return
	 */
	private Object[] getParamsForInsertQuery(MachineDwntmSplitDataBean dataBeanItem) {
		if(lOGGER.isDebugEnabled()) {
			 lOGGER.debug(" Entering getParamsForInsertQuery method of MachineItemWriter ");
		  }
		Object[] params = null;
		try {
			params = new Object[] { dataBeanItem.getDowntimeId(),
					dataBeanItem.getInstanceId(), dataBeanItem.getDowntimeReasonId(),
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
				lOGGER.error(" Exception occoured at getParamsForInsertQuery method of MachineItemWriter - "+ e.getMessage());
			 }
		} finally {
			if(lOGGER.isDebugEnabled()) {
				 lOGGER.debug(" Exiting getParamsForInsertQuery method of MachineItemWriter ");
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
