package com.walgreens.omni.dao;

import java.util.List;

import com.walgreens.common.cache.EvePropBean;

public interface OmniWebEnvConfigDAO {
	
	public List<EvePropBean> getEnvProperties(String envMode);

}
