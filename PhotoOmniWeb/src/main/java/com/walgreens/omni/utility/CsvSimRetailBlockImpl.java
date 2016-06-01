package com.walgreens.omni.utility;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.supercsv.io.ICsvBeanWriter;

import com.walgreens.omni.bean.SimRetailBlockCSVBean;

public class CsvSimRetailBlockImpl extends AbstractCsvView {

	@Override
	protected void buildCsvDocument(ICsvBeanWriter csvWriter,
			Map<String, Object> model) throws IOException {
		@SuppressWarnings("unchecked")
		List<SimRetailBlockCSVBean> simRetailBlockCSVBeanList = ((List<SimRetailBlockCSVBean>) model
				.get("csvData"));
		String[] header = (String[]) model.get("csvHeader");

        if(!model.get("number").toString().equalsIgnoreCase("")){			
		           csvWriter.writeHeader("SimsRetailBlock Report for search criterias RetailBlock "+model.get("retailBlock").toString()+", LocationType "+model.get("locationType").toString()+", Number "+model.get("number").toString());		       
		    }else{
		    	if(model.get("locationType").toString().equalsIgnoreCase("store")){
			        csvWriter.writeHeader("SimsRetailBlock Report for search criterias RetailBlock "+model.get("retailBlock").toString()+", LocationType multiple stores ");}
		    	else if(model.get("locationType").toString().equalsIgnoreCase("Chain")){
		    		csvWriter.writeHeader("SimsRetailBlock Report for search criterias RetailBlock "+model.get("retailBlock").toString()+", LocationType "+model.get("locationType").toString());}
		       }
		csvWriter.writeHeader(header);

		for (SimRetailBlockCSVBean aSimRetailBlockCSVBean : simRetailBlockCSVBeanList) {			
			csvWriter.write(aSimRetailBlockCSVBean, header);
		}
	}

}
