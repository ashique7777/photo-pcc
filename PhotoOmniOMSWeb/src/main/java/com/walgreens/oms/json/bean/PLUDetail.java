/**
 * 
 */
package com.walgreens.oms.json.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author CTS
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
"pluId",
"discountAmt"
})
public class PLUDetail {
	
	@JsonProperty("pluId")
	private String  pluId;
	@JsonProperty("discountAmt")
	private String  discountAmt;
	/**
	 * @return the pluId
	 */
	@JsonProperty("pluId")
	public String getPluId() {
		return pluId;
	}

	/**
	 * @param pluId the pluId to set
	 */
	@JsonProperty("pluId")
	public void setPluId(String pluId) {
		this.pluId = pluId;
	}

	/**
	 * @return the discountAmt
	 */
	@JsonProperty("discountAmt")
	public String getDiscountAmt() {
		return discountAmt;
	}

	/**
	 * @param discountAmt the discountAmt to set
	 */
	@JsonProperty("discountAmt")
	public void setDiscountAmt(String discountAmt) {
		this.discountAmt = discountAmt;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PLUDetail [pluId=" + pluId + ", discountAmt=" + discountAmt
				+ "]";
	}

	
}
