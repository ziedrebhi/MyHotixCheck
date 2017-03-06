package hotix.myhotix.checkin.activity;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Client implements Parcelable {

    int ClientId; //
    int Civilite; //
    String ClientNom; //
    String ClientPrenom; //
    int Pays; //
    String Adresse; //
    String DateNaiss; //
    String LieuNaiss; //
    String Sexe; //
    String SitFam; //
    int Fumeur; //
    int Handicape;//
    int NatureDocIdentite; //
    String NumDocIdentite; //
    String Email; //
    String Gsm; //
    String Profession; //
    String City; //
    String CodePostal;
    String Image;

    public static final Creator<Client> CREATOR = new Creator<Client>() {
        @Override
        public Client createFromParcel(Parcel in) {
            return new Client(in);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public Client() {

    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getCivilite() {
        return Civilite;
    }

    public void setCivilite(int civilite) {
        Civilite = civilite;
    }

    private Client(Parcel in) {
        ClientId = in.readInt();
        Civilite = in.readInt();
        ClientNom = in.readString();
        ClientPrenom = in.readString();
        Pays = in.readInt();
        Adresse = in.readString();
        DateNaiss = in.readString();
        LieuNaiss = in.readString();
        Sexe = in.readString();
        SitFam = in.readString();
        Fumeur = in.readInt();
        Handicape = in.readInt();
        NatureDocIdentite = in.readInt();
        NumDocIdentite = in.readString();
        Email = in.readString();
        Gsm = in.readString();
        Profession = in.readString();
        City = in.readString();
        CodePostal = in.readString();
        Image = in.readString();
    }

    @Override
    public String toString() {
        return "Client{" +
                "ClientId=" + ClientId +
                ", ClientNom='" + ClientNom + '\'' +
                ", Civilite='" + Civilite + '\'' +
                ", ClientPrenom='" + ClientPrenom + '\'' +
                ", Pays=" + Pays +
                ", Adresse='" + Adresse + '\'' +
                ", DateNaiss='" + DateNaiss + '\'' +
                ", LieuNaiss='" + LieuNaiss + '\'' +
                ", Sexe='" + Sexe + '\'' +
                ", SitFam='" + SitFam + '\'' +
                ", Fumeur=" + Fumeur +
                ", Handicape=" + Handicape +
                ", NatureDocIdentite=" + NatureDocIdentite +
                ", NumDocIdentite='" + NumDocIdentite + '\'' +
                ", Email='" + Email + '\'' +
                ", Gsm='" + Gsm + '\'' +
                ", Profession='" + Profession + '\'' +
                ", City='" + City + '\'' +
                ", CodePostal='" + CodePostal + '\'' +
                ", Image='" + Image + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ClientId);
        dest.writeInt(Civilite);
        dest.writeString(ClientNom);
        dest.writeString(ClientPrenom);
        dest.writeInt(Pays);
        dest.writeString(Adresse);
        dest.writeString(DateNaiss);
        dest.writeString(LieuNaiss);
        dest.writeString(Sexe);
        dest.writeString(SitFam);
        dest.writeInt(Fumeur);
        dest.writeInt(Handicape);
        dest.writeInt(NatureDocIdentite);
        dest.writeString(NumDocIdentite);
        dest.writeString(Email);
        dest.writeString(Gsm);
        dest.writeString(Profession);
        dest.writeString(City);
        dest.writeString(CodePostal);
        dest.writeString(Image);
    }


    public int getClientId() {
        return ClientId;
    }

    public void setClientId(int clientId) {
        ClientId = clientId;
    }

    public String getClientNom() {
        return ClientNom;
    }

    public void setClientNom(String clientNom) {
        ClientNom = clientNom;
    }

    public String getClientPrenom() {
        return ClientPrenom;
    }

    public void setClientPrenom(String clientPrenom) {
        ClientPrenom = clientPrenom;
    }

    public int getPays() {
        return Pays;
    }

    public void setPays(int pays) {
        Pays = pays;
    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String adresse) {
        Adresse = adresse;
    }

    public String getDateNaiss() {
        return DateNaiss;
    }

    public void setDateNaiss(String dateNaiss) {
        DateNaiss = dateNaiss;
    }

    public String getLieuNaiss() {
        return LieuNaiss;
    }

    public void setLieuNaiss(String lieuNaiss) {
        LieuNaiss = lieuNaiss;
    }

    public String getSexe() {
        return Sexe;
    }

    public void setSexe(String sexe) {
        Sexe = sexe;
    }

    public String getSitFam() {
        return SitFam;
    }

    public void setSitFam(String sitFam) {
        SitFam = sitFam;
    }

    public int getFumeur() {
        return Fumeur;
    }

    public void setFumeur(int fumeur) {
        Fumeur = fumeur;
    }

    public int getHandicape() {
        return Handicape;
    }

    public void setHandicape(int handicape) {
        Handicape = handicape;
    }

    public int getNatureDocIdentite() {
        return NatureDocIdentite;
    }

    public void setNatureDocIdentite(int natureDocIdentite) {
        NatureDocIdentite = natureDocIdentite;
    }

    public String getNumDocIdentite() {
        return NumDocIdentite;
    }

    public void setNumDocIdentite(String numDocIdentite) {
        NumDocIdentite = numDocIdentite;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGsm() {
        return Gsm;
    }

    public void setGsm(String gsm) {
        Gsm = gsm;
    }

    public String getProfession() {
        return Profession;
    }

    public void setProfession(String profession) {
        Profession = profession;
    }

    @Override
    public boolean equals(Object client) {
        boolean isEqual = false;

        if (client != null && client instanceof Client) {
            isEqual = ((this.getClientId() == ((Client) client).getClientId())
                    && (this.getSitFam().equals(((Client) client).getSitFam()))
                    && (this.getSexe().equals(((Client) client).getSexe()))
                    && (this.getAdresse().equals(((Client) client).getAdresse()))
                    && (this.getClientNom().equals(((Client) client).getClientNom()))
                    && (this.getClientPrenom().equals(((Client) client).getClientPrenom()))
                    && (this.getDateNaiss().equals(((Client) client).getDateNaiss()))
                    && (this.getLieuNaiss().equals(((Client) client).getLieuNaiss()))
                    && (this.getNatureDocIdentite() == ((Client) client).getNatureDocIdentite())
                    && (this.getNumDocIdentite().equals(((Client) client).getNumDocIdentite()))
                    && (this.getFumeur() == ((Client) client).getFumeur())
                    && (this.getCivilite() == ((Client) client).getCivilite())
                    && (this.getHandicape() == ((Client) client).getHandicape())
                    && (this.getEmail().equals(((Client) client).getEmail()))
                    && (this.getGsm().equals(((Client) client).getGsm()))
                    && (this.getPays() == ((Client) client).getPays())
                    && (this.getCity() == ((Client) client).getCity())
                    && (this.getCodePostal() == ((Client) client).getCodePostal())
                    && (this.getProfession().equals(((Client) client).getProfession())));

        }
        Log.i("EQUALS", String.valueOf(isEqual));
        return isEqual;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int hashCode() {
        return this.getClientId();
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCodePostal() {
        return CodePostal;
    }

    public void setCodePostal(String codePostal) {
        CodePostal = codePostal;
    }
}
