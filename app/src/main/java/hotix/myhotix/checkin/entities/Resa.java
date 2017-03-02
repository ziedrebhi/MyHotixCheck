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
        "Id",
        "Nom",
        "Code",
        "Prenom",
        "DateArrivee",
        "DateDepart",
        "PaysId",
        "Addresse",
        "DateNaissance",
        "Lieu",
        "Sexe",
        "Situation",
        "Fumeur",
        "handicape",
        "PieceId",
        "Email",
        "DocNum",
        "Gsm",
        "Profession",
        "City",
        "CodePostal",
        "Civilite"
})
public class Resa implements Serializable
{

    @JsonProperty("Id")
    private int id;
    @JsonProperty("Nom")
    private String nom;
    @JsonProperty("Code")
    private Object code;
    @JsonProperty("Prenom")
    private String prenom;
    @JsonProperty("DateArrivee")
    private String dateArrivee;
    @JsonProperty("DateDepart")
    private String dateDepart;
    @JsonProperty("PaysId")
    private int paysId;
    @JsonProperty("Addresse")
    private String addresse;
    @JsonProperty("DateNaissance")
    private String dateNaissance;
    @JsonProperty("Lieu")
    private String lieu;
    @JsonProperty("Sexe")
    private String sexe;
    @JsonProperty("Situation")
    private String situation;
    @JsonProperty("Fumeur")
    private int fumeur;
    @JsonProperty("handicape")
    private int handicape;
    @JsonProperty("PieceId")
    private int pieceId;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("DocNum")
    private String docNum;
    @JsonProperty("Gsm")
    private String gsm;
    @JsonProperty("Profession")
    private String profession;
    @JsonProperty("City")
    private String city;
    @JsonProperty("CodePostal")
    private String codePostal;
    @JsonProperty("Civilite")
    private String civilite;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -2421704810532788637L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Resa() {
    }

    /**
     *
     * @param addresse
     * @param lieu
     * @param dateNaissance
     * @param profession
     * @param pieceId
     * @param dateDepart
     * @param gsm
     * @param code
     * @param dateArrivee
     * @param nom
     * @param city
     * @param docNum
     * @param id
     * @param prenom
     * @param fumeur
     * @param codePostal
     * @param email
     * @param paysId
     * @param sexe
     * @param civilite
     * @param situation
     * @param handicape
     */
    public Resa(int id, String nom, Object code, String prenom, String dateArrivee, String dateDepart, int paysId, String addresse, String dateNaissance, String lieu, String sexe, String situation, int fumeur, int handicape, int pieceId, String email, String docNum, String gsm, String profession, String city, String codePostal, String civilite) {
        super();
        this.id = id;
        this.nom = nom;
        this.code = code;
        this.prenom = prenom;
        this.dateArrivee = dateArrivee;
        this.dateDepart = dateDepart;
        this.paysId = paysId;
        this.addresse = addresse;
        this.dateNaissance = dateNaissance;
        this.lieu = lieu;
        this.sexe = sexe;
        this.situation = situation;
        this.fumeur = fumeur;
        this.handicape = handicape;
        this.pieceId = pieceId;
        this.email = email;
        this.docNum = docNum;
        this.gsm = gsm;
        this.profession = profession;
        this.city = city;
        this.codePostal = codePostal;
        this.civilite = civilite;
    }

    @JsonProperty("Id")
    public int getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("Nom")
    public String getNom() {
        return nom;
    }

    @JsonProperty("Nom")
    public void setNom(String nom) {
        this.nom = nom;
    }

    @JsonProperty("Code")
    public Object getCode() {
        return code;
    }

    @JsonProperty("Code")
    public void setCode(Object code) {
        this.code = code;
    }

    @JsonProperty("Prenom")
    public String getPrenom() {
        return prenom;
    }

    @JsonProperty("Prenom")
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @JsonProperty("DateArrivee")
    public String getDateArrivee() {
        return dateArrivee;
    }

    @JsonProperty("DateArrivee")
    public void setDateArrivee(String dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    @JsonProperty("DateDepart")
    public String getDateDepart() {
        return dateDepart;
    }

    @JsonProperty("DateDepart")
    public void setDateDepart(String dateDepart) {
        this.dateDepart = dateDepart;
    }

    @JsonProperty("PaysId")
    public int getPaysId() {
        return paysId;
    }

    @JsonProperty("PaysId")
    public void setPaysId(int paysId) {
        this.paysId = paysId;
    }

    @JsonProperty("Addresse")
    public String getAddresse() {
        return addresse;
    }

    @JsonProperty("Addresse")
    public void setAddresse(String addresse) {
        this.addresse = addresse;
    }

    @JsonProperty("DateNaissance")
    public String getDateNaissance() {
        return dateNaissance;
    }

    @JsonProperty("DateNaissance")
    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    @JsonProperty("Lieu")
    public String getLieu() {
        return lieu;
    }

    @JsonProperty("Lieu")
    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    @JsonProperty("Sexe")
    public String getSexe() {
        return sexe;
    }

    @JsonProperty("Sexe")
    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    @JsonProperty("Situation")
    public String getSituation() {
        return situation;
    }

    @JsonProperty("Situation")
    public void setSituation(String situation) {
        this.situation = situation;
    }

    @JsonProperty("Fumeur")
    public int getFumeur() {
        return fumeur;
    }

    @JsonProperty("Fumeur")
    public void setFumeur(int fumeur) {
        this.fumeur = fumeur;
    }

    @JsonProperty("handicape")
    public int getHandicape() {
        return handicape;
    }

    @JsonProperty("handicape")
    public void setHandicape(int handicape) {
        this.handicape = handicape;
    }

    @JsonProperty("PieceId")
    public int getPieceId() {
        return pieceId;
    }

    @JsonProperty("PieceId")
    public void setPieceId(int pieceId) {
        this.pieceId = pieceId;
    }

    @JsonProperty("Email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("Email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("DocNum")
    public String getDocNum() {
        return docNum;
    }

    @JsonProperty("DocNum")
    public void setDocNum(String docNum) {
        this.docNum = docNum;
    }

    @JsonProperty("Gsm")
    public String getGsm() {
        return gsm;
    }

    @JsonProperty("Gsm")
    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    @JsonProperty("Profession")
    public String getProfession() {
        return profession;
    }

    @JsonProperty("Profession")
    public void setProfession(String profession) {
        this.profession = profession;
    }

    @JsonProperty("City")
    public String getCity() {
        return city;
    }

    @JsonProperty("City")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("CodePostal")
    public String getCodePostal() {
        return codePostal;
    }

    @JsonProperty("CodePostal")
    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    @JsonProperty("Civilite")
    public String getCivilite() {
        return civilite;
    }

    @JsonProperty("Civilite")
    public void setCivilite(String civilite) {
        this.civilite = civilite;
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