/**
 * 
 */
package com.walgreens.batch.central.bean;


/**
 * @author CTS
 *
 */
public class LCCSVFileReportDataBean {
	
	private AdhocAndDailyCSVFileReportDataBean adhocReportResBean = null;
	private ExceptionCSVFileReportDataBean exceptionReportResBean = null;
	/**
	 * @return the adhocReportResBean
	 */
	public AdhocAndDailyCSVFileReportDataBean getAdhocReportResBean() {
		return adhocReportResBean;
	}
	/**
	 * @param adhocReportResBean the adhocReportResBean to set
	 */
	public void setAdhocReportResBean(
			AdhocAndDailyCSVFileReportDataBean adhocReportResBean) {
		this.adhocReportResBean = adhocReportResBean;
	}
	/**
	 * @return the exceptionReportResBean
	 */
	public ExceptionCSVFileReportDataBean getExceptionReportResBean() {
		return exceptionReportResBean;
	}
	/**
	 * @param exceptionReportResBean the exceptionReportResBean to set
	 */
	public void setExceptionReportResBean(
			ExceptionCSVFileReportDataBean exceptionReportResBean) {
		this.exceptionReportResBean = exceptionReportResBean;
	}
	
	
	
}
