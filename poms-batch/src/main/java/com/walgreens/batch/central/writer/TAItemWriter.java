package com.walgreens.batch.central.writer;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;

import com.walgreens.batch.central.bean.TADataBean;
import com.walgreens.common.constant.PhotoOmniConstants;

/**
 * This class implements the functionality of item writer for flat file creation
 * step in Time and Attendance batch
 * 
 * @author Cognizant
 * 
 */
public class TAItemWriter implements ItemWriter<TADataBean>,
		FlatFileFooterCallback, ItemStream,PhotoOmniConstants {

	/**
	 * Reference to Application Logger object
	 */
	private static final Logger log = LoggerFactory
			.getLogger(TAItemWriter.class);

	private FlatFileItemWriter<TADataBean> delegate;
	BigDecimal totalAmount = BigDecimal.ZERO;

	/*
	 * This method will write the footer into the csv (flat file) based on the total number of
	 * rows fetched from the query  
	 * @see
	 * org.springframework.batch.item.file.FlatFileFooterCallback#writeFooter
	 * (java.io.Writer)
	 */
	public void writeFooter(Writer writer) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("Entering into TAItemWriter:writeFooter()");
		}
		writer.write("FOOTER,"
				+ new SimpleDateFormat(CALENDER_DATE_PATTERN).format(new Date()) + ","
				+ totalAmount);
		
		if (log.isDebugEnabled()) {
			log.debug("Exiting from TAItemWriter:writeFooter()");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	public void write(List<? extends TADataBean> items) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Entering into TAItemWriter:write()");
		}
		TADataBean dataBean = items.get(0);
		totalAmount = new BigDecimal(dataBean.getTotalRecors());

		delegate.write(items);

		log.info("In Time and Attendance Batch, Total Amount : "
				+ totalAmount);
		if (log.isDebugEnabled()) {
			log.debug("Exiting from TAItemWriter:write()");
		}

	}

	/**
	 * This method sets the value of delegate attribute
	 * 
	 * @param delegate
	 */
	public void setDelegate(FlatFileItemWriter<TADataBean> delegate) {
		this.delegate = delegate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.batch.item.ItemStream#close()
	 */
	public void close() throws ItemStreamException {
		this.delegate.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.batch.item.ItemStream#open(org.springframework.batch
	 * .item.ExecutionContext)
	 */
	public void open(ExecutionContext arg0) throws ItemStreamException {
		this.delegate.open(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.batch.item.ItemStream#update(org.springframework.
	 * batch.item.ExecutionContext)
	 */
	public void update(ExecutionContext arg0) throws ItemStreamException {
		this.delegate.update(arg0);
	}
}
