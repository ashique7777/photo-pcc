package com.walgreens.omni.utility;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.supercsv.io.ICsvBeanWriter;

import com.walgreens.omni.bean.SilverCanisterFailureCSVBean;
 
/**
 * An implementation of the AbstractCsvView.
 * This decides what model data to be used to generate CSV data. 
 *
 */
public class FailureCsvViewImpl extends AbstractCsvView {
 
    @Override
    protected void buildCsvDocument(ICsvBeanWriter csvWriter,
            Map<String, Object> model) throws IOException {
     	
    	@SuppressWarnings("unchecked")
    	List<SilverCanisterFailureCSVBean> silverCanisterFailureCSVBeanList = (List<SilverCanisterFailureCSVBean>) model.get("csvData");
        String[] header = (String[]) model.get("csvHeader");
 
        csvWriter.writeHeader(header);
 
        for (SilverCanisterFailureCSVBean aSilverCanisterFailureCSVBean : silverCanisterFailureCSVBeanList) {
            csvWriter.write(aSilverCanisterFailureCSVBean, header);
        }
    }


}
