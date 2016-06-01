/**
 * 
 */
package com.walgreens.batch.central.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.LicenceContentBean;
import com.walgreens.batch.central.bean.LicenceContentDetails;
import com.walgreens.batch.central.rowmapper.LicenceContentRowmapper;

/**
 * @author CTS
 *
 */
public class LicenceContentDAO {

	private static String querySql = "SELECT PF_ORDER_DTTM AS \"Order Date\",PF_QUANTITY AS \"Quantity\",PF_PROVIDER AS \"Provider\",PF_LOCATION_TYPE AS \"Location Type\", "
			+ "PF_LOCATION_NBR AS \"Location Number\", PF_ORIGINAL_RETAIL AS \"Retail\",PF_TOTAL_DISCOUNT_AMT AS \"Discount Applied\",PF_EMPLOYEE_DISCOUNT AS \"Employee Discount\", "
			+ "PF_NET_SALE AS \"Net Sale\", PF_PRODUCT_ID AS \"Product ID\",PF_UPC AS \"UPC\",PF_WIC AS \"WIC\", PF_LONG_DESC AS \"Product Description\", "
			+ "PF_LICENSED_CONTENT_ID AS \"Template ID\", PF_ENVELOPE_NBR AS \"Envelope Number\" FROM PF_LICENSED_CONTENT_REPORT";
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	/**
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public LicenceContentDetails getRecords() {
		LicenceContentDetails licenceContentDetails = new LicenceContentDetails();
		List<LicenceContentBean> licenceContentList = null;
		jdbcTemplate = new JdbcTemplate(dataSource);
		
		licenceContentList = jdbcTemplate.query(querySql,
				new LicenceContentRowmapper());

		licenceContentDetails.setLicenceContentList(licenceContentList);

		return licenceContentDetails;
		

	}

}
