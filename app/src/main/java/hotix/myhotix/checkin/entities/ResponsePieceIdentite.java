package hotix.myhotix.checkin.entities;

/**
 * Created by ziedrebhi on 02/03/2017.
 */
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "data",
        "status"
})
public class ResponsePieceIdentite implements Serializable
{

    @JsonProperty("data")
    private Data data;
    @JsonProperty("status")
    private boolean status;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -13611335238040457L;

    /**
     * No args constructor for use in serialization
     *
     */
    public ResponsePieceIdentite() {
    }

    /**
     *
     * @param status
     * @param data
     */
    public ResponsePieceIdentite(Data data, boolean status) {
        super();
        this.data = data;
        this.status = status;
    }

    @JsonProperty("data")
    public Data getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(Data data) {
        this.data = data;
    }

    public ResponsePieceIdentite withData(Data data) {
        this.data = data;
        return this;
    }

    @JsonProperty("status")
    public boolean isStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(boolean status) {
        this.status = status;
    }

    public ResponsePieceIdentite withStatus(boolean status) {
        this.status = status;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public ResponsePieceIdentite withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}