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
"imageId",
"imageQuantity"
})
public class PrintableSign {
	@JsonProperty("imageId")
	private String  imageId;
	
	@JsonProperty("imageQuantity")
	private String  imageQuantity;

	/**
	 * @return the imageId
	 */
	@JsonProperty("imageId")
	public String getImageId() {
		return imageId;
	}

	/**
	 * @param imageId the imageId to set
	 */
	@JsonProperty("imageId")
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	/**
	 * @return the imageQuantity
	 */
	@JsonProperty("imageQuantity")
	public String getImageQuantity() {
		return imageQuantity;
	}

	/**
	 * @param imageQuantity the imageQuantity to set
	 */
	@JsonProperty("imageQuantity")
	public void setImageQuantity(String imageQuantity) {
		this.imageQuantity = imageQuantity;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PrintableSign [imageId=" + imageId + ", imageQuantity="
				+ imageQuantity + "]";
	}
	

}
