/**
 * 
 */
package com.walgreens.batch.central.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.walgreens.batch.central.bean.LicenceContentBean;

/**
 * @author CTS
 *
 */
public class LicenceContentWriter implements ItemWriter<LicenceContentBean>,
		FlatFileHeaderCallback {
		@Autowired
		FlatFileItemWriter delegate;

	public void setDelegate(FlatFileItemWriter delegate) {
			this.delegate = delegate;
		}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.file.FlatFileHeaderCallback#writeHeader(java.io.Writer)
	 */
	@Override
	public void writeHeader(Writer writer) throws IOException {
		writer.write("output_file.txt" + "," + new Date());
	}

	@Override
	public void write(List<? extends LicenceContentBean> items)
			throws Exception {
		
	}

}
