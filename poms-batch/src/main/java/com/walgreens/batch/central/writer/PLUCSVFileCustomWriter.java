package com.walgreens.batch.central.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;

import com.walgreens.batch.central.bean.CSVFilePLUReportDataBean;
import com.walgreens.batch.central.bean.PLUReportPrefDataBean;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

public class PLUCSVFileCustomWriter implements
		ItemWriter<CSVFilePLUReportDataBean> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PLUCSVFileCustomWriter.class);

	private PLUReportPrefDataBean pluReportPrefDataBean;
	BufferedWriter objBufferedWriter = null;

	/**
	 * This method populate the data for the xlsx report.
	 * 
	 * @return PLU report.
	 * @throws PhotoOmniException
	 *             - Custom Exception.
	 */
	@Override
	public void write(List<? extends CSVFilePLUReportDataBean> items)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into write method of PLUCSVFileCustomWriter ");
		}
		String fileLocation = pluReportPrefDataBean.getFileLocation();
		String fileName = pluReportPrefDataBean.getFileNameList().get(0);
		try {
			objBufferedWriter = new BufferedWriter(new FileWriter(
					fileLocation.concat(fileName), true));
			for (CSVFilePLUReportDataBean csvFilePLUReportDataBean : items) {

				objBufferedWriter
						.append(" "
								+ (csvFilePLUReportDataBean.getOrderDate() == null ? " "
										: csvFilePLUReportDataBean
												.getOrderDate()));
				objBufferedWriter
						.append(PhotoOmniBatchConstants.COMMA_DELIMITER);
				objBufferedWriter.append(csvFilePLUReportDataBean
						.getPluNumber() == null ? " "
						: csvFilePLUReportDataBean.getPluNumber());
				objBufferedWriter
						.append(PhotoOmniBatchConstants.COMMA_DELIMITER);
				objBufferedWriter.append(csvFilePLUReportDataBean
						.getCouponCode() == null ? " "
						: csvFilePLUReportDataBean.getCouponCode());
				objBufferedWriter
						.append(PhotoOmniBatchConstants.COMMA_DELIMITER);
				objBufferedWriter
						.append(addEscapeSequenceInCsvField(csvFilePLUReportDataBean
								.getPromotionDescription() == null ? " "
								: csvFilePLUReportDataBean
										.getPromotionDescription()));
				objBufferedWriter
						.append(PhotoOmniBatchConstants.COMMA_DELIMITER);
				objBufferedWriter
						.append(csvFilePLUReportDataBean.getChannel() == null ? " "
								: csvFilePLUReportDataBean.getChannel());
				objBufferedWriter
						.append(PhotoOmniBatchConstants.COMMA_DELIMITER);
				objBufferedWriter.append(String
						.valueOf(csvFilePLUReportDataBean.getRetailPrice()));
				objBufferedWriter
						.append(PhotoOmniBatchConstants.COMMA_DELIMITER);
				objBufferedWriter.append(String
						.valueOf(csvFilePLUReportDataBean.getDiscountPrice()));
				objBufferedWriter
						.append(PhotoOmniBatchConstants.COMMA_DELIMITER);
				objBufferedWriter.append(String
						.valueOf(csvFilePLUReportDataBean.getNoOfOrders()));
				objBufferedWriter
						.append(PhotoOmniBatchConstants.COMMA_DELIMITER);
				objBufferedWriter.append(String
						.valueOf(csvFilePLUReportDataBean.getNoOfUnits()));
				objBufferedWriter
						.append(PhotoOmniBatchConstants.NEW_LINE_SEPARATOR);
			}

		} catch (Exception e) {
			LOGGER.error(" Error occoured at write method of PLUCSVFileCustomWriter ---- > "
					+ e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			try {
				objBufferedWriter.flush();
			} catch (IOException e) {
				LOGGER.error(" Error occoured at write method of PLUCSVFileCustomWriter ---- > "
						+ e);
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting write method of PLUCSVFileCustomWriter ");
			}
		}
	}

	private String addEscapeSequenceInCsvField(String ValueToEscape) {
		return "\"" + ValueToEscape + "\"";
	}

	/**
	 * Method to get stepExecution form Execution context
	 * 
	 * @param stepExecution
	 */
	@BeforeStep
	private void retriveValue(StepExecution stepExecution) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered into retriveValue method of PLUCSVFileCustomWriter");
		}
		JobExecution jobExecution = stepExecution.getJobExecution();
		ExecutionContext executionContext = jobExecution.getExecutionContext();
		pluReportPrefDataBean = (PLUReportPrefDataBean) executionContext
				.get("refDataKey");
	}

	@AfterStep
	private void closeFile() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into closeFile method of PLUCSVFileCustomWriter");
		}
		try {
			if (!CommonUtil.isNull(objBufferedWriter)) {
				objBufferedWriter.close();
			}
		} catch (IOException e) {
			LOGGER.error("Error occoured at closeFile method of PLUCSVFileCustomWriter ---- > "
					+ e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting from  closeFile method of PLUCSVFileCustomWriter");
			}
		}
	}
}