
package com.walgreens.omni.utility;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.supercsv.io.ICsvBeanWriter;

import com.walgreens.omni.bean.CannedReportDataCSVBean;
import com.walgreens.omni.bean.CsvSearchBean;

/**
 * @author Cognizant
 *
 */
public class CsvCannedReportImpl extends AbstractCsvView {
	
	
 
    @Override
    protected void buildCsvDocument(ICsvBeanWriter csvWriter,
            Map<String, Object> model) throws IOException {
     	
    	@SuppressWarnings("unchecked")
		List<CannedReportDataCSVBean> cannedReportDetailsBeanList = ((List<CannedReportDataCSVBean>) model.get("csvData"));
        String[] header = (String[]) model.get("csvHeader");
 
        CsvSearchBean searchBean = (CsvSearchBean)model.get("cvsSearch");
        String[] searchCriteria=(String[]) model.get("csvSearchCriteria");
       
        csvWriter.writeHeader(searchCriteria);
        csvWriter.write(searchBean, searchCriteria);
        
        csvWriter.writeHeader(header); 
        for (CannedReportDataCSVBean cannedReportDatacvsBean : cannedReportDetailsBeanList) {
            csvWriter.write(cannedReportDatacvsBean, header);
        }
    } 
	

}
