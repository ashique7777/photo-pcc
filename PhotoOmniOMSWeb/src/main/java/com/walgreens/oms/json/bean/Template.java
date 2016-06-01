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
"templateId",
"quantity"
})
public class Template {
	
	@JsonProperty("templateId")private String templateId;
	@JsonProperty("quantity")private String quantity;	
	
	/**
	 * @return the templateId
	 */
	@JsonProperty("templateId")
	public String getTemplateId() {
		return templateId;
	}
	/**
	 * @param templateId the templateId to set
	 */
	@JsonProperty("templateId")
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	/**
	 * @return the quantity
	 */
	@JsonProperty("quantity")
	public String getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	@JsonProperty("quantity")
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Template [templateId=" + templateId + ", quantity=" + quantity
				+ "]";
	}
	

}
