package hotix.myhotix.checkin.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hotix.myhotix.checkin.R;
import hotix.myhotix.checkin.entities.PieceIdentite;
import hotix.myhotix.checkin.entities.Resa;
import hotix.myhotix.checkin.entities.ResponsePieceIdentite;
import hotix.myhotix.checkin.entities.ResponseResa;
import hotix.myhotix.checkin.utilities.HttpHandler;
import hotix.myhotix.checkin.utilities.UpdateChecker;

public class SplashScreen extends AppCompatActivity {

    // ************ Paramètres Web Services ************
    public final String NAMESPACE = "http://tempuri.org/";

    public final String SOAP_ACTION_PAYS = "http://tempuri.org/GetAllPays";
    public final String METHOD_NAME_PAYS = "GetAllPays";

    public final String SOAP_ACTION_PIECE_IDENDITE = "http://tempuri.org/GetAllPiecesIdendite";
    public final String METHOD_NAME_PIECE_IDENDITE = "GetAllPiecesIdendite";

    public final String SOAP_ACTION_GET_RESA = "http://tempuri.org/GetReservation";
    public final String METHOD_NAME_GET_RESA = "GetReservation";

    static int y1, m1, d1;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    ProgressDialog customProgress1;
    AlertDialog.Builder alertDialogBuilder;
    SharedPreferences pref;
    TextView version;
    EditText editSearch;
    String refResa;
    ArrayList<Client> curClients;
    Boolean exception = false;
    ArrayList<ItemSpinner> listPays;
    ArrayList<String> pays;
    ArrayList<ItemSpinner> listDocTypes;
    ArrayList<String> docTypes;

