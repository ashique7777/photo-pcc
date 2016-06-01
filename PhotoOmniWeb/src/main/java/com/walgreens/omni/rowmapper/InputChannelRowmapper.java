/**
 *Class for Rowmapper, to get the code id and decode 
 */
package com.walgreens.omni.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.omni.json.bean.InputChannel;

/**
 * @author Cognizant
 *
 */
/**
 * 
 *Rowmapper to get the code id and decode 
 *
 */
public class InputChannelRowmapper implements RowMapper<InputChannel>  {

	@Override
	public InputChannel mapRow(ResultSet rs, int rowNum) throws SQLException {
		InputChannel inputChannelBean = new InputChannel();
		inputChannelBean.setInputChannelId(rs.getString("CODE_ID"));
		inputChannelBean.setInputChannelName(rs.getString("DECODE"));
		return inputChannelBean;
		
	}

}
