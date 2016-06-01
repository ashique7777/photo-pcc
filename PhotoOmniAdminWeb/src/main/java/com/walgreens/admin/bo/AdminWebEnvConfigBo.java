package com.walgreens.admin.bo;

import java.util.List;

import com.walgreens.common.cache.EvePropBean;

public interface AdminWebEnvConfigBo {

	public List<EvePropBean> getEnvProperties(String envMode);

}
