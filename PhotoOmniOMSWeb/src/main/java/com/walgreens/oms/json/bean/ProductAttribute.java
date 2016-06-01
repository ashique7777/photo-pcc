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
"nbrSets",
"nbrSheetsPrinted",
"noOfInputs",
"panaromicPrints",
"nbrIndexSheets"
})
public class ProductAttribute {
	
	@JsonProperty("nbrSets")private String nbrSets;
	@JsonProperty("nbrSheetsPrinted")private String nbrSheetsPrinted;
	@JsonProperty("noOfInputs")private String noOfInputs;
	@JsonProperty("panaromicPrints")private String  panaromicPrints;
	@JsonProperty("nbrIndexSheets")private String  nbrIndexSheets;
	
	/**
	 * @return the panaromicPrints
	 */
	@JsonProperty("panaromicPrints")
	public String getPanaromicPrints() {
		return panaromicPrints;
	}
	/**
	 * @param panaromicPrints the panaromicPrints to set
	 */
	@JsonProperty("panaromicPrints")
	public void setPanaromicPrints(String panaromicPrints) {
		this.panaromicPrints = panaromicPrints;
	}
	
	/**
	 * @return the nbrSets
	 */
	@JsonProperty("nbrSets")
	public String getNbrSets() {
		return nbrSets;
	}
	/**
	 * @param nbrSets the nbrSets to set
	 */
	@JsonProperty("nbrSets")
	public void setNbrSets(String nbrSets) {
		this.nbrSets = nbrSets;
	}
	/**
	 * @return the nbrSheetsPrinted
	 */
	@JsonProperty("nbrSheetsPrinted")
	public String getNbrSheetsPrinted() {
		return nbrSheetsPrinted;
	}
	/**
	 * @param nbrSheetsPrinted the nbrSheetsPrinted to set
	 */
	@JsonProperty("nbrSheetsPrinted")
	public void setNbrSheetsPrinted(String nbrSheetsPrinted) {
		this.nbrSheetsPrinted = nbrSheetsPrinted;
	}
	/**
	 * @return the noOfInputs
	 */
	@JsonProperty("noOfInputs")
	public String getNoOfInputs() {
		return noOfInputs;
	}
	/**
	 * @param noOfInputs the noOfInputs to set
	 */
	@JsonProperty("noOfInputs")
	public void setNoOfInputs(String noOfInputs) {
		this.noOfInputs = noOfInputs;
	}
	
	/**
	 * @return the nbrIndexSheets
	 */
	@JsonProperty("nbrIndexSheets")
	public String getNbrIndexSheets() {
		return nbrIndexSheets;
	}
	/**
	 * @param nbrIndexSheets the nbrIndexSheets to set
	 */
	@JsonProperty("nbrIndexSheets")
	public void setNbrIndexSheets(String nbrIndexSheets) {
		this.nbrIndexSheets = nbrIndexSheets;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProductAttribute [nbrSets=" + nbrSets + ", nbrSheetsPrinted="
				+ nbrSheetsPrinted + ", noOfInputs=" + noOfInputs
				+ ", panaromicPrints=" + panaromicPrints + ", nbrIndexSheets="
				+ nbrIndexSheets + "]";
	}
	
	
	
}
