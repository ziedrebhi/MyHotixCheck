package hotix.myhotix.checkin.activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.annotation.BoolRes;
import android.util.Log;

import hotix.myhotix.checkin.R;

public class IPServeur extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private EditTextPreference ipPreference, hoteloref;
    private SwitchPreference fumeur, handicap, date, pax, anim, nom, prenom, gender, pays, dociden, sitfam, profesion, city, codepostal,
            address, gsm, email, autoupdate, show, Signature;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefserveur);

        pref = getPreferenceManager().getSharedPreferences();
        pref.registerOnSharedPreferenceChangeListener(this);
        editor = pref.edit();
        ipPreference = (EditTextPreference) getPreferenceScreen().findPreference("SERVEUR");
        hoteloref = (EditTextPreference) getPreferenceScreen().findPreference("HOTEL");
        fumeur = (SwitchPreference) getPreferenceScreen().findPreference("FUMEUR");
        autoupdate = (SwitchPreference) getPreferenceScreen().findPreference("AUTOUPDATE");

        handicap = (SwitchPreference) getPreferenceScreen().findPreference("HANDICAP");
        date = (SwitchPreference) getPreferenceScreen().findPreference("BIRTH");
        pax = (SwitchPreference) getPreferenceScreen().findPreference("PAX");
        anim = (SwitchPreference) getPreferenceScreen().findPreference("ANIMATION");
        show = (SwitchPreference) getPreferenceScreen().findPreference("SHOWCLIENTS");

        nom = (SwitchPreference) getPreferenceScreen().findPreference("NOM");
        prenom = (SwitchPreference) getPreferenceScreen().findPreference("PRENOM");
        email = (SwitchPreference) getPreferenceScreen().findPreference("EMAIL");
        gsm = (SwitchPreference) getPreferenceScreen().findPreference("GSM");
        pays = (SwitchPreference) getPreferenceScreen().findPreference("PAYS");
        address = (SwitchPreference) getPreferenceScreen().findPreference("ADDRESS");
        codepostal = (SwitchPreference) getPreferenceScreen().findPreference("CODEPOSTAL");
        city = (SwitchPreference) getPreferenceScreen().findPreference("CITY");
        gender = (SwitchPreference) getPreferenceScreen().findPreference("GENDER");
        sitfam = (SwitchPreference) getPreferenceScreen().findPreference("SITFAM");
        profesion = (SwitchPreference) getPreferenceScreen().findPreference("PROFESSION");
        dociden = (SwitchPreference) getPreferenceScreen().findPreference("DOCIDEN");

        Signature = (SwitchPreference) getPreferenceScreen().findPreference("SIGNATURE");

        String serveur = pref.getString("SERVEUR", "");
        ipPreference.setSummary(serveur);
        String hotel = pref.getString("HOTEL", "");
        hoteloref.setSummary(hotel);

        Boolean isFumeur = pref.getBoolean("FUMEUR", true);
        fumeur.setSummary(isFumeur ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));
        Boolean isAutoUpdate = pref.getBoolean("AUTOUPDATE", false);
        autoupdate.setSummary(isAutoUpdate ? getResources().getText(R.string.oui) : getResources().getText(
                R.string.non));

        Boolean isHandicap = pref.getBoolean("HANDICAP", true);
        handicap.setSummary(isHandicap ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));


        Boolean isDate = pref.getBoolean("BIRTH", true);
        date.setSummary(isDate ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));

        Boolean isPax = pref.getBoolean("PAX", true);
        pax.setSummary(isPax ? getResources().getText(R.string.show) : getResources().getText(
                R.string.non));


        Boolean isAnime = pref.getBoolean("ANIMATION", true);
        anim.setSummary(isAnime ? getResources().getText(R.string.oui) : getResources().getText(
                R.string.non));


        Boolean isShowing = pref.getBoolean("SHOWCLIENTS", false);
        show.setSummary(isShowing ? getResources().getText(R.string.oui) : getResources().getText(
                R.string.non));


        Boolean isNom = pref.getBoolean("NOM", true);
        nom.setSummary(isNom ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));

        Boolean isprenom = pref.getBoolean("PRENOM", true);
        prenom.setSummary(isprenom ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));

        Boolean isemail = pref.getBoolean("EMAIL", true);
        email.setSummary(isemail ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));

        Boolean isgsm = pref.getBoolean("GSM", true);
        gsm.setSummary(isgsm ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));


        Boolean ispays = pref.getBoolean("PAYS", true);
        pays.setSummary(ispays ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));

        Boolean isaddress = pref.getBoolean("ADDRESS", true);
        address.setSummary(isaddress ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));


        Boolean iscity = pref.getBoolean("CITY", true);
        city.setSummary(iscity ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));

        Boolean iscode = pref.getBoolean("CODEPOSTAL", true);
        codepostal.setSummary(iscode ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));

        Boolean issex = pref.getBoolean("GENDER", true);
        gender.setSummary(issex ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));

        Boolean issitfam = pref.getBoolean("SITFAM", true);
        sitfam.setSummary(issitfam ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));


        Boolean isprof = pref.getBoolean("PROFESSION", true);
        profesion.setSummary(isprof ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));

        Boolean isdocid = pref.getBoolean("DOCIDEN", true);
        dociden.setSummary(isdocid ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));

        Boolean isSignature = pref.getBoolean("SIGNATURE", false);
        Signature.setSummary(isSignature ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));


        ipPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                Log.i("params", newValue.toString());
                editor.putString("SERVEUR", newValue.toString());
                editor.commit();
                return true;
            }
        });
        hoteloref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                Log.i("params", newValue.toString());
                editor.putString("HOTEL", newValue.toString());
                editor.commit();
                return true;
            }
        });
        fumeur.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                Log.i("params", newValue.toString());
                editor.putBoolean("FUMEUR", Boolean.valueOf(newValue.toString()));
                editor.commit();
                return true;
            }
        });
        handicap.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                Log.i("params", newValue.toString());
                editor.putBoolean("HANDICAP", Boolean.valueOf(newValue.toString()));
                editor.commit();
                return true;
            }
        });
        date.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                Log.i("params", newValue.toString());
                editor.putBoolean("BIRTH", Boolean.valueOf(newValue.toString()));
                editor.commit();
                return true;
            }
        });
        pax.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                Log.i("params", newValue.toString());
                editor.putBoolean("PAX", Boolean.valueOf(newValue.toString()));
                editor.commit();
                return true;
            }
        });
        autoupdate.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                Log.i("params", newValue.toString());
                editor.putBoolean("AUTOUPDATE", Boolean.valueOf(newValue.toString()));
                editor.commit();
                return true;
            }
        });

        anim.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                Log.i("params", newValue.toString());
                editor.putBoolean("ANIMATION", Boolean.valueOf(newValue.toString()));
                editor.commit();
                return true;
            }
        });
        show.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                Log.i("params", newValue.toString());
                editor.putBoolean("SHOWCLIENTS", Boolean.valueOf(newValue.toString()));
                editor.commit();
                return true;
            }
        });
        prenom.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                Log.i("params", newValue.toString());
                editor.putBoolean("PRENOM", Boolean.valueOf(newValue.toString()));
                editor.commit();
                return true;
            }
        });
        nom.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                Log.i("params", newValue.toString());
                editor.putBoolean("NOM", Boolean.valueOf(newValue.toString()));
                editor.commit();
                return true;
            }
        });
        city.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                Log.i("params", newValue.toString());
                editor.putBoolean("CITY", Boolean.valueOf(newValue.toString()));
                editor.commit();
                return true;
            }
        });
        address.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                Log.i("params", newValue.toString());
                editor.putBoolean("ADDRESS", Boolean.valueOf(newValue.toString()));
                editor.commit();
                return true;
            }
        });
        codepostal.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                Log.i("params", newValue.toString());
                editor.putBoolean("CODEPOSTAL", Boolean.valueOf(newValue.toString()));
                editor.commit();
                return true;
            }
        });
        pays.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                Log.i("params", newValue.toString());
                editor.putBoolean("PAYS", Boolean.valueOf(newValue.toString()));
                editor.commit();
                return true;
            }
        });
        sitfam.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                Log.i("params", newValue.toString());
                editor.putBoolean("SITFAM", Boolean.valueOf(newValue.toString()));
                editor.commit();
                return true;
            }
        });
        profesion.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                Log.i("params", newValue.toString());
                editor.putBoolean("PROFESSION", Boolean.valueOf(newValue.toString()));
                editor.commit();
                return true;
            }
        });
        gender.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                Log.i("params", newValue.toString());
                editor.putBoolean("GENDER", Boolean.valueOf(newValue.toString()));
                editor.commit();
                return true;
            }
        });
        email.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                Log.i("params", newValue.toString());
                editor.putBoolean("EMAIL", Boolean.valueOf(newValue.toString()));
                editor.commit();
                return true;
            }
        });
        gsm.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                Log.i("params", newValue.toString());
                editor.putBoolean("GSM", Boolean.valueOf(newValue.toString()));
                editor.commit();
                return true;
            }
        });
        dociden.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                Log.i("params", newValue.toString());
                editor.putBoolean("DOCIDENT", Boolean.valueOf(newValue.toString()));
                editor.commit();
                return true;
            }
        });
        Signature.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                Log.i("params", newValue.toString());
                editor.putBoolean("SIGNATURE", Boolean.valueOf(newValue.toString()));
                editor.commit();
                return true;
            }
        });
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        Log.i("params", "onSharedPreferenceChanged");
        String serveur = (pref.getString("SERVEUR", ""));
        ipPreference.setSummary(serveur);

        String hotel = (pref.getString("HOTEL", ""));
        hoteloref.setSummary(hotel);

        Boolean isFumeur = pref.getBoolean("FUMEUR", true);
        fumeur.setSummary(isFumeur ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));

        Boolean isHandicap = pref.getBoolean("HANDICAP", true);
        handicap.setSummary(isHandicap ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));


        Boolean isNom = pref.getBoolean("NOM", true);
        nom.setSummary(isNom ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));

        Boolean isprenom = pref.getBoolean("PRENOM", true);
        prenom.setSummary(isprenom ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));

        Boolean isemail = pref.getBoolean("EMAIL", true);
        email.setSummary(isemail ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));

        Boolean isgsm = pref.getBoolean("GSM", true);
        gsm.setSummary(isgsm ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));

        Boolean ispays = pref.getBoolean("PAYS", true);
        pays.setSummary(ispays ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));

        Boolean isAutoUpdate = pref.getBoolean("AUTOUPDATE", false);
        autoupdate.setSummary(isAutoUpdate ? getResources().getText(R.string.oui) : getResources().getText(
                R.string.non));

        Boolean isaddress = pref.getBoolean("ADDRESS", true);
        address.setSummary(isaddress ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));

        Boolean iscity = pref.getBoolean("CITY", true);
        city.setSummary(iscity ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));

        Boolean iscode = pref.getBoolean("CODEPOSTAL", true);
        codepostal.setSummary(iscode ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));

        Boolean issex = pref.getBoolean("GENDER", true);
        gender.setSummary(issex ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));

        Boolean issitfam = pref.getBoolean("SITFAM", true);
        sitfam.setSummary(issitfam ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));


        Boolean isprof = pref.getBoolean("PROFESSION", true);
        profesion.setSummary(isprof ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));

        Boolean isdocid = pref.getBoolean("DOCIDEN", true);
        dociden.setSummary(isdocid ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));


        Boolean isDate = pref.getBoolean("BIRTH", true);
        date.setSummary(isDate ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));


        Boolean isSignature = pref.getBoolean("SIGNATURE", false);
        Signature.setSummary(isSignature ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));


        Boolean isPax = pref.getBoolean("PAX", true);
        pax.setSummary(isPax ? getResources().getText(R.string.oui) : getResources().getText(
                R.string.non));

        Boolean isAnime = pref.getBoolean("ANIMATION", true);
        anim.setSummary(isAnime ? getResources().getText(R.string.oui) : getResources().getText(
                R.string.non));

        Boolean isShowing = pref.getBoolean("SHOWCLIENTS", false);
        show.setSummary(isShowing ? getResources().getText(R.string.oui) : getResources().getText(
                R.string.non));

        // finish();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

        super.onResume();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        finish();
        super.onStop();
    }
}
