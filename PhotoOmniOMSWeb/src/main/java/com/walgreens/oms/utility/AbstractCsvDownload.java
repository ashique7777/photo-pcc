package com.walgreens.oms.utility;

import java.io.IOException;
import java.util.Map;
 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 

import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
 
/**
 * This class is an abstract CSV view which concrete views must implement. 
 * @author www.codejava.net
 *
 */
public abstract class AbstractCsvDownload extends AbstractView {
	
	/** The content type for an Excel response */
	private static final String CONTENT_TYPE = "text/csv";

	@SuppressWarnings("unused")
	private String url;

	/**
	 * Default Constructor.
	 * Sets the content type of the view to "application/vnd.ms-excel".
	 */
	public AbstractCsvDownload() {
		setContentType(CONTENT_TYPE);
	}

	/**
	 * Set the URL of the Excel workbook source, without localization part nor extension.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	protected boolean generatesDownloadContent() {
		return true;
	}

	/**
	 * Renders the Excel view, given the specified model.
	 */
	
    private String fileName;
 
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
 
    protected void prepareResponse(HttpServletRequest request,
            HttpServletResponse response) {
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",fileName);
        response.setHeader(headerKey, headerValue);
    }
 
    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
 
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
 
        buildCsvDocument(csvWriter, model);
        response.setContentType(getContentType());
        csvWriter.close();
    }
 
    /**
     * The concrete view must implement this method. 
     */
    protected abstract void buildCsvDocument(ICsvBeanWriter csvWriter,
            Map<String, Object> model) throws IOException;
}

