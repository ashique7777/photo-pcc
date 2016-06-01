package com.walgreens.batch.central.reader;

import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class PmByWICMasterItemReader implements ItemReader<List<String>>{

	public List<String> read() throws Exception, UnexpectedInputException,
			ParseException, NonTransientResourceException {
		
		
		return null;
	}

}
