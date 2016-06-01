/********************************************************************************/
/* Copyright (c) 2015, Walgreens Co.											*/
/* All Rights Reserved.															*/
/*																				*/
/* THIS SOFTWARE AND DOCUMENTATION IS PROVIDED "AS IS," AND COPYRIGHT			*/
/* HOLDERS MAKE NO REPRESENTATIONS OR WARRANTIES, EXPRESS OR IMPLIED,			*/
/* INCLUDING BUT NOT LIMITED TO, WARRANTIES OF MERCHANTABILITY OR				*/
/* FITNESS FOR ANY PARTICULAR PURPOSE OR THAT THE USE OF THE SOFTWARE			*/
/* OR DOCUMENTATION WILL NOT INFRINGE ANY THIRD PARTY PATENTS,					*/
/* COPYRIGHTS, TRADEMARKS OR OTHER RIGHTS.										*/
/*																				*/
/* COPYRIGHT HOLDERS WILL NOT BE LIABLE FOR ANY DIRECT, INDIRECT,				*/
/* SPECIAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF ANY USE OF THE				*/
/* SOFTWARE OR DOCUMENTATION.													*/
/*																				*/
/* The name and trademarks of copyright holders may NOT be used in				*/
/* advertising or publicity pertaining to the software without					*/
/* specific, written prior permission. Title to copyright in this				*/
/* software and any associated documentation will at all times remain			*/
/* with copyright holders.														*/
/*																				*/
/* /*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Version             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		18 Feb 2015           CTS
 *  
 /********************************************************************************/

package com.walgreens.admin.utility;

import com.walgreens.admin.bean.MachineDowntimeBean;

/**
 * @author CTS
 * 
 */
public class ServiceUtil {

	private ServiceUtil() {

	}

	/**
	 * Method checks if the downtime is Machine Downtime
	 * 
	 * @param machineReqBean
	 * @return isMachine
	 */
	public static boolean isMachineDowntime(MachineDowntimeBean machineReqBean) {
		boolean isMachine = false;
		if (null != machineReqBean.getMachineId()) {
			if (null == machineReqBean.getEquipmentId()
					|| machineReqBean.getEquipmentId().isEmpty()) {
				if (null == machineReqBean.getMediaId()
						|| machineReqBean.getMediaId().isEmpty()) {
					isMachine = true;
				} else {
					isMachine = false;
				}
			} else {
				isMachine = false;
			}
		}
		return isMachine;
	}

	/**
	 * Method checks if the downtime is Equipment Downtime
	 * 
	 * @param machineReqBean
	 * @return isEquipment
	 */
	public static boolean isEquipmentDowntime(MachineDowntimeBean machineReqBean) {
		boolean isEquipment = false;
		if (null != machineReqBean.getMachineId()
				&& null != machineReqBean.getEquipmentId()) {
			if (null == machineReqBean.getMediaId()
					|| machineReqBean.getMediaId().isEmpty()) {
				isEquipment = true;
			} else {
				isEquipment = false;
			}
		}
		return isEquipment;

	}

	/**
	 * Method checks if the downtime is Component Downtime
	 * 
	 * @param machineReqBean
	 * @return isComponent
	 */
	public static boolean isComponentDowntime(MachineDowntimeBean machineReqBean) {
		boolean isComponent = false;
		if (null != machineReqBean.getMachineId()
				&& null != machineReqBean.getEquipmentId()
				&& null != machineReqBean.getMediaId()) {
			isComponent = true;
		}
		return isComponent;
	}

}