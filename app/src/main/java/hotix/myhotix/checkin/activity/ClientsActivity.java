package hotix.myhotix.checkin.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import hotix.myhotix.checkin.R;
import hotix.myhotix.checkin.fragments.OneFragment;
import hotix.myhotix.checkin.utilities.HttpHandler;

public class ClientsActivity extends AppCompatActivity {

    ArrayList<Client> curClients;
    ArrayList<ItemSpinner> listPays;
    ArrayList<ItemSpinner> listDocTypes;
    AlertDialog.Builder alertDialogBuilder;
    ProgressDialog customProgress1;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int NbrClient;
    private String refResa;
    ArrayList<Client> clientsUpdate;
    public final String NAMESPACE = "http://tempuri.org/";
    public final String SOAP_ACTION_UPDATE_RESA = "http://tempuri.org/UpdateReservationInfos";
    public final String METHOD_NAME_UPDATE_RESA = "UpdateReservationInfos";
    Client newClient;
    AsyncUpdateReservationAPI wsUpdateAPI;
    int nbrClientToUpdate = 0;
    int nbrClientUpdates = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view_icon_text_tabs);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coo);

        curClients = new ArrayList<Client>();
        NbrClient = getIntent().getIntExtra("NBR_CLIENTS", 1);
        refResa = getIntent().getStringExtra("RESA_REF");
        curClients = getIntent().getParcelableArrayListExtra("CLIENTS");
        for (Client cli : curClients) {
            Log.i("Clients Activity Client", cli.toString());
        }
        listDocTypes = getIntent().getParcelableArrayListExtra("DOCS");
        listPays = getIntent().getParcelableArrayListExtra("PAYS");
        //Log.i("GET CLIENTS ACTIVITY", curClients.toString());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(getResources().getText(R.string.str_resa_ref) + ": " + refResa);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();


    }

    private String FormatDate(String s) {

        return s.substring(6, 8) + "/" + s.substring(4, 6) + "/"
                + s.substring(0, 4);
    }

    /**
     * Adding custom view to tab
     */
    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.ic_tab_favourite,
                R.drawable.ic_check_circle_white_24dp,
                R.drawable.ic_person_pin_white_48dp
        };
        for (int i = 0; i < NbrClient; i++) {
            // TextView tab = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            // tab.setText("Client " + String.valueOf(i + 1));
            //  tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_contacts, 0, 0);
            // tabLayout.getTabAt(i).setCustomView(tab);
            tabLayout.getTabAt(i).setIcon(tabIcons[2]);
        }
    }

    /**
     * Adding fragments to ViewPager
     *
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        for (int i = 0; i < NbrClient; i++) {
            adapter.addFrag(new OneFragment().newInstance(curClients.get(i), refResa, listPays, listDocTypes, i + 1), "ONE");
            Log.i("strList", "ADD FRAGMENT");

        }
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_settings was selected
            case R.id.action_settings:

                Intent i = new Intent(ClientsActivity.this, IPServeur.class);
                startActivity(i);
                break;
            case R.id.action_save:
                ////Log.i("Save", "Save");
                clientsUpdate = new ArrayList<Client>();


                alertDialogBuilder = new AlertDialog.Builder(ClientsActivity.this);
                alertDialogBuilder.setTitle(getResources().getText(
                        R.string.str_resa_ref) + " " + refResa);
                alertDialogBuilder.setMessage(getResources().getText(
                        R.string.ask_update));
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton(getResources().getText(
                        R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SharedPreferences sp = PreferenceManager
                                        .getDefaultSharedPreferences(getApplicationContext());
                                if (!sp.getBoolean("SIGNATURE", false)) {

                                    clientsUpdate = new ArrayList<Client>();
                                    nbrClientUpdates = 0;
                                    nbrClientToUpdate = 0;
                                    ArrayList<Fragment> listFrag = getAllFragments();
                                    // //Log.i("FRAGMENTS", String.valueOf(listFrag.size()));
                                    for (Fragment frg : listFrag) {
                                        OneFragment frgFicheClient = (OneFragment) frg;
                                        Client cli = frgFicheClient.getDataClient();
                                        if (cli != null)
                                            clientsUpdate.add(cli);
                                        //Log.i("clientsUpdate", clientsUpdate.toString());
                                    }

                                    customProgress1 = new ProgressDialog(ClientsActivity.this);
                                    if (listFrag.size() == clientsUpdate.size()) {
                                        nbrClientToUpdate = clientsUpdate.size();

                                        //Log.i("UPDATE ", "TRUE");
                                        int i = 0;
                                        for (Client cli : clientsUpdate) {
                                            newClient = cli;
                                            // //Log.i("ZIED", "UPDATE CLIENT :" + String.valueOf(i + 1));

                                            customProgress1.setMessage(getResources().getText(
                                                    R.string.msg_saving_resa));
                                            //customProgress1.show();
                                            if (isOnline()) {

                                                wsUpdateAPI = new AsyncUpdateReservationAPI(cli);
                                                wsUpdateAPI.execute();
                                            }

                                        }
                                    } else {
                                        //Log.i("UPDATE ", "FALSE");
                                    }
                                } else {
                                    Intent intent = new Intent(ClientsActivity.this, CaptureSignature.class);
                                    startActivityForResult(intent, SIGNATURE_ACTIVITY);
                                }


                            }
                        });
                alertDialogBuilder.setNegativeButton(getResources().getText(
                        R.string.no), null);

                getWindow().setGravity(Gravity.BOTTOM);
                getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                alertDialogBuilder.show();

                break;
            case R.id.help:
                Intent i2 = new Intent(ClientsActivity.this, AboutActivity.class);
                startActivity(i2);

                break;
            case R.id.version:

                android.support.v7.app.AlertDialog.Builder alertDialogBuilder2 = new android.support.v7.app.AlertDialog.Builder(this);

                PackageManager manager = this.getPackageManager();
                alertDialogBuilder2.setTitle(getResources().getText(
                        R.string.app_name));
                alertDialogBuilder2.setIcon(R.drawable.ic_launcher);
                PackageInfo info;
                try {
                    info = manager.getPackageInfo(this.getPackageName(), 0);
                    alertDialogBuilder2.setMessage(getResources().getText(
                            R.string.version) + " : " + info.versionName);

                } catch (PackageManager.NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                alertDialogBuilder2.setCancelable(false);
                alertDialogBuilder2.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //ResetFields();

                            }
                        });


                alertDialogBuilder2.show();

                break;
            default:
                finish();
                break;
        }

        return true;
    }

    public ArrayList<Fragment> getAllFragments() {
        ArrayList<Fragment> lista = new ArrayList<Fragment>();

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            try {
                fragment.getTag();
                lista.add(fragment);
            } catch (NullPointerException e) {

            }
        }

        return lista;

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // return mFragmentTitleList.get(position);
            return null;
        }
    }

    private CoordinatorLayout coordinatorLayout;

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean isconnected = netInfo != null && netInfo.isConnectedOrConnecting();
        if (isconnected) {


        } else {

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
        return isconnected;

    }

    public class AsyncUpdateReservationOLD extends
            AsyncTask<String, String, String> {

        HttpTransportSE androidHttpTransport;
        String result = "False";
        Client cliToUpdate;

        public AsyncUpdateReservationOLD(Client cliToUpdate) {
            this.cliToUpdate = cliToUpdate;
        }

        @Override
        protected void onPreExecute() {
            //  //Log.i("ASYNCK TASK", "onPreExecute");

            customProgress1.show();
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            //   //Log.i("ASYNCK TASK", "doInBackground");
            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME_UPDATE_RESA);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            //Log.i("Client to Update Now :", cliToUpdate.toString());
            PropertyInfo pi_clientId = new PropertyInfo();
            pi_clientId.setName("clientId");
            pi_clientId.setValue(cliToUpdate.getClientId());
            pi_clientId.setType(Integer.class);
            request.addProperty(pi_clientId);

            PropertyInfo pi_clientNom = new PropertyInfo();
            pi_clientNom.setName("NomClient");
            pi_clientNom.setValue(cliToUpdate.getClientNom());
            pi_clientNom.setType(String.class);
            request.addProperty(pi_clientNom);

            PropertyInfo pi_clientPrenom = new PropertyInfo();
            pi_clientPrenom.setName("PrenomClient");
            pi_clientPrenom.setValue(cliToUpdate.getClientPrenom());
            pi_clientPrenom.setType(String.class);
            request.addProperty(pi_clientPrenom);

            PropertyInfo pi_paysId = new PropertyInfo();
            pi_paysId.setName("PaysId");
            pi_paysId.setValue(cliToUpdate.getPays());
            pi_paysId.setType(Integer.class);
            request.addProperty(pi_paysId);

            PropertyInfo pi_adresse = new PropertyInfo();
            pi_adresse.setName("clientAdresse");
            pi_adresse.setValue(cliToUpdate.getAdresse());
            pi_adresse.setType(String.class);
            request.addProperty(pi_adresse);

            PropertyInfo pi_dateNaiss = new PropertyInfo();
            pi_dateNaiss.setName("DateNaiss");
            pi_dateNaiss.setValue(cliToUpdate.getDateNaiss());
            //   //Log.i("Date Naissance", newClient.getDateNaiss());
            pi_dateNaiss.setType(String.class);
            request.addProperty(pi_dateNaiss);

            PropertyInfo pi_lieuNaiss = new PropertyInfo();
            pi_lieuNaiss.setName("LieuNaiss");
            pi_lieuNaiss.setValue(cliToUpdate.getLieuNaiss());
            pi_lieuNaiss.setType(String.class);
            request.addProperty(pi_lieuNaiss);

            PropertyInfo pi_sexe = new PropertyInfo();
            pi_sexe.setName("Sexe");
            pi_sexe.setValue(cliToUpdate.getSexe());
            pi_sexe.setType(String.class);
            request.addProperty(pi_sexe);

            PropertyInfo pi_sitFam = new PropertyInfo();
            pi_sitFam.setName("SitFam");
            pi_sitFam.setValue(cliToUpdate.getSitFam());
            pi_sitFam.setType(String.class);
            request.addProperty(pi_sitFam);

            PropertyInfo pi_fumeur = new PropertyInfo();
            pi_fumeur.setName("Fumeur");
            pi_fumeur.setValue(cliToUpdate.getFumeur());
            pi_fumeur.setType(Integer.class);
            request.addProperty(pi_fumeur);

            PropertyInfo pi_handicape = new PropertyInfo();
            pi_handicape.setName("Handicape");
            pi_handicape.setValue(cliToUpdate.getHandicape());
            pi_handicape.setType(Integer.class);
            request.addProperty(pi_handicape);

            PropertyInfo pi_docTypeId = new PropertyInfo();
            pi_docTypeId.setName("DocTypeId");
            pi_docTypeId.setValue(cliToUpdate.getNatureDocIdentite());
            pi_docTypeId.setType(Integer.class);
            request.addProperty(pi_docTypeId);

            PropertyInfo pi_docNum = new PropertyInfo();
            pi_docNum.setName("DocIdNum");
            pi_docNum.setValue(cliToUpdate.getNumDocIdentite());
            pi_docNum.setType(String.class);
            request.addProperty(pi_docNum);

            PropertyInfo pi_email = new PropertyInfo();
            pi_email.setName("Email");
            pi_email.setValue(cliToUpdate.getEmail());
            pi_email.setType(String.class);
            request.addProperty(pi_email);

            PropertyInfo pi_gsm = new PropertyInfo();
            pi_gsm.setName("Gsm");
            pi_gsm.setValue(cliToUpdate.getGsm());
            pi_gsm.setType(String.class);
            request.addProperty(pi_gsm);

            PropertyInfo pi_profession = new PropertyInfo();
            pi_profession.setName("Profession");
            pi_profession.setValue(cliToUpdate.getProfession());
            pi_profession.setType(String.class);
            request.addProperty(pi_profession);

            PropertyInfo pi_city = new PropertyInfo();
            pi_city.setName("City");
            pi_city.setValue(cliToUpdate.getCity());
            pi_city.setType(String.class);
            request.addProperty(pi_city);

            PropertyInfo pi_postal = new PropertyInfo();
            pi_postal.setName("CodePostal");
            pi_postal.setValue(cliToUpdate.getCodePostal());
            pi_postal.setType(String.class);
            request.addProperty(pi_postal);

            PropertyInfo pi_civi = new PropertyInfo();
            pi_civi.setName("Civilite");
            pi_civi.setValue(cliToUpdate.getCivilite());
            pi_civi.setType(Integer.class);
            request.addProperty(pi_civi);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            androidHttpTransport = new HttpTransportSE(getURL());

            try {
                try {
                    androidHttpTransport
                            .call(SOAP_ACTION_UPDATE_RESA, envelope);
                    result = envelope.getResponse().toString();
                } catch (SocketTimeoutException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            customProgress1.hide();
                            ShowErrorConnectionDialog();
                            androidHttpTransport.reset();
                        }
                    });

                }
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        nbrClientUpdates++;
                        if (nbrClientToUpdate == nbrClientUpdates) {
                            customProgress1.hide();
                            ShowSuccesUpdateDialog(Boolean.parseBoolean(result));


                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        customProgress1.hide();
                        ShowErrorConnectionDialog();
                        androidHttpTransport.reset();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            //  nbrClientUpdates++;
            customProgress1.hide();
            androidHttpTransport.reset();
            // //Log.i("Finished", "onPostExecute " + String.valueOf(nbrClientUpdates));
            super.onPostExecute(result);
        }

    }

    public String getURL() {
        String URL = null;
        try {
            SharedPreferences sp = PreferenceManager
                    .getDefaultSharedPreferences(this);
            URL = sp.getString("SERVEUR", "");
            // //Log.i("URL", URL);
            URL = "http://" + URL + "/hngwebsetup/webservice/myhotix.asmx";
            //Log.i("URL", URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return URL;
    }

    private void ShowSuccesUpdateDialog(Boolean b) {

        customProgress1.hide();

        alertDialogBuilder = new AlertDialog.Builder(this);

        if (b) {
            alertDialogBuilder.setMessage(getResources().getText(
                    R.string.msg_succes_update));
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //ResetFields();
                            finish();
                        }
                    });
        } else {
            alertDialogBuilder.setMessage(getResources().getText(
                    R.string.msg_echec_update));
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
        }

        alertDialogBuilder.show();

    }

    private void ShowErrorConnectionDialog() {

        customProgress1.hide();
        //ResetFields();
        alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage(getResources().getText(
                R.string.msg_erreur_serveur));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // ResetFields();
                    }
                });

        alertDialogBuilder.show();

    }

    public static final int SIGNATURE_ACTIVITY = 1;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SIGNATURE_ACTIVITY:
                if (resultCode == RESULT_OK) {

                    Bundle bundle = data.getExtras();
                    String status = bundle.getString("status");
                    String uniqueId = bundle.getString("uniqueid");
                    if (status.equalsIgnoreCase("done")) {
                    /*    Toast toast = Toast.makeText(this, "Signature capture successful!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 105, 50);
                        toast.show();*/
                        clientsUpdate = new ArrayList<Client>();
                        nbrClientUpdates = 0;
                        nbrClientToUpdate = 0;
                        ArrayList<Fragment> listFrag = getAllFragments();
                        // //Log.i("FRAGMENTS", String.valueOf(listFrag.size()));
                        for (Fragment frg : listFrag) {
                            OneFragment frgFicheClient = (OneFragment) frg;
                            Client cli = frgFicheClient.getDataClient();
                            if (cli != null)
                                clientsUpdate.add(cli);
                            //Log.i("clientsUpdate", clientsUpdate.toString());
                        }

                        customProgress1 = new ProgressDialog(ClientsActivity.this);
                        if (listFrag.size() == clientsUpdate.size()) {
                            nbrClientToUpdate = clientsUpdate.size();

                            //Log.i("UPDATE ", "TRUE");
                            int i = 0;
                            for (Client cli : clientsUpdate) {
                                newClient = cli;
                                // //Log.i("ZIED", "UPDATE CLIENT :" + String.valueOf(i + 1));

                                customProgress1.setMessage(getResources().getText(
                                        R.string.msg_saving_resa));
                                //customProgress1.show();
                                if (isOnline()) {

                                    wsUpdateAPI = new AsyncUpdateReservationAPI(cli);
                                    wsUpdateAPI.execute();
                                }

                            }
                        } else {
                            //Log.i("UPDATE ", "FALSE");
                        }
                    }
                }
                break;
        }

    }


    private String TAG = ClientsActivity.class.getSimpleName();

    /**************************************************************************
     *
     *
     */

    public class AsyncUpdateReservationAPI extends
            AsyncTask<String, String, String> {


        String result = "False";
        Client cliToUpdate;

        public AsyncUpdateReservationAPI(Client cliToUpdate) {
            this.cliToUpdate = cliToUpdate;
        }

        @Override
        protected void onPreExecute() {
            //  //Log.i("ASYNCK TASK", "onPreExecute");

            customProgress1.show();
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = getURLAPI() + "UpdateReservationInfos";
            url += "?clientId=" + cliToUpdate.getClientId() +
                    ((cliToUpdate.getClientNom() != "") ? "&NomClient=" + cliToUpdate.getClientNom() : "") +
                    ((cliToUpdate.getClientPrenom() != "") ? "&PrenomClient=" + cliToUpdate.getClientPrenom() : "") +
                    "&PaysId=" + cliToUpdate.getPays() +
                    "&clientAdresse=" + cliToUpdate.getAdresse() +
                    "&DateNaiss=" + cliToUpdate.getDateNaiss() +
                    "&LieuNaiss=" + cliToUpdate.getLieuNaiss() +
                    "&Sexe=" + cliToUpdate.getSexe() +
                    "&SitFam=" + cliToUpdate.getSitFam() +
                    "&Fumeur=" + cliToUpdate.getFumeur() +
                    "&Handicape=" + cliToUpdate.getHandicape() +
                    "&DocTypeId=" + cliToUpdate.getNatureDocIdentite() +
                    "&DocIdNum=" + cliToUpdate.getNumDocIdentite() +
                    "&Email=" + cliToUpdate.getEmail() +
                    "&Gsm=" + cliToUpdate.getGsm() +
                    "&Profession=" + cliToUpdate.getProfession();
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, " url: " + url);

            Log.e(TAG, "Response from url: " + jsonStr);


            if (jsonStr != null) {

                result = jsonStr;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        customProgress1.hide();
                        ShowSuccesUpdateDialog(Boolean.parseBoolean(result));

                    }
                });

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                        customProgress1.hide();
                        ShowErrorConnectionDialog();
                    }
                });
            }

            return null;
/*
            try {
                try {
                    androidHttpTransport
                            .call(SOAP_ACTION_UPDATE_RESA, envelope);
                    result = envelope.getResponse().toString();
                } catch (SocketTimeoutException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            customProgress1.hide();
                            ShowErrorConnectionDialog();
                            androidHttpTransport.reset();
                        }
                    });

                }
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        nbrClientUpdates++;
                        if (nbrClientToUpdate == nbrClientUpdates) {
                            customProgress1.hide();
                            ShowSuccesUpdateDialog(Boolean.parseBoolean(result));


                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        customProgress1.hide();
                        ShowErrorConnectionDialog();
                        androidHttpTransport.reset();
                    }
                });
            }

            return null;*/
        }

        @Override
        protected void onPostExecute(String result) {

            //  nbrClientUpdates++;
            customProgress1.hide();
            super.onPostExecute(result);
        }

    }


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

