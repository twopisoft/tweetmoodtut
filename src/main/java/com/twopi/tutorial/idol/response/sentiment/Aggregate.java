
/**
 * This code was automatically generated using the jsonschema2pojo tool (http://www.jsonschema2pojo.org/)
 */
package com.twopi.tutorial.idol.response.sentiment;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "sentiment",
    "score"
})
public class Aggregate {

    @JsonProperty("sentiment")
    private Aggregate.Sentiment sentiment;
    @JsonProperty("score")
    private Double score;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The sentiment
     */
    @JsonProperty("sentiment")
    public Aggregate.Sentiment getSentiment() {
        return sentiment;
    }

    /**
     * 
     * @param sentiment
     *     The sentiment
     */
    @JsonProperty("sentiment")
    public void setSentiment(Aggregate.Sentiment sentiment) {
        this.sentiment = sentiment;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Generated("org.jsonschema2pojo")
    public static enum Sentiment {

        POSITIVE("positive"),
        NEGATIVE("negative"),
        NEUTRAL("neutral");
        private final String value;
        private static Map<String, Aggregate.Sentiment> constants = new HashMap<String, Aggregate.Sentiment>();

        static {
            for (Aggregate.Sentiment c: values()) {
                constants.put(c.value, c);
            }
        }

        private Sentiment(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static Aggregate.Sentiment fromValue(String value) {
            Aggregate.Sentiment constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
