package com.walgreens.batch.central.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;

import com.walgreens.batch.central.bean.KPIFeedBean;

public class KPIFlatFileHeaderFooterWriter implements ItemWriter<KPIFeedBean>,
		FlatFileHeaderCallback, FlatFileFooterCallback, ItemStream {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(KPIFlatFileHeaderFooterWriter.class);

	private FlatFileItemWriter<KPIFeedBean> delegate;

	public void setDelegate(FlatFileItemWriter<KPIFeedBean> delegate) {
		this.delegate = delegate;
	}

	@Override
	public void write(List<? extends KPIFeedBean> items) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Step-12:Start FLAT FILE CREATION.");
		}
	}

	@Override
	public void writeHeader(Writer writer) throws IOException {
		writer.write("# TIME FRAME=DA");
	}

	@Override
	public void writeFooter(Writer writer) throws IOException {
	}

	@Override
	public void open(ExecutionContext executionContext)
			throws ItemStreamException {
		this.delegate.open(executionContext);
	}

	@Override
	public void update(ExecutionContext executionContext)
			throws ItemStreamException {
		this.delegate.update(executionContext);
	}

	@Override
	public void close() throws ItemStreamException {
		this.delegate.close();
	}
}