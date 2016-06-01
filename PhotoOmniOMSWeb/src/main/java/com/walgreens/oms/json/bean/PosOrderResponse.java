package com.walgreens.oms.json.bean;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.walgreens.common.utility.MessageHeader;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "messageHeader",
    "posList"
})
public class PosOrderResponse {

    @JsonProperty("messageHeader")
    private MessageHeader messageHeader;
    @JsonProperty("posList")
    private List<PosList> posList = new ArrayList<PosList>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The MessageHeader
     */
    @JsonProperty("messageHeader")
    public MessageHeader getMessageHeader() {
        return messageHeader;
    }

    /**
     * 
     * @param MessageHeader
     *     The MessageHeader
     */
    @JsonProperty("messageHeader")
    public void setMessageHeader(MessageHeader messageHeader) {
        this.messageHeader = messageHeader;
    }

    /**
     * 
     * @return
     *     The posList
     */
    @JsonProperty("posList")
    public List<PosList> getPosList() {
        return posList;
    }

    /**
     * 
     * @param posList
     *     The posList
     */
    @JsonProperty("posList")
    public void setPosList(List<PosList> posList) {
        this.posList = posList;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
