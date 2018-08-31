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

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hotix.myhotix.checkin.R;
import hotix.myhotix.checkin.fragments.OneFragment;

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
    Client newClient;
    HttpRequestTaskUpdateReservation wsUpdateAPI;
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
        int index = 1;
        for (Client cli : curClients) {
            Log.i("ClientActivity onCreate", "Client " + String.valueOf(index) + " :" + cli.toString());
            index++;
        }
        listDocTypes = getIntent().getParcelableArrayListExtra("DOCS");
        listPays = getIntent().getParcelableArrayListExtra("PAYS");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle(getResources().getText(R.string.str_resa_ref) + ": " + refResa);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
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
            //Log.i("strList", "ADD FRAGMENT");

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

                clientsUpdate = new ArrayList<Client>();

                Log.i("FRAGMENT", "onOptionsItemSelected");
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

                                    for (Fragment frg : listFrag) {
                                        OneFragment frgFicheClient = (OneFragment) frg;
                                        Client cli = frgFicheClient.getDataClient();
                                        if (cli != null)
                                            clientsUpdate.add(cli);

                                    }

                                    customProgress1 = new ProgressDialog(ClientsActivity.this);
                                    if (listFrag.size() == clientsUpdate.size()) {
                                        nbrClientToUpdate = clientsUpdate.size();


                                        customProgress1.setMessage(getResources().getText(
                                                R.string.msg_saving_resa));
                                        for (Client cli : clientsUpdate) {
                                            newClient = cli;


                                            //customProgress1.show();
                                            if (isOnline()) {

                                                wsUpdateAPI = new HttpRequestTaskUpdateReservation(cli);
                                                wsUpdateAPI.execute();
                                            }

                                        }
                                    } else {

                                    }
                                } else {
                                    Intent intent = new Intent(ClientsActivity.this, SignatureActivity.class);
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

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem registrar = menu.findItem(R.id.help);
        registrar.setVisible(false);
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
                    String image = bundle.getString("image");
                    if (status.equalsIgnoreCase("done")) {
                    /*    Toast toast = Toast.makeText(this, "Signature capture successful!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 105, 50);
                        toast.show();*/
                        clientsUpdate = new ArrayList<Client>();
                        nbrClientUpdates = 0;
                        nbrClientToUpdate = 0;
                        ArrayList<Fragment> listFrag = getAllFragments();

                        for (Fragment frg : listFrag) {
                            OneFragment frgFicheClient = (OneFragment) frg;
                            Client cli = frgFicheClient.getDataClient();
                            cli.setImage(image);
                            if (cli != null)
                                clientsUpdate.add(cli);

                        }

                        customProgress1 = new ProgressDialog(ClientsActivity.this);
                        if (listFrag.size() == clientsUpdate.size()) {
                            nbrClientToUpdate = clientsUpdate.size();

                            customProgress1.setMessage(getResources().getText(
                                    R.string.msg_saving_resa));

                            for (Client cli : clientsUpdate) {
                                newClient = cli;
                                // Log.i("Update zr", newClient.toString());

                                //customProgress1.show();
                                if (isOnline()) {

                                    wsUpdateAPI = new HttpRequestTaskUpdateReservation(cli);
                                    wsUpdateAPI.execute();
                                }

                            }
                        } else {

                        }
                    }
                }
                break;
        }

    }

    private String TAG = ClientsActivity.class.getSimpleName();

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


    private class HttpRequestTaskUpdateReservation extends AsyncTask<Void, Void, Boolean> {
        Boolean response = null;

        Client cliToUpdate;

        public HttpRequestTaskUpdateReservation(Client cliToUpdate) {
            this.cliToUpdate = cliToUpdate;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                // Start OLD
                String url = getURLAPI() + "UpdateReservationInfos";
                  /*  url += "?clientId=" + cliToUpdate.getClientId() +
                        ((cliToUpdate.getClientNom() != "") ? "&NomClient=" + cliToUpdate.getClientNom() : "") +
                        ((cliToUpdate.getClientPrenom() != "") ? "&PrenomClient=" + cliToUpdate.getClientPrenom() : "") +
                        "&PaysId=" + cliToUpdate.getPays() +
                        "&clientAdresse=" + cliToUpdate.getAdresse() +
                        "&DateNaiss=" + cliToUpdate.getDateNaiss().replace("/", "") +
                        "&LieuNaiss=" + cliToUpdate.getLieuNaiss() +
                        "&Sexe=" + cliToUpdate.getSexe() +
                        "&SitFam=" + cliToUpdate.getSitFam() +
                        "&Fumeur=" + cliToUpdate.getFumeur() +
                        "&Handicape=" + cliToUpdate.getHandicape() +
                        "&DocTypeId=" + cliToUpdate.getNatureDocIdentite() +
                        "&DocIdNum=" + cliToUpdate.getNumDocIdentite() +
                        "&Email=" + cliToUpdate.getEmail() +
                        "&Gsm=" + cliToUpdate.getGsm() +
                        "&Profession=" + cliToUpdate.getProfession() +
                        "&Image=" + ((cliToUpdate.getImage() == null) ? "" : cliToUpdate.getImage());
                RestTemplate restTemplate = new RestTemplate();
                Log.i(TAG, "Update Reservation :" + url.toString());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                response = restTemplate.getForObject(url, Boolean.class);
                Log.i(TAG, "Update Reservation :" + response.toString());

                return response;*/
                // End

                RestTemplate restTemplate = new RestTemplate();

                HttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
                HttpMessageConverter stringHttpMessageConverternew = new StringHttpMessageConverter();

                restTemplate.getMessageConverters().add(formHttpMessageConverter);
                restTemplate.getMessageConverters().add(stringHttpMessageConverternew);

                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("clientId", String.valueOf(cliToUpdate.getClientId()));
                map.add("NomClient", cliToUpdate.getClientNom());
                map.add("PrenomClient", cliToUpdate.getClientPrenom());
                map.add("PaysId", String.valueOf(cliToUpdate.getPays()));
                map.add("clientAdresse", cliToUpdate.getAdresse());
                map.add("DateNaiss", cliToUpdate.getDateNaiss().replace("/", ""));
                map.add("LieuNaiss", cliToUpdate.getLieuNaiss());
                map.add("Sexe", cliToUpdate.getSexe());
                map.add("SitFam", cliToUpdate.getSitFam());
                map.add("Fumeur", String.valueOf(cliToUpdate.getFumeur()));
                map.add("Handicape", String.valueOf(cliToUpdate.getHandicape()));
                map.add("DocTypeId", String.valueOf(cliToUpdate.getNatureDocIdentite()));
                map.add("DocIdNum", cliToUpdate.getNumDocIdentite());
                map.add("Email", cliToUpdate.getEmail());
                map.add("Gsm", cliToUpdate.getGsm());
                map.add("Profession", cliToUpdate.getProfession());
                map.add("Image", ((cliToUpdate.getImage() == null) ? null : cliToUpdate.getImage()));

                String responsepOST = restTemplate.postForObject(url, map, String.class);
                Log.i(TAG, "Update Reservation :" + responsepOST);
                response = Boolean.valueOf(responsepOST);
                return Boolean.valueOf(response);


            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);

            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean greeting) {
            nbrClientUpdates++;
            Log.i(TAG, "HERE 1 :" + greeting);
            if (nbrClientToUpdate == nbrClientUpdates) {
                customProgress1.hide();
                Log.i(TAG, "HERE 2 :" + greeting);
                if (response == null)
                    response = false;
                ShowSuccesUpdateDialog(response);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (nbrClientUpdates == 0) {
                customProgress1.show();
                //Log.i(TAG, "GET Reservation");
            }
        }
    }

    ProgressDialog pd;

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

