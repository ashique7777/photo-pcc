/**
 * 
 */
package com.walgreens.oms.utility;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.supercsv.io.ICsvBeanWriter;

import com.walgreens.oms.bean.PayOnFulfillmentCSVRespData;


/**
 * @author mannasa
 *
 */
public class PayOnFulFilementCsvView extends AbstractCsvDownload {

	@Override
	protected void buildCsvDocument(ICsvBeanWriter csvWriter,
			Map<String, Object> model) throws IOException {
		
		@SuppressWarnings("unchecked")
		List<PayOnFulfillmentCSVRespData> csvResponseList = ((List<PayOnFulfillmentCSVRespData>) model.get("csvData"));;
		String[] header = (String[]) model.get("csvHeader");
        csvWriter.writeHeader(header);
       
        for (PayOnFulfillmentCSVRespData csvRespBean : csvResponseList) {
            csvWriter.write(csvRespBean, header);
        }
		
	}

}