    HttpRequestTaskPays wsPaysApi;
    HttpRequestTaskIdentite wsPIApi;
    private CoordinatorLayout coordinatorLayout;
    private Toolbar toolbar;
    ImageView background;
    UpdateChecker checker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .LinearLayout1);

        version = (TextView) findViewById(R.id.version);
        editSearch = (EditText) findViewById(R.id.inputSearch);
        background = (ImageView) findViewById(R.id.companyIcon3);


        customProgress1 = new ProgressDialog(SplashScreen.this);
        curClients = new ArrayList<Client>();
        setTitle("Check In");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.getBackground().setAlpha(0);
        setSupportActionBar(toolbar);
        pays = new ArrayList<String>();
        docTypes = new ArrayList<String>();
        listPays = new ArrayList<ItemSpinner>();
        listDocTypes = new ArrayList<ItemSpinner>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setShowHideAnimationEnabled(true);
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().set//Logo(R.drawable.ic_launcher);
        editSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                ////Log.i("Clavier", "Here");
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // //Log.i("Clavier", "ACTION_UP");
                    if (event.getRawX() >= (editSearch.getRight() - editSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        //  //Log.i("Clavier", "drawable");
                        if (!editSearch.getText().toString().trim().equals("")) {
                            refResa = editSearch.getText().toString().trim();
                            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            im.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
                            if (isOnline()) {
                                if ((listDocTypes.size() != 0) && (listPays.size() != 0)) {
                                    HttpRequestTaskReservation ws = new HttpRequestTaskReservation();
                                    ws.execute();
                                } else {
                                    ShowSnakeBarInfo(5);
                                }
                            }


                        } else {
                            //editSearch.setError(getResources().getText(R.string.champs_requis));

                            Animation shake = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.shake);
                            editSearch.startAnimation(shake);
                        }

                        return true;
                    } else {
                        // //Log.i("Clavier", "Search");
                        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        im.showSoftInput(editSearch, 0);
                    }

                }
                return true;
            }
        });
        PackageManager manager = this.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);

            version.setText(getResources().getText(
                    R.string.hotix) + info.versionName);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String serveur = (pref.getString("SERVEUR", ""));
        if (serveur.equals("")) {

            Intent i = new Intent(SplashScreen.this, IPServeur.class);
            startActivity(i);

        }


        checker = new UpdateChecker(this, true);

    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean isconnected = netInfo != null && netInfo.isConnectedOrConnecting();
        if (isconnected) {
            ShowSnakeBarInfo(3);

        } else {

            RetryConnection();
        }
        return isconnected;

    }

    private void RetryConnection() {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, getResources().getText(
                        R.string.msg_pas_de_connexion), Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getText(
                        R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isOnline();
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);

        snackbar.show();
    }

    private void RetryGetData() {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, getResources().getText(
                        R.string.error_load_data), Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getText(
                        R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getDataPaysIden();
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);

        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_settings was selected
            case R.id.action_settings:

                Intent i = new Intent(SplashScreen.this, IPServeur.class);
                startActivity(i);
                break;
            case R.id.update:
                boolean lastVersion = CheckVersionApp();
                PackageManager manager5 = this.getPackageManager();
                String version = "";
                PackageInfo info5;
                try {
                    info5 = manager5.getPackageInfo(this.getPackageName(), 0);
                    version = info5.versionName;

                } catch (NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (lastVersion) {
                    android.support.v7.app.AlertDialog.Builder alertDialogBuilder2 = new android.support.v7.app.AlertDialog.Builder(this);


                    alertDialogBuilder2.setTitle(getResources().getText(
                            R.string.app_name));
                    alertDialogBuilder2.setMessage("Version : " + version + "\n" + getResources().getText(
                            R.string.last_version_ok));
                    alertDialogBuilder2.setIcon(R.drawable.ic_launcher);

                    alertDialogBuilder2.setCancelable(false);
                    alertDialogBuilder2.setPositiveButton(getResources().getText(
                            R.string.close),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //ResetFields();

                                }
                            });


                    alertDialogBuilder2.show();

                } else {
                    android.support.v7.app.AlertDialog.Builder alertDialogBuilder2 = new android.support.v7.app.AlertDialog.Builder(this);


                    alertDialogBuilder2.setTitle(getResources().getText(
                            R.string.app_name));
                    alertDialogBuilder2.setMessage("Version : " + version + "\n" + getResources().getText(
                            R.string.last_version_no));
                    alertDialogBuilder2.setIcon(R.drawable.ic_launcher);

                    alertDialogBuilder2.setCancelable(false);
                    alertDialogBuilder2.setPositiveButton(getResources().getText(
                            R.string.update),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //ResetFields();
                                    DownloadAndInstallLastVersion();
                                }
                            });


                    alertDialogBuilder2.show();
                }
                break;
            case R.id.help:

                Intent i2 = new Intent(SplashScreen.this, AboutActivity.class);
                startActivity(i2);
                break;

            default:
                break;
        }

        return true;
    }


    public boolean CheckVersionApp() {
        boolean lastVersion = true;
        String serveur = (pref.getString("SERVEUR", ""));
        if (!serveur.equals("")) {
            checker.checkForUpdateByVersionCode("http://" + serveur + "/Android/version.txt");

            if (checker.isUpdateAvailable()) {
                Log.i("Update", "True");
                lastVersion = false;

            }
        }
        return lastVersion;
    }

    public void DownloadAndInstallLastVersion() {
        String serveur = (pref.getString("SERVEUR", ""));
        if (!serveur.equals("")) {

            checker.downloadAndInstall("http://" + serveur + "/Android/app.apk");
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

        super.onResume();
        wsPaysApi = new HttpRequestTaskPays();
        wsPIApi = new HttpRequestTaskIdentite();
        pref = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        getDataPaysIden();
        String codeHotel = pref.getString("HOTEL", "");
        switch (codeHotel) {
            case "1866":
                // Badira
                if (pref.getBoolean("ANIMATION", true)) {
                    // //Log.i("Animation", "TRUE");
                    AnimationDrawable animation = new AnimationDrawable();
                    animation.addFrame(getResources().getDrawable(R.drawable.reception), 5000);
                    animation.addFrame(getResources().getDrawable(R.drawable.receptiona), 5000);
                    animation.addFrame(getResources().getDrawable(R.drawable.receptionb), 5000);
                    animation.addFrame(getResources().getDrawable(R.drawable.receptionc), 5000);
                    animation.setOneShot(false);


                    background.setImageDrawable(animation);

                    // start the animation!
                    animation.start();
                } else {
                    ////Log.i("Animation", "TRUE");
                    background.setImageResource(R.drawable.reception);
                }
                break;
            default:
                background.setImageResource(R.drawable.hotel);
                break;
        }

        curClients = new ArrayList<Client>();
        editSearch.setText("");
        Boolean autoupdate = pref.getBoolean("AUTOUPDATE", false);
        if (autoupdate) {
            Log.i("AUTO UPDATE", "TRUE");

            if (!CheckVersionApp()) {
                android.support.v7.app.AlertDialog.Builder alertDialogBuilder2 = new android.support.v7.app.AlertDialog.Builder(this);
                Log.i("AUTO UPDATE", "OUTDATE : GET LAST VERSION");
                PackageManager manager = this.getPackageManager();
                alertDialogBuilder2.setTitle(getResources().getText(
                        R.string.app_name));
                alertDialogBuilder2.setMessage(getResources().getText(
                        R.string.last_version_no));
                alertDialogBuilder2.setIcon(R.drawable.ic_launcher);

                alertDialogBuilder2.setCancelable(false);
                alertDialogBuilder2.setPositiveButton(getResources().getText(
                        R.string.update),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //ResetFields();
                                DownloadAndInstallLastVersion();
                            }
                        });


                alertDialogBuilder2.show();
            } else {
                Log.i("AUTO UPDATE", "UPDATED : You have last version");

            }
        } else {
            Log.i("AUTO UPDATE", "FALSE");

        }


    }

    private void getDataPaysIden() {
        String serveur = (pref.getString("SERVEUR", "192.168.0.72"));
        if (!serveur.equals("")) {

            if (isOnline()) {
                if (((listDocTypes != null) && (listDocTypes.size() == 0)) || ((listPays != null) && (listPays.size() == 0)))
                    wsPaysApi.execute();

            }
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }


    public String getURL() {
        String URL = null;
        try {
            SharedPreferences sp = PreferenceManager
                    .getDefaultSharedPreferences(this);
            URL = sp.getString("SERVEUR", "");
            ////Log.i("URL", URL);
            URL = "http://" + URL + "/hngwebsetup/webservice/myhotix.asmx";
            //Log.i("URL", URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return URL;
    }

    private void ShowSnakeBarInfo(int i) {
        ////Log.i("ShowSnakeBarInfo", String.valueOf(i));
        Snackbar snackbar;

        switch (i) {
            case 0:
                // pas de clients
                snackbar = Snackbar
                        .make(coordinatorLayout, getResources().getText(
                                R.string.msg_pas_de_donnees_client), Snackbar.LENGTH_LONG);
                ViewGroup group = (ViewGroup) snackbar.getView();
                group.setBackgroundColor(ContextCompat.getColor(SplashScreen.this, R.color.colorPrimary));
                snackbar.show();
                break;
            case 1:
                // pas de connexion
                snackbar = Snackbar
                        .make(coordinatorLayout, getResources().getText(
                                R.string.msg_pas_de_connexion), Snackbar.LENGTH_LONG);
                ViewGroup group2 = (ViewGroup) snackbar.getView();
                group2.setBackgroundColor(ContextCompat.getColor(SplashScreen.this, R.color.colorPrimary));
                snackbar.show();
                break;
            case 2:
                // erreur serveur
                snackbar = Snackbar
                        .make(coordinatorLayout, getResources().getText(
                                R.string.msg_erreur_serveur), Snackbar.LENGTH_LONG);
                ViewGroup group3 = (ViewGroup) snackbar.getView();
                group3.setBackgroundColor(ContextCompat.getColor(SplashScreen.this, R.color.colorPrimary));
                snackbar.show();
                break;
            case 3:
                // connected
                snackbar = Snackbar
                        .make(coordinatorLayout, getResources().getText(
                                R.string.msg_connected), Snackbar.LENGTH_LONG);
                ViewGroup group4 = (ViewGroup) snackbar.getView();
                group4.setBackgroundColor(ContextCompat.getColor(SplashScreen.this, R.color.colorPrimary));
                snackbar.show();
                break;
            case 4:
                // required
                snackbar = Snackbar
                        .make(coordinatorLayout, getResources().getText(
                                R.string.champs_requis), Snackbar.LENGTH_LONG);
                ViewGroup group5 = (ViewGroup) snackbar.getView();
                group5.setBackgroundColor(ContextCompat.getColor(SplashScreen.this, R.color.colorPrimary));
                snackbar.show();

                break;
            case 5:
           /*     snackbar = Snackbar
                        .make(coordinatorLayout, getResources().getText(
                                R.string.error_load_data), Snackbar.LENGTH_LONG);
                ViewGroup group6 = (ViewGroup) snackbar.getView();
                group6.setBackgroundColor(ContextCompat.getColor(SplashScreen.this, R.color.colorPrimary));
                snackbar.show();*/
                Log.i("Asynck Pays", wsPaysApi.getStatus().toString());
                wsPaysApi = null;
                wsPaysApi = new HttpRequestTaskPays();
                wsPIApi = new HttpRequestTaskIdentite();

                RetryGetData();
                Log.i("Asynck Pays2", wsPaysApi.getStatus().toString());

                break;
        }
    }


    /* Asynck Task */
    /******************************************************************/
    /*********************       OLD         **************************/
    /******************************************************************/

    public class AsyncGetReservation extends AsyncTask<String, String, String> {

        HttpTransportSE androidHttpTransport;
        SoapObject response = null;

        @Override
        protected void onPreExecute() {

            customProgress1.setMessage(getResources().getText(
                    R.string.msg_loading_resa));
            customProgress1.setCancelable(false);
            if (customProgress1 != null) {
                customProgress1.show();
            }

            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_GET_RESA);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            PropertyInfo pi_resa_ref = new PropertyInfo();
            pi_resa_ref.setName("resaRef");
            pi_resa_ref.setValue(refResa);
            pi_resa_ref.setType(String.class);
            request.addProperty(pi_resa_ref);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            androidHttpTransport = new HttpTransportSE(getURL(), 6000);

            try {
                androidHttpTransport.call(SOAP_ACTION_GET_RESA, envelope);
                response = (SoapObject) envelope.getResponse();
            } catch (SocketTimeoutException e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        androidHttpTransport.reset();
                        ////Log.i("SocketTimeoutException", "Here");

                        //3 ShowErrorConnectionDialog();
                        exception = true;
                        response = null;
                    }
                });

            } catch (HttpResponseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //  //Log.i("HttpResponseException", "Here");
                //ShowErrorConnectionDialog();
                exception = true;
                response = null;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                exception = true;
                // //Log.i("IOException", "Here");
                //ShowErrorConnectionDialog();
                response = null;
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
                // //Log.i("XmlPullParserException", "Here");
                exception = true;
                //ShowErrorConnectionDialog();
                response = null;
                exception = true;
            }
            if (response != null) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //Log.i("Response", response.toString());

                        SoapObject Reservations = new SoapObject();
                        SoapObject Reservation = new SoapObject();
                        // Lecture de fichier XML
                        Reservations = (SoapObject) response.getProperty(0);
                        // //Log.i("Reservations", Reservations.toString());

                        for (int i = 0; i < Reservations.getPropertyCount(); i++) {
                            Reservation = (SoapObject) Reservations.getProperty(i);
                            // //Log.i("Reservation", Reservation.toString());
                            // ID
                            Client curClient = new Client();
                            curClient.setClientId(Integer.parseInt(Reservation.getProperty(
                                    "ClientId").toString()));

                            if (curClient.getClientId() != 0) {
                                // Civilite
                                if (Reservation.getProperty("Civilite").toString()
                                        .equals("anyType{}"))
                                    curClient.setCivilite(1);
                                else
                                    curClient.setCivilite(Integer.parseInt(Reservation.getProperty("Civilite").toString()));

                                // NOM
                                if (Reservation.getProperty("ClientNom").toString()
                                        .equals("anyType{}"))
                                    curClient.setClientNom("");
                                else
                                    curClient.setClientNom(Reservation.getProperty(
                                            "ClientNom").toString());

                                // PRENOM
                                if (Reservation.getProperty("ClientPrenom").toString()
                                        .equals("anyType{}"))
                                    curClient.setClientPrenom("");
                                else
                                    curClient.setClientPrenom(Reservation.getProperty(
                                            "ClientPrenom").toString());

                                // DATE NAISSANCE
                                if (Reservation.getProperty("DateNaiss").toString()
                                        .equals("anyType{}"))
                                    curClient.setDateNaiss("");
                                else {
                                    curClient.setDateNaiss(FormatDate(Reservation
                                            .getProperty("DateNaiss").toString()));

                                   /* InitDateNaissance(FormatDate(Reservation.getProperty(
                                            "DateNaiss").toString()));*/
                                }

                                // LIEU NAISSANCE
                                if (Reservation.getProperty("LieuNaiss").toString()
                                        .equals("anyType{}"))
                                    curClient.setLieuNaiss("");
                                else
                                    curClient.setLieuNaiss(Reservation.getProperty(
                                            "LieuNaiss").toString());

                                // City
                                if (Reservation.getProperty("City").toString()
                                        .equals("anyType{}"))
                                    curClient.setCity("");
                                else
                                    curClient.setCity(Reservation.getProperty(
                                            "City").toString());
                                // CodePostal
                                if (Reservation.getProperty("CodePostal").toString()
                                        .equals("anyType{}"))
                                    curClient.setCodePostal("");
                                else
                                    curClient.setCodePostal(Reservation.getProperty(
                                            "CodePostal").toString());

                                // PAYS
                                curClient.setPays(Integer.parseInt(Reservation.getProperty(
                                        "Pays").toString()));

                                // ADRESSE
                                if (Reservation.getProperty("Adresse").toString()
                                        .equals("anyType{}"))
                                    curClient.setAdresse("");
                                else
                                    curClient.setAdresse(Reservation.getProperty("Adresse")
                                            .toString());


                                // SEXE
                                if (Reservation.getProperty("Sexe").toString()
                                        .equals("anyType{}"))
                                    curClient.setSexe("M");
                                else
                                    curClient.setSexe(Reservation.getProperty("Sexe")
                                            .toString());

                                // SITUATION FAMILIALE
                                if (Reservation.getProperty("SitFam").toString()
                                        .equals("anyType{}"))
                                    curClient.setSitFam("C");
                                else
                                    curClient.setSitFam(Reservation.getProperty("SitFam")
                                            .toString());

                                // FUMEUR
                                if (Reservation.getProperty("Fumeur").toString()
                                        .equals("anyType{}"))
                                    curClient.setFumeur(0);
                                else if (Reservation.getProperty("Fumeur").toString()
                                        .equals("True"))
                                    curClient.setFumeur(1);
                                else
                                    curClient.setFumeur(0);

                                // HANDICAPE
                                if (Reservation.getProperty("Handicape").toString()
                                        .equals("anyType{}"))
                                    curClient.setHandicape(0);
                                else if (Reservation.getProperty("Handicape").toString()
                                        .equals("True"))
                                    curClient.setHandicape(1);
                                else
                                    curClient.setHandicape(0);

                                // TYPE DOCUMENT IDENDITE
                                if (Reservation.getProperty("DocTypeId").toString()
                                        .equals("anyType{}"))
                                    curClient.setNatureDocIdentite(1);
                                else
                                    curClient.setNatureDocIdentite(Integer
                                            .parseInt(Reservation.getProperty("DocTypeId")
                                                    .toString()));

                                // NUM DOCUMENT IDENDITE
                                if (Reservation.getProperty("DocIdNum").toString()
                                        .equals("anyType{}"))
                                    curClient.setNumDocIdentite("");
                                else
                                    curClient.setNumDocIdentite(Reservation.getProperty(
                                            "DocIdNum").toString());

                                // E-MAIL
                                if (Reservation.getProperty("Email").toString()
                                        .equals("anyType{}"))
                                    curClient.setEmail("");
                                else
                                    curClient.setEmail(Reservation.getProperty("Email")
                                            .toString());

                                // GSM
                                if (Reservation.getProperty("Gsm").toString()
                                        .equals("anyType{}"))
                                    curClient.setGsm("");
                                else
                                    curClient.setGsm(Reservation.getProperty("Gsm")
                                            .toString());

                                // PROFESSION
                                if (Reservation.getProperty("Profession").toString()
                                        .equals("anyType{}"))
                                    curClient.setProfession("");
                                else
                                    curClient.setProfession(Reservation.getProperty(
                                            "Profession").toString());
                                curClients.add(curClient);
                            }
                        }
                    }
                });


            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (customProgress1 != null) {
                customProgress1.hide();
            }
            androidHttpTransport.reset();

            if (curClients.size() != 0) {

                ////Log.i("Clients", curClients.toString());
                Intent intent = new Intent(SplashScreen.this, ClientsActivity.class);
                intent.putExtra("NBR_CLIENTS", curClients.size());
                intent.putExtra("RESA_REF", refResa);
                intent.putExtra("CLIENTS", curClients);
                intent.putExtra("PAYS", listPays);
                intent.putExtra("DOCS", listDocTypes);
                startActivity(intent);
                for (Client cli : curClients) {
                    Log.i("Splash Activity Client", cli.toString());
                }


            } else {
                if (exception)
                    ShowSnakeBarInfo(2);
                else
                    ShowSnakeBarInfo(0);
            }

            super.onPostExecute(result);
        }
    }

    public class AsyncGetAllPays extends
            AsyncTask<String, String, ArrayList<ItemSpinner>> {
        HttpTransportSE androidHttpTransport;
        SoapObject response = null;

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            // //Log.i("onCancelled", "AsyncGetAllPays");
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            // //Log.i("AsyncGetAllPays", "onPreExecute");
            customProgress1.setMessage(getResources().getText(
                    R.string.msg_loading_resa));

            customProgress1.show();

            super.onPreExecute();
        }

        protected ArrayList<ItemSpinner> doInBackground(String... params) {
            ////Log.i("AsyncGetAllPays", "doInBackground");
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_PAYS);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            androidHttpTransport = new HttpTransportSE(getURL(), 5000);

            try {
                androidHttpTransport.call(SOAP_ACTION_PAYS, envelope);
                response = (SoapObject) envelope.getResponse();

            } catch (SocketTimeoutException e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        customProgress1.hide();
                        androidHttpTransport.reset();
                        //Log.i("SocketTimeoutException", "Here");
                        exception = true;
                        response = null;
                    }
                });

            } catch (HttpResponseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        //Log.i("HttpResponseException", "Here");
                        customProgress1.hide();
                        exception = true;
                        response = null;
                    }
                });

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();


                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        //Log.i("IOException", "Here");
                        customProgress1.hide();
                        exception = true;
                        response = null;
                    }
                });
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                exception = true;
                e.printStackTrace();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        //Log.i("XmlPullParserException", "Here");
                        customProgress1.hide();
                        exception = true;
                        response = null;
                    }
                });


            }

            if (response != null) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                    }
                });

                ////Log.i("AsyncGetAllPays", response.toString());
                ItemSpinner item;
                SoapObject Pays = new SoapObject();
                SoapObject UnPays = new SoapObject();

                //Log.i("Pays", response.toString());

                // Lecture de fichier XML
                Pays = (SoapObject) response.getProperty(0);

                ////Log.i("N° PAYS", String.valueOf(Pays.getPropertyCount()));

                for (int i = 0; i < Pays.getPropertyCount(); i++) {

                    UnPays = (SoapObject) Pays.getProperty(i);
                    try {
                        item = new ItemSpinner();
                        item.setId(Integer.parseInt(UnPays
                                .getProperty("PaysId").toString()));
                        item.setLabel(UnPays.getProperty("PaysNom").toString());
                        item.setTel(UnPays.getProperty("PaysTel").toString());
                        // DATE NAISSANCE
                        if (UnPays.getProperty("PaysTel").toString()
                                .equals("anyType{}"))
                            item.setTel("");
                        else {
                            item.setTel(UnPays
                                    .getProperty("PaysTel").toString());
                        }

                        listPays.add(item);
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                //Log.i("PAYS", "Empty");
                                customProgress1.hide();
                                exception = true;
                                ShowSnakeBarInfo(5);
                                Log.i("PAYS", "Empty");

                            }
                        });
                    }
                }

            }

            return listPays;
        }

        @Override
        protected void onPostExecute(ArrayList<ItemSpinner> listPays) {
            androidHttpTransport.reset();
            // //Log.i("AsyncGetAllPays", "onPostExecute");
            //DisplaySpinner(spPays, pays, listPays);
            if (listPays.size() == 0) {
                if (exception) {
                    Log.i("PAYS", "onPostExecute");
                    ShowSnakeBarInfo(5);
                } else
                    ShowSnakeBarInfo(0);
            } else {
                //Log.i("ALL_PAYS", listPays.toString());

             /*   Bundle value = new Bundle();
                value.putParcelableArrayList("PAYS", listPays);*/
                wsPIApi.execute();
                //customProgress1.hide();
            }
            //wsPI.execute();
            super.onPostExecute(listPays);
        }
    }

    public class AsyncGetAllPiecesIdendite extends
            AsyncTask<String, String, ArrayList<ItemSpinner>> {
        HttpTransportSE androidHttpTransport;
        SoapObject response = null;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected ArrayList<ItemSpinner> doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME_PIECE_IDENDITE);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            androidHttpTransport = new HttpTransportSE(getURL(), 5000);

            try {
                androidHttpTransport.call(SOAP_ACTION_PIECE_IDENDITE, envelope);
                response = (SoapObject) envelope.getResponse();
            } catch (SocketTimeoutException e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        androidHttpTransport.reset();
                        // //Log.i("SocketTimeoutException", "Here");
//                        btn_ok.setEnabled(false);
//                        btn_update.setEnabled(false);
                        exception = true;

                        response = null;
                    }
                });

            } catch (HttpResponseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                ////Log.i("HttpResponseException", "Here");

                response = null;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                ////Log.i("IOException", "Here");
                exception = true;

                response = null;
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                exception = true;

                e.printStackTrace();
                ////Log.i("XmlPullParserException", "Here");

                response = null;
            }

            if (response != null) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
