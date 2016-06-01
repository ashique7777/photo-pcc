package com.walgreens.oms.utility;

import java.util.Comparator;

import com.walgreens.oms.bean.OmPosTranByWicUpcBean;

public class OmPosTranByWicUpcComparator implements Comparator<OmPosTranByWicUpcBean> {

	@Override
	public int compare(OmPosTranByWicUpcBean obj1, OmPosTranByWicUpcBean obj2) {
		
		OmPosTranByWicUpcBean omPosTranByWicUpcBeanOne = (OmPosTranByWicUpcBean) obj1;
		OmPosTranByWicUpcBean omPosTranByWicUpcBeanTwo = (OmPosTranByWicUpcBean) obj2;
		
		int wicComp = String.valueOf(omPosTranByWicUpcBeanOne.getWic())
				      .compareTo(String.valueOf(omPosTranByWicUpcBeanTwo.getWic()));
		/*int upcComp = String.valueOf(omPosTranByWicUpcBeanOne.getUpc())
			      .compareTo(String.valueOf(omPosTranByWicUpcBeanTwo.getUpc()));*/
		
		return ((wicComp == 0) ? String.valueOf(omPosTranByWicUpcBeanOne.getUpc())
			      .compareTo(String.valueOf(omPosTranByWicUpcBeanTwo.getUpc())) : wicComp);
	}

}
