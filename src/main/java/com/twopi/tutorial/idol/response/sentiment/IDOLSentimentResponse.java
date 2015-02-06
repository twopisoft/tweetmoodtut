/**
 * This code was automatically generated using the jsonschema2pojo tool (http://www.jsonschema2pojo.org/)
 */
package com.twopi.tutorial.idol.response.sentiment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import org.glassfish.jersey.client.ClientResponse;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "positive",
    "negative",
    "aggregate"
})
public class IDOLSentimentResponse {

    @JsonProperty("positive")
    private List<Positive> positive = new ArrayList<Positive>();
    @JsonProperty("negative")
    private List<Negative> negative = new ArrayList<Negative>();
    /**
     * 
     */
    @JsonProperty("aggregate")
    private Aggregate aggregate;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The positive
     */
    @JsonProperty("positive")
    public List<Positive> getPositive() {
        return positive;
    }

    /**
     * 
     * @param positive
     *     The positive
     */
    @JsonProperty("positive")
    public void setPositive(List<Positive> positive) {
        this.positive = positive;
    }

    /**
     * 
     * @return
     *     The negative
     */
    @JsonProperty("negative")
    public List<Negative> getNegative() {
        return negative;
    }

    /**
     * 
     * @param negative
     *     The negative
     */
    @JsonProperty("negative")
    public void setNegative(List<Negative> negative) {
        this.negative = negative;
    }

    /**
     * 
     * @return
     *     The aggregate
     */
    @JsonProperty("aggregate")
    public Aggregate getAggregate() {
        return aggregate;
    }

    /**
     * 
     * @param aggregate
     *     The aggregate
     */
    @JsonProperty("aggregate")
    public void setAggregate(Aggregate aggregate) {
        this.aggregate = aggregate;
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
