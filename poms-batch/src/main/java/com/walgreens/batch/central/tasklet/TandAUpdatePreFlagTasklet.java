package com.walgreens.batch.central.tasklet;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bo.TimeAttendanceBO;
import com.walgreens.batch.central.factory.OMSBOFactory;
import com.walgreens.common.exception.PhotoOmniException;

public class TandAUpdatePreFlagTasklet implements Tasklet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TandAUpdatePreFlagTasklet.class);

	 @Value("${tanda.destination.folder.path}")
	private String tandAExactFileLocation;

	@Autowired
	private OMSBOFactory factory;

	/**
	 * omniJdbcTemplate
	 */
	@Autowired
	private JdbcTemplate omniJdbcTemplate;

	/**
	 * This method will update the PM_STATUS and PM_STATUS_CD = 'T' in
	 * OM_ORDER_ATTRIBUTE and OM_SHOPPING_CART table and TIME_ATTENDANCE_CD =
	 * 'Y' in OM_ORDER_PM
	 * 
	 * @param contribution
	 *            contains StepContribution values.
	 * @param chunkContext
	 *            contains chunkContext values.
	 */
	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Enter execute method of TandAUpdatePreFlagTasklet ");
		}
		ResourceBundle propFile = null;
		TimeAttendanceBO taBO = factory.getTimeAttendanceBO();
		boolean isPresent = taBO
				.checkDestinationFolderForExactFile(tandAExactFileLocation);

		try {
			if (isPresent) {
				propFile = ResourceBundle.getBundle("PhotoOmniBatchSQL");
				String updateShoppingCart = propFile
						.getString("UPDATE_OM_SHOPPING_CART");
				String updateOrderAttribute = propFile
						.getString("UPDATE_OM_ORDER_ATTRIBUTE");
				String updateOrderPm = propFile.getString("UPDATE_OM_ORDER_PM");
				
				LOGGER.info("Updating Time_Attendance_Status to Y after batch process finds the renamed file from exact");
				omniJdbcTemplate.execute(updateOrderPm);

				LOGGER.info("Updating PM_STATUS to T after batch process finds the renamed file from exact");
				omniJdbcTemplate.execute(updateOrderAttribute);

				LOGGER.info("Updating PM_STATUS_CD to T after batch process finds the renamed file from exact");
				omniJdbcTemplate.execute(updateShoppingCart);

				LOGGER.info("All the values are updated");
			}
		} catch (DataAccessException e) {
			LOGGER.error(
					" Error occoured at execute method of TandAUpdatePreFlagTasklet - ",
					e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(
					" Error occoured at execute method of TandAUpdatePreFlagTasklet - ",
					e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting execute method of TandAUpdatePreFlagTasklet ");
			}
		}
		return RepeatStatus.FINISHED;
	}

	/**
	 * @return the omniJdbcTemplate
	 */
	public JdbcTemplate getOmniJdbcTemplate() {
		return omniJdbcTemplate;
	}

	/**
	 * @param omniJdbcTemplate
	 *            the omniJdbcTemplate to set
	 */
	public void setOmniJdbcTemplate(JdbcTemplate omniJdbcTemplate) {
		this.omniJdbcTemplate = omniJdbcTemplate;
	}
}
