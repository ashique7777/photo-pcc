package com.walgreens.oms.bo;


import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.oms.bean.PrintSignFilter;
import com.walgreens.oms.bean.PrintSignFilterRespBean;
import com.walgreens.oms.json.bean.EventDataFilter;
import com.walgreens.oms.json.bean.PrintableSignFilter;


/**
 * @author CTS
 * @version 1.1 Mar 18, 2015
 * 
 */
public interface PrintSignReportFilterBO {
    
	public EventDataFilter getEventTypDetails(final PrintSignFilter filter) throws PhotoOmniException;
	
	public  PrintSignFilterRespBean submitPSReportFilterRequest(final PrintableSignFilter reqBean) throws PhotoOmniException;

}
