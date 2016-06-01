package com.walgreens.batch.central.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.walgreens.batch.central.bo.KPIBO;

@Component
@Scope("singleton")
public class KPIBOFactory {

	@Autowired
	private KPIBO kpibo;

	public KPIBO getKpibo() {
		return kpibo;
	}

	public void setKpibo(KPIBO kpibo) {
		this.kpibo = kpibo;
	}

}
