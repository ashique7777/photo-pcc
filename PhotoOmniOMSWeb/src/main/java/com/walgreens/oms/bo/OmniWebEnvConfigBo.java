package com.walgreens.oms.bo;

import java.util.List;

import com.walgreens.common.cache.EvePropBean;

public interface OmniWebEnvConfigBo {

	public List<EvePropBean> getEnvProperties(String envMode);

}