//                        btn_ok.setEnabled(true);
//                        btn_update.setEnabled(true);
                    }
                });

                ItemSpinner item;
                SoapObject DocTypes = new SoapObject();
                SoapObject DocType = new SoapObject();

                // Lecture de fichier XML
                DocTypes = (SoapObject) response.getProperty(0);

                for (int i = 0; i < DocTypes.getPropertyCount(); i++) {
                    DocType = (SoapObject) DocTypes.getProperty(i);
                    try {
                        item = new ItemSpinner();
                        item.setId(Integer.parseInt(DocType.getProperty(
                                "PieceId").toString()));
                        item.setLabel(DocType.getProperty("PieceLabel")
                                .toString());

                        listDocTypes.add(item);
                    } catch (Exception e) {
                        exception = true;

                    }
                }
            }

            return listDocTypes;
        }

        @Override
        protected void onPostExecute(ArrayList<ItemSpinner> listDocTypes) {
            if (customProgress1 != null) {
                customProgress1.hide();
            }
            androidHttpTransport.reset();
            //  DisplaySpinner(spDocTypes, docTypes, listDocTypes);
            if (listDocTypes.size() == 0) {
                if (exception) {
                    Log.i("Asyn", "DOC");
                    ShowSnakeBarInfo(5);
                } else
                    ShowSnakeBarInfo(0);
            } else {
                //Log.i("ALL_IDENTIY", listDocTypes.toString());

            }
            super.onPostExecute(listDocTypes);
        }
    }


    private String FormatDate(String s) {

        // s=yyyymmdd
        return s.substring(4, 6) + "/" + s.substring(6, 8) + "/"
                + s.substring(0, 4);
        // return MM/dd/yyyy
    }


    /******************************************************************/
    /******************************************************************/
    /******************************************************************/

    private String TAG = SplashScreen.class.getSimpleName();
    private ListView lv;
    ArrayList<HashMap<String, String>> contactList;


    /*********************************************
     * GET PAYS
     *********************************************/

    public class AsyncGetAllPaysAPI extends
            AsyncTask<String, String, ArrayList<ItemSpinner>> {


        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            // //Log.i("onCancelled", "AsyncGetAllPays");
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            // //Log.i("AsyncGetAllPays", "onPreExecute");
            customProgress1.setMessage(getResources().getText(
                    R.string.msg_loading_resa));

            customProgress1.show();

            super.onPreExecute();
        }

        protected ArrayList<ItemSpinner> doInBackground(String... params) {


            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = getURLAPI() + "GetAllPays";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONArray contacts = new JSONArray(jsonStr);
                    ItemSpinner item;
                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String id = c.getString("id");
                        String name = c.getString("name");
                        String code = c.getString("code");


                        item = new ItemSpinner();
                        item.setId(Integer.parseInt(id));
                        item.setLabel(name.toString());
                        item.setTel(code.toString());

                        listPays.add(item);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                        customProgress1.hide();
                        exception = true;
                        ShowSnakeBarInfo(5);
                    }
                });
            }

            return listPays;
        }

        @Override
        protected void onPostExecute(ArrayList<ItemSpinner> listPays) {

            if (listPays.size() == 0) {
                if (exception) {
                    Log.i("PAYS", "onPostExecute");
                    ShowSnakeBarInfo(5);
                } else
                    ShowSnakeBarInfo(0);
            } else {

                wsPIApi.execute();

            }

            super.onPostExecute(listPays);
        }
    }


    /**********************************************
     * GET Doc Identity
     **********************************************/

    public class AsyncGetAllPiecesIdenditeAPI extends
            AsyncTask<String, String, ArrayList<ItemSpinner>> {


        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            // //Log.i("onCancelled", "AsyncGetAllPays");
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            // //Log.i("AsyncGetAllPays", "onPreExecute");
            super.onPreExecute();
        }

        protected ArrayList<ItemSpinner> doInBackground(String... params) {


            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = getURLAPI() + "GetAllPiecesIdendite";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {

                    // Getting JSON Array node
                    JSONArray contacts = new JSONArray(jsonStr);
                    ItemSpinner item;
                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String id = c.getString("id");
                        String name = c.getString("name");


                        item = new ItemSpinner();
                        item.setId(Integer.parseInt(id));
                        item.setLabel(name.toString());


                        listDocTypes.add(item);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                        customProgress1.hide();
                        exception = true;
                        ShowSnakeBarInfo(5);
                    }
                });
            }

            return listDocTypes;
        }

        @Override
        protected void onPostExecute(ArrayList<ItemSpinner> listPays) {
            if (customProgress1 != null) {
                customProgress1.hide();
            }

            //  DisplaySpinner(spDocTypes, docTypes, listDocTypes);
            if (listDocTypes.size() == 0) {
                if (exception) {
                    Log.i("Asyn", "DOC");
                    ShowSnakeBarInfo(5);
                } else
                    ShowSnakeBarInfo(0);
            } else {
                //Log.i("ALL_IDENTIY", listDocTypes.toString());

            }
            super.onPostExecute(listDocTypes);
        }
    }


    /*****************************************************************/

    /*
         NEW API CALLS
     */


    private class HttpRequestTaskPays extends AsyncTask<Void, Void, ResponsePieceIdentite> {
        ResponsePieceIdentite response = null;


        @Override
        protected ResponsePieceIdentite doInBackground(Void... params) {
            try {
                final String url = getURLAPI() + "GetAllPays";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                response = restTemplate.getForObject(url, ResponsePieceIdentite.class);
                Log.i(TAG, response.toString());
                Log.i(TAG, String.valueOf(response.isStatus()));
                Log.i(TAG, String.valueOf(response.getData().size()));
                return response;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(ResponsePieceIdentite greeting) {
            if (response.isStatus() && (response.getData().size() != 0)) {

                // put data into listPays
                listPays = new ArrayList<>();
                List<PieceIdentite> dataPays = response.getData();
                for (PieceIdentite _pays :
                        dataPays) {
                    ItemSpinner item = new ItemSpinner();
                    item.setId(_pays.getId());
                    item.setLabel(_pays.getName());
                    item.setTel(_pays.getCode());
                    listPays.add(item);
                }


            } else {
                ShowSnakeBarInfo(5);
            }
            customProgress1.hide();
            wsPIApi.execute();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customProgress1.setMessage(getResources().getText(
                    R.string.msg_loading_resa));

            customProgress1.show();
            Log.i(TAG, "GET Pays");
        }
    }

    private class HttpRequestTaskIdentite extends AsyncTask<Void, Void, ResponsePieceIdentite> {
        ResponsePieceIdentite response = null;


        @Override
        protected ResponsePieceIdentite doInBackground(Void... params) {
            try {
                final String url = getURLAPI() + "GetAllPiecesIdendite";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                response = restTemplate.getForObject(url, ResponsePieceIdentite.class);
                Log.i(TAG, response.toString());
                Log.i(TAG, String.valueOf(response.isStatus()));
                Log.i(TAG, String.valueOf(response.getData().size()));
                return response;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);

            }

            return null;
        }

        @Override
        protected void onPostExecute(ResponsePieceIdentite greeting) {
            if (response.isStatus() && (response.getData().size() != 0)) {
                // put data into listDocTypes
                listDocTypes = new ArrayList<>();
                List<PieceIdentite> dataId = response.getData();
                for (PieceIdentite _pays :
                        dataId) {
                    ItemSpinner item = new ItemSpinner();
                    item.setId(_pays.getId());
                    item.setLabel(_pays.getName());
                    item.setTel(_pays.getCode());
                    listDocTypes.add(item);
                }
            } else {
                ShowSnakeBarInfo(5);
            }
            customProgress1.hide();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customProgress1.setMessage(getResources().getText(
                    R.string.msg_loading_resa));

            customProgress1.show();
            Log.i(TAG, "GET Pieces Identite");
        }
    }

    private class HttpRequestTaskReservation extends AsyncTask<Void, Void, ResponseResa> {
        ResponseResa response = null;
        String ResaRef = "";

        @Override
        protected ResponseResa doInBackground(Void... params) {
            try {
                final String url = getURLAPI() + "GetReservation?resaRef=" + ResaRef;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                response = restTemplate.getForObject(url, ResponseResa.class);
                Log.i(TAG, response.toString());
                Log.i(TAG, String.valueOf(response.isStatus()));
                Log.i(TAG, String.valueOf(response.getData().size()));
                return response;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);

            }

            return null;
        }

        @Override
        protected void onPostExecute(ResponseResa greeting) {
            if (response.isStatus() && (response.getData().size() != 0)) {
                // put data into curClients
                curClients = new ArrayList<Client>();
                for (Resa r :
                        response.getData()
                        ) {
                    Client curClient = new Client();


                    curClient.setCity(r.getCity());
                    curClient.setCodePostal(r.getCodePostal());

                    curClient.setClientId(r.getId());
                    curClient.setCivilite(Integer.parseInt(r.getCivilite()));
                    curClient.setClientNom(r.getNom());
                    curClient.setClientPrenom(r.getPrenom());
                    curClient.setPays(r.getPaysId());
                    curClient.setAdresse(r.getAddresse());
                    curClient.setDateNaiss(r.getDateNaissance());
                    curClient.setLieuNaiss(r.getLieu());
                    curClient.setSexe(r.getSexe());
                    curClient.setSitFam(r.getSituation());
                    curClient.setHandicape(r.getHandicape());
                    curClient.setNatureDocIdentite(r.getPieceId());
                    curClient.setNumDocIdentite(r.getDocNum());
                    curClient.setGsm(r.getGsm());
                    curClient.setProfession(r.getProfession());
                    curClient.setCity(r.getCity());
                    curClient.setFumeur(r.getFumeur());
                    curClients.add(curClient);
                }
                Intent intent = new Intent(SplashScreen.this, ClientsActivity.class);
                intent.putExtra("NBR_CLIENTS", curClients.size());
                intent.putExtra("RESA_REF", refResa);
                intent.putExtra("CLIENTS", curClients);
                intent.putExtra("PAYS", listPays);
                intent.putExtra("DOCS", listDocTypes);
                startActivity(intent);
                for (Client cli : curClients) {
                    Log.i("Splash Activity Client", cli.toString());
                }
            } else {
                if(!response.isStatus())
                {
                    ShowSnakeBarInfo(2);
                }
                else {
                    ShowSnakeBarInfo(1);
                }

            }
            customProgress1.hide();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customProgress1.setMessage(getResources().getText(
                    R.string.msg_loading_resa));
            ResaRef = refResa;
            customProgress1.show();
            Log.i(TAG, "GET Reservation");
        }
    }

    /**************************************************************/
    public String getURLAPI() {
        String URL = null;
        try {
            SharedPreferences sp = PreferenceManager
                    .getDefaultSharedPreferences(this);
            URL = sp.getString("SERVEUR", "192.168.0.100");

            URL = "http://" + URL + "/HNGAPI/api/apiPreCheckIn/";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return URL;
    }


}
