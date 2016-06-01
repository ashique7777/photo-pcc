/**
 * 
 */
package com.walgreens.batch.central.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.POFOrderVCRepBean;
import com.walgreens.batch.central.utility.PayOnFulfillmentQry;

/**
 * @author CTS
 * 
 */
public class POFUserApproveCustomWriter implements
		ItemWriter<POFOrderVCRepBean> {

	/**
	 * logger to log the details.
	 */
	private static final Logger lOGGER = LoggerFactory
			.getLogger(POFUserApproveCustomWriter.class);

	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Override
	public void write(List<? extends POFOrderVCRepBean> items) throws Exception {
		if (lOGGER.isInfoEnabled()) {
			lOGGER.debug(" .....Entering writer method of POFUserApproveCustomWriter Step Four");
		}
		try {
			String updateOrderVCRepQry = PayOnFulfillmentQry
					.updateOrderVCRepForUserPayment().toString();
			String deleteOrderVCAuditQry = PayOnFulfillmentQry
					.deleteOrderVCAudit().toString();
			int auditRow = jdbcTemplate.update(deleteOrderVCAuditQry);
			if (auditRow > 0)
				lOGGER.debug(" Success write method of POFUserApproveCustomWriter  Step Four delete record from Audit Table............ "
						+ auditRow);
			else
				lOGGER.debug(" No Record deleted from audit table...");

			lOGGER.debug(" Update Order VC Rep Table");
			int updateRow = jdbcTemplate.update(updateOrderVCRepQry);
			if (updateRow > 0)
				lOGGER.debug(" Success write method of POFUserApproveCustomWriter  Step Four number of records updated in Order VC Rep Table............ "
						+ updateRow);
			else
				lOGGER.debug(" No Record updated in Order VC Rep Table");

		} catch (Exception e) {
			lOGGER.error("POFUserApproveCustomWriter  error at Step Four.............."
					+ e);

		} finally {
			if (lOGGER.isInfoEnabled()) {
				lOGGER.info(" Exiting write method of POFUserApproveCustomWriter ");
			}
		}
	}

}
