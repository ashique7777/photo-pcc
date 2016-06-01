package com.walgreens.batch.central.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.walgreens.batch.central.dao.OrdersUtilDAO;

@Component
@Scope("singleton")
public class BatchOmsDAOFactory {

	@Autowired
	private OrdersUtilDAO ordersUtilDAO;

	public OrdersUtilDAO getOrdersUtilDAO() {
		return ordersUtilDAO;
	}

	public void setOrdersUtilDAO(OrdersUtilDAO ordersUtilDAO) {
		this.ordersUtilDAO = ordersUtilDAO;
	}

}
