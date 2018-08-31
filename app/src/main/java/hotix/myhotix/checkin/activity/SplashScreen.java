package hotix.myhotix.checkin.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.TextView;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import hotix.myhotix.checkin.R;
import hotix.myhotix.checkin.entities.Pay;
import hotix.myhotix.checkin.entities.PiecesId;
import hotix.myhotix.checkin.entities.Resa;
import hotix.myhotix.checkin.entities.ResponsePieceIdentite;
import hotix.myhotix.checkin.entities.ResponseResa;
import hotix.myhotix.checkin.utilities.UpdateChecker;

public class SplashScreen extends AppCompatActivity {

    // ************ Paramètres Web Services ************

    ProgressDialog customProgress1;

    SharedPreferences pref;
    TextView version;
    EditText editSearch;
    String refResa;

    ArrayList<Client> curClients;
    ArrayList<ItemSpinner> listPays;
    ArrayList<String> pays;
    ArrayList<ItemSpinner> listDocTypes;
    ArrayList<String> docTypes;

    HttpRequestTaskPays wsPaysApi;
    //HttpRequestTaskIdentite wsPIApi;
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

        version.setVisibility(View.GONE);
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

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    if (event.getRawX() >= (editSearch.getRight() - editSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

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
            finish();
        }


        if (!serveur.equals("")) {
            serveur = serveur.split("/")[0];

            Bitmap myImage = getBitmapFromURL("http://" + serveur + "/Android/backgroundCheck.jpg");
            Log.i("BackGround",(myImage!=null)?"ok":"NO");
            //BitmapDrawable(obj) convert Bitmap object into drawable object.
            if(myImage!=null) {

                background.setImageBitmap(myImage);
            }
        }
        checker = new UpdateChecker(this, true);

    }

    public Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem registrar = menu.findItem(R.id.help);
        registrar.setVisible(false);
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
            serveur = serveur.split("/")[0];
            checker.checkForUpdateByVersionCode("http://" + serveur + "/Android/versionCheck.txt");

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
            serveur = serveur.split("/")[0];
            checker.downloadAndInstall("http://" + serveur + "/Android/appCheck.apk");
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

        super.onResume();
        wsPaysApi = new HttpRequestTaskPays();
        // wsPIApi = new HttpRequestTaskIdentite();
        pref = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        getDataPaysIden();
        String codeHotel = pref.getString("HOTEL", "");
        switch (codeHotel) {
            case "1866":
                // Badira
              /*  if (pref.getBoolean("ANIMATION", true)) {

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

                    background.setImageResource(R.drawable.reception);
                }*/
                break;
            case "1865":
                // Sultan
             /*   if (pref.getBoolean("ANIMATION", true)) {

                    AnimationDrawable animation = new AnimationDrawable();
                    animation.addFrame(getResources().getDrawable(R.drawable.sultan1), 5000);
                    animation.addFrame(getResources().getDrawable(R.drawable.sultan2), 5000);
                    animation.setOneShot(false);


                    background.setImageDrawable(animation);

                    // start the animation!
                    animation.start();
                } else {

                    //background.setImageResource(R.drawable.sultan1);
                }*/
                break;
            case "1701":
                background.setImageResource(R.drawable.royal);
                break;
            default:
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
        String serveur = (pref.getString("SERVEUR", ""));
        if (!serveur.equals("")) {
            serveur = serveur.split("/")[0];
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


    private void ShowSnakeBarInfo(int i) {

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
                //Log.i("Asynck Pays", wsPaysApi.getStatus().toString());
                wsPaysApi = null;
                wsPaysApi = new HttpRequestTaskPays();
                //  wsPIApi = new HttpRequestTaskIdentite();

                RetryGetData();
                //Log.i("Asynck Pays2", wsPaysApi.getStatus().toString());

                break;
        }
    }


    /******************************************************************/

    private String TAG = SplashScreen.class.getSimpleName();

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
                restTemplate.setErrorHandler(new MyErrorHandler());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                response = restTemplate.getForObject(url, ResponsePieceIdentite.class);
                //Log.i(TAG, "Pays :" + response.toString());
                Log.i(TAG, "Data :" + String.valueOf(response.isStatus()));

                return response;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(ResponsePieceIdentite greeting) {
            if (response != null) {
                if (response.isStatus()) {


                    listPays = new ArrayList<>();
                    listDocTypes = new ArrayList<>();

                    List<Pay> dataPays = response.getData().getPays();
                    for (Pay _pays :
                            dataPays) {
                        ItemSpinner item = new ItemSpinner();
                        item.setId(_pays.getId());
                        item.setLabel(_pays.getName());
                        item.setTel(_pays.getCode());
                        listPays.add(item);
                    }
                    List<PiecesId> dataId = response.getData().getPiecesId();
                    for (PiecesId _pays :
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
            //Log.i(TAG, "GET Pays");
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
                restTemplate.setErrorHandler(new MyErrorHandler());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                response = restTemplate.getForObject(url, ResponseResa.class);
                //Log.i(TAG, "Reservation :" + response.toString());
                Log.i(TAG, "Reservation :" + String.valueOf(response.isStatus()));
                Log.i(TAG, "Reservation :" + String.valueOf(response.getData().size()));
                return response;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);

            }

            return null;
        }

        @Override
        protected void onPostExecute(ResponseResa greeting) {
            if (response != null) {
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
                        curClient.setEmail(r.getEmail());
                        //Log.i("DebugZied", curClient.getDateNaiss());
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
               /*
                for (Client cli : curClients) {
                    Log.i("Splash Activity Client", cli.toString());
                }*/
                } else {
                    if (!response.isStatus()) {
                        ShowSnakeBarInfo(2);
                    } else {
                        ShowSnakeBarInfo(0);
                    }

                }
            } else {
                ShowSnakeBarInfo(0);
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
            //Log.i(TAG, "GET Reservation");
        }
    }

    public String getURLAPI() {
        String URL = null;
        try {
            SharedPreferences sp = PreferenceManager
                    .getDefaultSharedPreferences(this);
            URL = sp.getString("SERVEUR", "");
            String urlStr = "HNGAPI";
            boolean exist = URL.toLowerCase().matches(urlStr.toLowerCase());
            if (!exist)
                URL = URL + "/HNGAPI";
            URL = "http://" + URL + "/api/apiPreCheckIn/";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return URL;
    }

    public class MyErrorHandler implements ResponseErrorHandler {
        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            // your error handling here
            final String code = String.valueOf(response.getRawStatusCode());
            Log.i("ResponseErrorHandler", "handleError: " + String.valueOf(response.getRawStatusCode()));
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //ShowAlert(getResources().getString(R.string.msg_connecting_error_srv) + "\n(" + code + ")");
                }
            });
        }

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            boolean hasError = false;
            Log.i("ResponseErrorHandler", "hasError: " + String.valueOf(response.getRawStatusCode()));
            int rawStatusCode = response.getRawStatusCode();
            if (rawStatusCode != 200) {
                hasError = true;
            }
            return hasError;
        }
    }
}
