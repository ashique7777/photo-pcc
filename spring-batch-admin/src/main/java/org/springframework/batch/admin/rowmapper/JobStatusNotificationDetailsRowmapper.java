package org.springframework.batch.admin.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.batch.admin.bean.JobStatusNotificationDetailsBean;
import org.springframework.jdbc.core.RowMapper;

public class JobStatusNotificationDetailsRowmapper implements RowMapper<JobStatusNotificationDetailsBean> {

	@Override
	public JobStatusNotificationDetailsBean mapRow(ResultSet rs, int arg1) throws SQLException {
		JobStatusNotificationDetailsBean objJobStatusNotificationDetails = new JobStatusNotificationDetailsBean();
		objJobStatusNotificationDetails.setJobInstanceId(rs.getString("JOB_INSTANCE_ID"));
		objJobStatusNotificationDetails.setJobName(rs.getString("JOB_NAME"));
		objJobStatusNotificationDetails.setJobExecutionId(rs.getString("JOB_EXECUTION_ID"));
		objJobStatusNotificationDetails.setStartTime(rs.getString("START_TIME"));
		objJobStatusNotificationDetails.setEndTime(rs.getString("END_TIME"));
		return objJobStatusNotificationDetails;
	}

}
