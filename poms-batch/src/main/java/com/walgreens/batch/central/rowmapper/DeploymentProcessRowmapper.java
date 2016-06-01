/**
 * 
 */
package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.DeploymentProcessDataBean;

/**
 * @author CTS
 *
 */
public class DeploymentProcessRowmapper implements RowMapper<DeploymentProcessDataBean>{

	public DeploymentProcessDataBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		DeploymentProcessDataBean deploymentProcessDataBean = new DeploymentProcessDataBean();
		deploymentProcessDataBean.setPropertyValue(rs.getString("PROPERTY_VALUE"));
		return deploymentProcessDataBean;
	}


}
