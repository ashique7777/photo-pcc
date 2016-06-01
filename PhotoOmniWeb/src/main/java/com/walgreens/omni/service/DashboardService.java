package com.walgreens.omni.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;

public interface DashboardService {

	public Map<String, Object> getDashboardItems();

	public Map<String, Object> loadReportTemplates(String key,
			Boolean defaultFlag, HttpServletRequest request);

	public Map<String, Object> loadFilterData(String key, Boolean defaultFlag,
			HttpServletRequest request);

	public Map<String, Object> loadStoreData(String key, Boolean defaultFlag,
			HttpServletRequest request, Map<String, Object> params);

	public Map<String, Object> print(HttpServletRequest request);

	public Map<String, Object> printData(HttpServletRequest request,
			String key, Boolean defaultFlag);

	public void exportToExcel(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String key,
			@PathVariable Boolean defaultFlag);

}
