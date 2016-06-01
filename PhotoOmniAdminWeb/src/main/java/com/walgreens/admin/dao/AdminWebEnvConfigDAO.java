package com.walgreens.admin.dao;

import java.util.List;

import com.walgreens.common.cache.EvePropBean;

public interface AdminWebEnvConfigDAO {
	
	public List<EvePropBean> getEnvProperties(String envMode);

}
