package com.walgreens.batch.central.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.walgreens.batch.central.dao.KPIDAO;

@Component
@Scope("singleton")
public class KPIDAOFactory {

	@Autowired
	private KPIDAO kpidao;

	public KPIDAO getKpidao() {
		return kpidao;
	}

	public void setKpidao(KPIDAO kpidao) {
		this.kpidao = kpidao;
	}

}
