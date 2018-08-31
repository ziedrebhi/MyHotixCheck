package hotix.myhotix.checkin.entities;

/**
 * Created by ziedrebhi on 28/03/2017.
 */

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Pays",
        "PiecesId"
})
public class Data implements Serializable
{

    @JsonProperty("Pays")
    private List<Pay> pays = null;
    @JsonProperty("PiecesId")
    private List<PiecesId> piecesId = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 1979653849121723373L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Data() {
    }

    /**
     *
     * @param pays
     * @param piecesId
     */
    public Data(List<Pay> pays, List<PiecesId> piecesId) {
        super();
        this.pays = pays;
        this.piecesId = piecesId;
    }

    @JsonProperty("Pays")
    public List<Pay> getPays() {
        return pays;
    }

    @JsonProperty("Pays")
    public void setPays(List<Pay> pays) {
        this.pays = pays;
    }

    public Data withPays(List<Pay> pays) {
        this.pays = pays;
        return this;
    }

    @JsonProperty("PiecesId")
    public List<PiecesId> getPiecesId() {
        return piecesId;
    }

    @JsonProperty("PiecesId")
    public void setPiecesId(List<PiecesId> piecesId) {
        this.piecesId = piecesId;
    }

    public Data withPiecesId(List<PiecesId> piecesId) {
        this.piecesId = piecesId;
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

    public Data withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}