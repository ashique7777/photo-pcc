/**
 * 
 */
package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.EnvPropertiesBean;

/**
 * This rowmapper is the implementation of EnvPropertiesQuery.getEnvProperties()
 * @author Cognizant
 *
 */
public class EnvPropertiesRowMapper implements RowMapper<EnvPropertiesBean>{
	
	@Override
	public EnvPropertiesBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		EnvPropertiesBean envPropBean = new EnvPropertiesBean();
		envPropBean.setEnvPropId(rs.getInt("SYS_ENV_PROPERTIES_ID"));
		envPropBean.setPropName(rs.getString("PROPERTY_NAME"));
		envPropBean.setPropType(rs.getString("PROPERTY_TYPE"));
		envPropBean.setPropValue(rs.getString("PROPERTY_VALUE")); 
		return envPropBean;
	}

}
