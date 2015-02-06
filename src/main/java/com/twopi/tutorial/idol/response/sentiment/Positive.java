/**
 * This code was automatically generated using the jsonschema2pojo tool (http://www.jsonschema2pojo.org/)
 */
package com.twopi.tutorial.idol.response.sentiment;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "sentiment",
    "topic",
    "score",
    "original_text",
    "normalized_text",
    "original_length",
    "normalized_length"
})
public class Positive {

    @JsonProperty("sentiment")
    private String sentiment;
    @JsonProperty("topic")
    private String topic;
    @JsonProperty("score")
    private Double score;
    @JsonProperty("original_text")
    private String originalText;
    @JsonProperty("normalized_text")
    private String normalizedText;
    @JsonProperty("original_length")
    private Double originalLength;
    @JsonProperty("normalized_length")
    private Double normalizedLength;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The sentiment
     */
    @JsonProperty("sentiment")
    public String getSentiment() {
        return sentiment;
    }

    /**
     * 
     * @param sentiment
     *     The sentiment
     */
    @JsonProperty("sentiment")
    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    /**
     * 
     * @return
     *     The topic
     */
    @JsonProperty("topic")
    public String getTopic() {
        return topic;
    }

    /**
     * 
     * @param topic
     *     The topic
     */
    @JsonProperty("topic")
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * 
     * @return
     *     The score
     */
    @JsonProperty("score")
    public Double getScore() {
        return score;
    }

    /**
     * 
     * @param score
     *     The score
     */
    @JsonProperty("score")
    public void setScore(Double score) {
        this.score = score;
    }

    /**
     * 
     * @return
     *     The originalText
     */
    @JsonProperty("original_text")
    public String getOriginalText() {
        return originalText;
    }

    /**
     * 
     * @param originalText
     *     The original_text
     */
    @JsonProperty("original_text")
    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    /**
     * 
     * @return
     *     The normalizedText
     */
    @JsonProperty("normalized_text")
    public String getNormalizedText() {
        return normalizedText;
    }

    /**
     * 
     * @param normalizedText
     *     The normalized_text
     */
    @JsonProperty("normalized_text")
    public void setNormalizedText(String normalizedText) {
        this.normalizedText = normalizedText;
    }

    /**
     * 
     * @return
     *     The originalLength
     */
    @JsonProperty("original_length")
    public Double getOriginalLength() {
        return originalLength;
    }

    /**
     * 
     * @param originalLength
     *     The original_length
     */
    @JsonProperty("original_length")
    public void setOriginalLength(Double originalLength) {
        this.originalLength = originalLength;
    }

    /**
     * 
     * @return
     *     The normalizedLength
     */
    @JsonProperty("normalized_length")
    public Double getNormalizedLength() {
        return normalizedLength;
    }

    /**
     * 
     * @param normalizedLength
     *     The normalized_length
     */
    @JsonProperty("normalized_length")
    public void setNormalizedLength(Double normalizedLength) {
        this.normalizedLength = normalizedLength;
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
