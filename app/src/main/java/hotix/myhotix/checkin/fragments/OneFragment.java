package hotix.myhotix.checkin.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import hotix.myhotix.checkin.R;
import hotix.myhotix.checkin.activity.Client;
import hotix.myhotix.checkin.activity.EmailValidator;
import hotix.myhotix.checkin.activity.ItemSpinner;
import hotix.myhotix.checkin.activity.PrefixedEditText;


public class OneFragment extends Fragment implements View.OnClickListener {


    EditText edtNom, edtPrenom, edtAdresse, edtDateNaiss,
            edtLieuNaiss, edtNumDoc, edtEmail, edtProfession, edtCity, edtCodePostal;
    PrefixedEditText edtGsm;
    // ************ Spinner *************
    Spinner spPays, spDocTypes, spCivilite;
    // ************ RadioButton *********
    RadioGroup rdGrpSexe, rdGrpSitFam, rdGrpFumeur, rdGrpHandicape;
    RadioButton rdMasculin, rdFeminin, rdCelib, rdMarie, rdDivorce,
            rdFumeurOui, rdFumeurNon, rdHandOui, rdHandNon;
    // ************ Button *********
    Button btn_update;
    Client curClient, newClient;
    String resaRef;
    ArrayList<ItemSpinner> listPays;
    ArrayList<String> pays;
    ArrayList<ItemSpinner> listDocTypes;
    ArrayList<String> docTypes;
    ArrayList<ItemSpinner> listCivi;
    ArrayList<String> civi;
    AlertDialog.Builder alertDialogBuilder;
    ProgressDialog customProgress1;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    LinearLayout layoutBirth, layoutFumeur, layoutHandicap, layoutNom, layoutPrenom, layoutaddress, layoutcity,
            layoutcodepostal, layoutgendr, layoutpays, layoutprofession, layoutemail, layoutdoc, layoutsitfam, layoutgsm;

    private SimpleDateFormat dateFormatter;
    ScrollView scroll;
    int Nbre = 1;


    public OneFragment() {
        // Required empty public constructor
    }

    public static OneFragment newInstance(Client curClient, String resaRef, ArrayList<ItemSpinner> listPays, ArrayList<ItemSpinner> listDocTypes, int Nbre) {
        OneFragment myFragment = new OneFragment();

        Bundle args = new Bundle();
        args.putString("RESA_REF", resaRef);
        args.putParcelable("CLIENT", curClient);
        args.putParcelableArrayList("PAYS", listPays);
        args.putParcelableArrayList("DOCS", listDocTypes);
        args.putInt("NBRE_CLIENT", Nbre);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //log.i("FRAGMENT", "onCreate");
        resaRef = getArguments().getString("RESA_REF");
        curClient = getArguments().getParcelable("CLIENT");
        Nbre = getArguments().getInt("NBRE_CLIENT");
        //log.i("Fragment Client", curClient.toString());
        listDocTypes = getArguments().getParcelableArrayList("DOCS");
        listPays = getArguments().getParcelableArrayList("PAYS");

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        //log.i("FRAGMENT", "onCreateView");
        scroll = (ScrollView) view.findViewById(R.id.scrollView1);
        customProgress1 = new ProgressDialog(getActivity());
        customProgress1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        edtNom = (EditText) view.findViewById(R.id.edtNom);
        edtPrenom = (EditText) view.findViewById(R.id.edtPrenom);
        edtAdresse = (EditText) view.findViewById(R.id.edtAdresse);

        edtCity = (EditText) view.findViewById(R.id.edtCity);
        edtCodePostal = (EditText) view.findViewById(R.id.edtCodePostal);

        edtDateNaiss = (EditText) view.findViewById(R.id.edtDateNaiss);
        edtLieuNaiss = (EditText) view.findViewById(R.id.edtLieuNaiss);
        edtDateNaiss.setInputType(InputType.TYPE_NULL);

        edtNumDoc = (EditText) view.findViewById(R.id.edtNumDoc);
        edtEmail = (EditText) view.findViewById(R.id.edtEmail);
        edtGsm = (PrefixedEditText) view.findViewById(R.id.edtGsm);
        edtProfession = (EditText) view.findViewById(R.id.edtProfession);

        spPays = (Spinner) view.findViewById(R.id.spPays);
        spDocTypes = (Spinner) view.findViewById(R.id.spTypeDoc);
        spCivilite = (Spinner) view.findViewById(R.id.spCivilite);

        spPays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String code = GetPhoneFromSpinner(spPays, pays, listPays) + " ";
                float sizeText = new Button(getActivity()).getTextSize();
//                edtGsm.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code, sizeText), null, null, null);
//                edtGsm.setCompoundDrawablePadding(code.length());
                edtGsm.setPrefix(code);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        rdGrpSexe = (RadioGroup) view.findViewById(R.id.rgSexe);
        rdGrpSitFam = (RadioGroup) view.findViewById(R.id.rgSitFam);
        rdGrpFumeur = (RadioGroup) view.findViewById(R.id.rgFumeur);
        rdGrpHandicape = (RadioGroup) view.findViewById(R.id.rgHand);

        rdMasculin = (RadioButton) view.findViewById(R.id.rdMasculin);
        rdFeminin = (RadioButton) view.findViewById(R.id.rdFeminin);

        rdCelib = (RadioButton) view.findViewById(R.id.rdCelib);
        rdMarie = (RadioButton) view.findViewById(R.id.rdMarie);
        rdDivorce = (RadioButton) view.findViewById(R.id.rdDivorce);

        rdFumeurOui = (RadioButton) view.findViewById(R.id.rdFumeurOui);
        rdFumeurNon = (RadioButton) view.findViewById(R.id.rdFumeurNon);
        rdHandOui = (RadioButton) view.findViewById(R.id.rdHanOui);
        rdHandNon = (RadioButton) view.findViewById(R.id.rdHanNon);

        btn_update = (Button) view.findViewById(R.id.btn_update);

        layoutBirth = (LinearLayout) view.findViewById(R.id.LayoutBirth);
        layoutFumeur = (LinearLayout) view.findViewById(R.id.LayoutFumeur);
        layoutHandicap = (LinearLayout) view.findViewById(R.id.LayoutHandicap);

        layoutPrenom = (LinearLayout) view.findViewById(R.id.LayoutPrenom);
        layoutaddress = (LinearLayout) view.findViewById(R.id.LayoutAdresse);
        layoutcity = (LinearLayout) view.findViewById(R.id.LayoutCity);
        layoutcodepostal = (LinearLayout) view.findViewById(R.id.LayoutCodePostal);
        layoutemail = (LinearLayout) view.findViewById(R.id.LayoutEmail);
        layoutdoc = (LinearLayout) view.findViewById(R.id.LayoutIdentite);
        layoutgendr = (LinearLayout) view.findViewById(R.id.LayoutGender);
        layoutgsm = (LinearLayout) view.findViewById(R.id.LayoutGsm);
        layoutNom = (LinearLayout) view.findViewById(R.id.LayoutNom);
        layoutpays = (LinearLayout) view.findViewById(R.id.LayoutPays);
        layoutprofession = (LinearLayout) view.findViewById(R.id.LayoutProfession);
        layoutsitfam = (LinearLayout) view.findViewById(R.id.LayoutFam);

        try {
            SharedPreferences sp = PreferenceManager
                    .getDefaultSharedPreferences(getActivity());
            if (!sp.getBoolean("BIRTH", true)) {
                layoutBirth.setVisibility(View.GONE);
            } else {
                layoutBirth.setVisibility(View.VISIBLE);
            }
            if (!sp.getBoolean("FUMEUR", true)) {
                layoutFumeur.setVisibility(View.GONE);
            } else {
                layoutFumeur.setVisibility(View.VISIBLE);
            }
            if (!sp.getBoolean("HANDICAP", true)) {
                layoutHandicap.setVisibility(View.GONE);
            } else {
                layoutHandicap.setVisibility(View.VISIBLE);
            }


            if (!sp.getBoolean("EMAIL", true)) {
                layoutemail.setVisibility(View.GONE);
            } else {
                layoutemail.setVisibility(View.VISIBLE);
            }

            if (!sp.getBoolean("PAYS", true)) {
                layoutpays.setVisibility(View.GONE);
            } else {
                layoutpays.setVisibility(View.VISIBLE);
            }

            if (!sp.getBoolean("CODEPOSTAL", true)) {
                layoutcodepostal.setVisibility(View.GONE);
            } else {
                layoutcodepostal.setVisibility(View.VISIBLE);
            }

            if (!sp.getBoolean("PROFESSION", true)) {
                layoutprofession.setVisibility(View.GONE);
            } else {
                layoutprofession.setVisibility(View.VISIBLE);
            }
            if (!sp.getBoolean("NOM", true)) {
                layoutNom.setVisibility(View.GONE);
            } else {
                layoutNom.setVisibility(View.VISIBLE);
            }
            if (!sp.getBoolean("PRENOM", true)) {
                layoutPrenom.setVisibility(View.GONE);
            } else {
                layoutPrenom.setVisibility(View.VISIBLE);
            }
            if (!sp.getBoolean("SITFAM", true)) {
                layoutsitfam.setVisibility(View.GONE);
            } else {
                layoutsitfam.setVisibility(View.VISIBLE);
            }
            if (!sp.getBoolean("GSM", true)) {
                layoutgsm.setVisibility(View.GONE);
            } else {
                layoutgsm.setVisibility(View.VISIBLE);
            }
            if (!sp.getBoolean("ADDRESS", true)) {
                layoutaddress.setVisibility(View.GONE);
            } else {
                layoutaddress.setVisibility(View.VISIBLE);
            }

            if (!sp.getBoolean("DOCIDEN", true)) {
                layoutdoc.setVisibility(View.GONE);
            } else {
                layoutdoc.setVisibility(View.VISIBLE);
            }
            if (!sp.getBoolean("CITY", true)) {
                layoutcity.setVisibility(View.GONE);
            } else {
                layoutcity.setVisibility(View.VISIBLE);
            }
            if (!sp.getBoolean("GENDER", true)) {
                layoutgendr.setVisibility(View.GONE);
            } else {
                layoutgendr.setVisibility(View.VISIBLE);
            }

            if (!sp.getBoolean("PAX", true)) {
                btn_update.setVisibility(View.GONE);
            } else {
                btn_update.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        //setDateTimeField(dateFormatter.format(new Date().getTime()));
        btn_update.setOnClickListener(this);
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                ////log.i("Here", "OnDateSetListener ");
                updateLabel();
            }

        };

        edtDateNaiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //log.i("FRAGMENT", "onActivityCreated " + curClient.toString());
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        //log.i("FRAGMENT", "onStart " + curClient.toString());
        super.onStart();
    }

    @Override
    public void onResume() {
        //log.i("FRAGMENT", "onResume " + curClient.toString());
        listCivi = new ArrayList<ItemSpinner>();
        ////log.i("RESA_REF_FRAGMENT", resaRef);
        ////log.i("CLIENT_FRAGMENT", curClient.toString());
        pays = new ArrayList<String>();
        docTypes = new ArrayList<String>();
        civi = new ArrayList<String>();
        listCivi.add(new ItemSpinner(1, getResources().getText(R.string.mr).toString()));
        listCivi.add(new ItemSpinner(2, getResources().getText(R.string.mme).toString()));
        listCivi.add(new ItemSpinner(3, getResources().getText(R.string.mlle).toString()));
        DisplaySpinner(spPays, pays, listPays);
        DisplaySpinner(spDocTypes, docTypes, listDocTypes);
        DisplaySpinner(spCivilite, civi, listCivi);

        setDataClient();
        getDataClient();
        super.onResume();
    }

    @Override
    public void onPause() {
        //log.i("FRAGMENT", "onPause " + curClient.toString());
        super.onPause();
    }

    @Override
    public void onStop() {
        //log.i("FRAGMENT", "onStop " + curClient.toString());
        getDataClient();
        if (wsUpdate != null)
            wsUpdate.cancel(true);
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        //log.i("FRAGMENT", "onAttach");
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        //log.i("FRAGMENT", "onDetach " + curClient.toString());
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        //log.i("FRAGMENT", "onDestroyView  " + curClient.toString());
        super.onDestroyView();
    }

    private void updateLabel() {

        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        edtDateNaiss.setText(sdf.format(myCalendar.getTime()));
        //log.i("Here", "Update Date Picker 1");
    }

    private void DisplaySpinner(Spinner sp, ArrayList<String> strList,
                                ArrayList<ItemSpinner> listItems) {

        for (int i = 0; i < listItems.size(); i++) {
            strList.add(listItems.get(i).getLabel());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, strList);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(dataAdapter);
    }

    private int GetIdFromSpinner(Spinner sp, ArrayList<String> strList,
                                 ArrayList<ItemSpinner> liste) {
        int val = 0;
        ////log.i("strList", strList.toString());
        ////log.i("strList", liste.toString());
        for (int i = 0; i < strList.size(); i++) {
            if (liste.get(i).getLabel().equals(sp.getSelectedItem().toString())) {
                val = liste.get(i).getId();
            }
        }
        // ////log.i("Id", String.valueOf(val));
        return val;
    }

    private String GetPhoneFromSpinner(Spinner sp, ArrayList<String> strList,
                                       ArrayList<ItemSpinner> liste) {
        String val = "";
        // ////log.i("strList", strList.toString());
        // ////log.i("strList", liste.toString());
        for (int i = 0; i < strList.size(); i++) {
            if (liste.get(i).getLabel().equals(sp.getSelectedItem().toString())) {
                val = liste.get(i).getTel();
            }
        }
        // ////log.i("Id", String.valueOf(val));
        return val;
    }

    private int GetPosFromSpinner(ArrayList<ItemSpinner> ListePays, int IdPays) {
        int val = 0;
        for (int i = 0; i < ListePays.size(); i++) {
            if (ListePays.get(i).getId() == IdPays) {
                val = i;

                return val;
            }
        }

        return val;
    }


    public Client getDataClient() {
        ////log.i("Get Data from Fragment", curClient.toString());
        if (curClient.getClientId() != 0) {

            // Construction de l'objet client

            curClient.setClientId(curClient.getClientId());

            curClient.setCivilite(GetIdFromSpinner(spCivilite, civi, listCivi));

            curClient.setClientNom(edtNom.getText().toString());

            curClient.setClientPrenom(edtPrenom.getText().toString());

            curClient.setPays(GetIdFromSpinner(spPays, pays, listPays));

            curClient.setAdresse(edtAdresse.getText().toString());
            curClient.setCity(edtCity.getText().toString());
            curClient.setCodePostal(edtCodePostal.getText().toString());
            curClient.setLieuNaiss(edtLieuNaiss.getText().toString());

            if (rdGrpSexe.getCheckedRadioButtonId() == R.id.rdMasculin)
                curClient.setSexe("M");
            else
                curClient.setSexe("F");

            switch (rdGrpSitFam.getCheckedRadioButtonId()) {
                case R.id.rdCelib:
                    curClient.setSitFam("C");
                    break;
                case R.id.rdMarie:
                    curClient.setSitFam("M");
                    break;
                case R.id.rdDivorce:
                    curClient.setSitFam("D");
                    break;
            }

            switch (rdGrpHandicape.getCheckedRadioButtonId()) {
                case R.id.rdHanNon:
                    curClient.setHandicape(0);
                    break;
                case R.id.rdHanOui:
                    curClient.setHandicape(1);
                    break;

            }
            switch (rdGrpFumeur.getCheckedRadioButtonId()) {
                case R.id.rdFumeurNon:
                    curClient.setFumeur(0);
                    break;
                case R.id.rdFumeurOui:
                    curClient.setFumeur(1);
                    break;

            }

            curClient.setNatureDocIdentite(GetIdFromSpinner(spDocTypes,
                    docTypes, listDocTypes));

            curClient.setNumDocIdentite(edtNumDoc.getText().toString());

            curClient.setGsm(edtGsm.getText().toString());

            curClient.setProfession(edtProfession.getText().toString());

            curClient.setEmail(edtEmail.getText().toString()
                    .trim());

            curClient.setDateNaiss(edtDateNaiss.getText().toString());
            ////log.i("Here","curClient.getDateNaiss() ="+ curClient.getDateNaiss().toString());
            EmailValidator emVal = new EmailValidator();
/*
            if ((layoutemail.getVisibility() == View.VISIBLE)
                    && (edtEmail.getText().toString().trim().equals(""))
                    && (!emVal.validate(edtEmail.getText().toString().trim())))
            {
                edtEmail.setError(getResources().getText(
                        R.string.champs_requis));
                Snackbar snackbar = Snackbar
                        .make(scroll, getResources().getText(
                                R.string.email_requis), Snackbar.LENGTH_LONG);
                ViewGroup group = (ViewGroup) snackbar.getView();
                group.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
                snackbar.show();
                return null;

            }
            if ((layoutdoc.getVisibility() == View.VISIBLE) && (edtNumDoc.getText().toString().trim().equals(""))) {
                edtNumDoc.setError(getResources().getText(
                        R.string.champs_requis));
                Snackbar snackbar = Snackbar
                        .make(scroll, getResources().getText(
                                R.string.numid_requis), Snackbar.LENGTH_LONG);
                ViewGroup group = (ViewGroup) snackbar.getView();
                group.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
                snackbar.show();
                return null;
            }*/
            return curClient;
        } else return null;
    }


    private String FormatDateyyyyMMdd(String s) {


        // s= MM/dd/yyyy
        String ss = s.substring(6, 10) + s.substring(0, 2) + s.substring(3, 5);
        ////log.i("CONVERSION DATE :", " Date initial: " + s + " , Date final: " + ss);
        return ss;
        // return yyyyMMdd
    }

    private void setDataClient() {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        ////log.i("SET DATA ", String.valueOf(Nbre) + ", SHOW CLIENTS =" + sp.getBoolean("SHOWCLIENTS", false));

        if (!sp.getBoolean("SHOWCLIENTS", false)) {
            if (Nbre == 1) {
                edtNom.setText(curClient.getClientNom());

                edtPrenom.setText(curClient.getClientPrenom());
                edtAdresse.setText(curClient.getAdresse());
                edtEmail.setText(curClient.getEmail());
                edtGsm.setText(curClient.getGsm());
                edtAdresse.setText(curClient.getAdresse());
                edtNumDoc.setText(curClient.getNumDocIdentite());
                edtProfession.setText(curClient.getProfession());
                edtCity.setText(curClient.getCity());
                edtCodePostal.setText(curClient.getCodePostal());
                edtLieuNaiss.setText(curClient.getLieuNaiss());
            }
        } else {
            edtNom.setText(curClient.getClientNom());

            edtPrenom.setText(curClient.getClientPrenom());
            edtAdresse.setText(curClient.getAdresse());
            edtEmail.setText(curClient.getEmail());
            edtGsm.setText(curClient.getGsm());
            edtAdresse.setText(curClient.getAdresse());
            edtNumDoc.setText(curClient.getNumDocIdentite());
            edtProfession.setText(curClient.getProfession());
            edtCity.setText(curClient.getCity());
            edtCodePostal.setText(curClient.getCodePostal());
            edtLieuNaiss.setText(curClient.getLieuNaiss());
        }
        switch (curClient.getSexe()) {
            case "F":
                rdMasculin.setChecked(false);
                rdFeminin.setChecked(true);
                break;
            case "M":
                rdMasculin.setChecked(true);
                rdFeminin.setChecked(false);

                break;
        }
        switch (curClient.getSitFam()) {
            case "C":
                rdCelib.setChecked(true);
                rdMarie.setChecked(false);
                rdDivorce.setChecked(false);
                break;
            case "M":
                rdCelib.setChecked(false);
                rdMarie.setChecked(true);
                rdDivorce.setChecked(false);
                break;
            case "D":
                rdCelib.setChecked(false);
                rdMarie.setChecked(false);
                rdDivorce.setChecked(true);
                break;

        }
        if (curClient.getFumeur() == 1) {
            rdFumeurOui.setChecked(true);
            rdFumeurNon.setChecked(false);
        } else {
            rdFumeurNon.setChecked(true);
            rdFumeurOui.setChecked(false);
        }

        if (curClient.getHandicape() == 1) {
            rdHandOui.setChecked(true);
            rdHandNon.setChecked(false);
        } else {
            rdHandNon.setChecked(true);
            rdHandOui.setChecked(false);
        }
        if (listPays.size() > 0)
            spPays.setSelection(GetPosFromSpinner(listPays,
                    curClient.getPays()));

        if (listDocTypes.size() > 0)
            spDocTypes.setSelection(GetPosFromSpinner(listDocTypes,
                    curClient.getNatureDocIdentite()));
        if (listCivi.size() > 0)
            spCivilite.setSelection(GetPosFromSpinner(listCivi,
                    curClient.getCivilite()));
        //  setDateTimeField(curClient.getDateNaiss());
        try {
            myCalendar.setTime(dateFormatter.parse(curClient.getDateNaiss()));

            updateLabel();
            ////log.i("Here", "edtDateNaiss.getText :" + edtDateNaiss.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            sp = PreferenceManager
                    .getDefaultSharedPreferences(getActivity());
            if (!sp.getBoolean("BIRTH", true)) {
                layoutBirth.setVisibility(View.GONE);
            } else {
                layoutBirth.setVisibility(View.VISIBLE);
            }
            if (!sp.getBoolean("FUMEUR", true)) {
                layoutFumeur.setVisibility(View.GONE);
            } else {
                layoutFumeur.setVisibility(View.VISIBLE);
            }
            if (!sp.getBoolean("HANDICAP", true)) {
                layoutHandicap.setVisibility(View.GONE);
            } else {
                layoutHandicap.setVisibility(View.VISIBLE);
            }
            if (!sp.getBoolean("EMAIL", true)) {
                layoutemail.setVisibility(View.GONE);
            } else {
                layoutemail.setVisibility(View.VISIBLE);
            }

            if (!sp.getBoolean("PAYS", true)) {
                layoutpays.setVisibility(View.GONE);
            } else {
                layoutpays.setVisibility(View.VISIBLE);
            }

            if (!sp.getBoolean("CODEPOSTAL", true)) {
                layoutcodepostal.setVisibility(View.GONE);
            } else {
                layoutcodepostal.setVisibility(View.VISIBLE);
            }

            if (!sp.getBoolean("PROFESSION", true)) {
                layoutprofession.setVisibility(View.GONE);
            } else {
                layoutprofession.setVisibility(View.VISIBLE);
            }
            if (!sp.getBoolean("NOM", true)) {
                layoutNom.setVisibility(View.GONE);
            } else {
                layoutNom.setVisibility(View.VISIBLE);
            }
            if (!sp.getBoolean("PRENOM", true)) {
                layoutPrenom.setVisibility(View.GONE);
            } else {
                layoutPrenom.setVisibility(View.VISIBLE);
            }
            if (!sp.getBoolean("SITFAM", true)) {
                layoutsitfam.setVisibility(View.GONE);
            } else {
                layoutsitfam.setVisibility(View.VISIBLE);
            }
            if (!sp.getBoolean("GSM", true)) {
                layoutgsm.setVisibility(View.GONE);
            } else {
                layoutgsm.setVisibility(View.VISIBLE);
            }
            if (!sp.getBoolean("ADDRESS", true)) {
                layoutaddress.setVisibility(View.GONE);
            } else {
                layoutaddress.setVisibility(View.VISIBLE);
            }

            if (!sp.getBoolean("DOCIDEN", true)) {
                layoutdoc.setVisibility(View.GONE);
            } else {
                layoutdoc.setVisibility(View.VISIBLE);
            }
            if (!sp.getBoolean("CITY", true)) {
                layoutcity.setVisibility(View.GONE);
            } else {
                layoutcity.setVisibility(View.VISIBLE);
            }
            if (!sp.getBoolean("GENDER", true)) {
                layoutgendr.setVisibility(View.GONE);
            } else {
                layoutgendr.setVisibility(View.VISIBLE);
            }

            if (!sp.getBoolean("PAX", true)) {
                btn_update.setVisibility(View.GONE);
            } else {
                btn_update.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    HttpRequestTaskUpdateReservation wsUpdate;

    private String GetDateNaissanceFromEditTet() {
        String dateN = edtDateNaiss.getText().toString();
        DateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        try {
            Date result = df.parse(dateN);
            dateN.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ////log.i("Date Naissance", dateN);
        return dateN;
    }

    @Override
    public void onClick(View v) {

        if (curClient.getClientId() != 0) {

            // Construction de l'objet client
            newClient = new Client();

            newClient.setClientId(curClient.getClientId());

            newClient.setCivilite(GetIdFromSpinner(spCivilite, civi, listCivi));

            newClient.setClientNom(edtNom.getText().toString());

            newClient.setClientPrenom(edtPrenom.getText().toString());

            newClient.setPays(GetIdFromSpinner(spPays, pays, listPays));

            newClient.setAdresse(edtAdresse.getText().toString());

            newClient.setCity(edtCity.getText().toString());

            newClient.setCodePostal(edtCodePostal.getText().toString());

            if (rdGrpSexe.getCheckedRadioButtonId() == R.id.rdMasculin)
                newClient.setSexe("M");
            else
                newClient.setSexe("F");

            switch (rdGrpSitFam.getCheckedRadioButtonId()) {
                case R.id.rdCelib:
                    newClient.setSitFam("C");
                    break;
                case R.id.rdMarie:
                    newClient.setSitFam("M");
                    break;
                case R.id.rdDivorce:
                    newClient.setSitFam("D");
                    break;
            }


            newClient.setNatureDocIdentite(GetIdFromSpinner(spDocTypes,
                    docTypes, listDocTypes));

            newClient.setNumDocIdentite(edtNumDoc.getText().toString());

            newClient.setGsm(edtGsm.getText().toString());

            newClient.setLieuNaiss(edtLieuNaiss.getText().toString());

            newClient.setProfession(edtProfession.getText().toString());

            switch (rdGrpHandicape.getCheckedRadioButtonId()) {
                case R.id.rdHanNon:
                    newClient.setHandicape(0);
                    break;
                case R.id.rdHanOui:
                    newClient.setHandicape(1);
                    break;

            }
            switch (rdGrpFumeur.getCheckedRadioButtonId()) {
                case R.id.rdFumeurNon:
                    newClient.setFumeur(0);
                    break;
                case R.id.rdFumeurOui:
                    newClient.setFumeur(1);
                    break;

            }
            newClient.setDateNaiss(GetDateNaissanceFromEditTet());
            //log.i("Here"," newClient.setDateNaiss(GetDateNaissanceFromEditTet()) " );
            newClient.setEmail(edtEmail.getText().toString().trim());
            EmailValidator emVal = new EmailValidator();

            //region DOC and Email are VISIBLES
//            if ((layoutdoc.getVisibility() == View.VISIBLE)
//                    && (layoutemail.getVisibility() == View.VISIBLE)) {
//                //////log.i("SAVE", "update");
//                if ((!edtEmail.getText().toString().trim().equals("")) && (!edtNumDoc.getText().toString().trim().equals(""))) {
//
//                    if (emVal.validate(edtEmail.getText().toString().trim())) {


                        customProgress1.setMessage(getResources().getText(R.string.msg_saving_resa));
                        //customProgress1.show();
                        //log.i("ClientToUpdate Fragment", newClient.toString());
                        wsUpdate = new HttpRequestTaskUpdateReservation(newClient);
                        wsUpdate.execute();


//                    } else {
//                        edtEmail.setError(getResources().getText(R.string.msg_mail_invalide));
//
//                    }
//                } else {
//                    if (edtEmail.getText().toString().trim().equals("") && (layoutemail.getVisibility() == View.VISIBLE)) {
//                        edtEmail.setError(getResources().getText(
//                                R.string.champs_requis));
//                    }
//                    if (edtNumDoc.getText().toString().trim().equals("") && (layoutdoc.getVisibility() == View.VISIBLE)) {
//                        edtNumDoc.setError(getResources().getText(
//                                R.string.champs_requis));
//                    }
//                }
//            }
            //endregion

            //region Doc VISIBLE and Email INVISIBLE
//            if ((layoutdoc.getVisibility() == View.VISIBLE) && (layoutemail.getVisibility() != View.VISIBLE)) {
//
//                // Doc Visible  , Email Non Visible
//                if (!edtNumDoc.getText().toString().trim().equals("")) {
//                    customProgress1.setMessage(getResources().getText(R.string.msg_saving_resa));
//                    //log.i("ClientToUpdate Fragment", newClient.toString());
//
//                    wsUpdate = new HttpRequestTaskUpdateReservation(newClient);
//                    wsUpdate.execute();
//                } else {
//                    edtNumDoc.setError(getResources().getText(
//                            R.string.champs_requis));
//                }
//            }
            //endregion

            //region Email VISBLE and Doc INVISIBLE
//            if ((layoutemail.getVisibility() == View.VISIBLE) && (layoutdoc.getVisibility() != View.VISIBLE)) {
//                // Email Visible  , Doc Non Visible
//                if (!edtEmail.getText().toString().trim().equals("")) {
//                    if (emVal.validate(edtEmail.getText().toString().trim())) {
//
//
//                        customProgress1.setMessage(getResources().getText(R.string.msg_saving_resa));
//                        //log.i("ClientToUpdate Fragment", newClient.toString());
//
//                        wsUpdate = new HttpRequestTaskUpdateReservation(newClient);
//                        wsUpdate.execute();
//                    } else {
//                        edtEmail.setError(getResources().getText(
//                                R.string.msg_mail_invalide));
//                    }
//                } else {
//                    edtEmail.setError(getResources().getText(
//                            R.string.champs_requis));
//                }
//            }
//            //endregion
//
//            if ((layoutemail.getVisibility() != View.VISIBLE) && (layoutdoc.getVisibility() != View.VISIBLE)) {
//                customProgress1.setMessage(getResources().getText(R.string.msg_saving_resa));
//                //customProgress1.show();
//                //log.i("ClientToUpdate Fragment", newClient.toString());
//                wsUpdate = new HttpRequestTaskUpdateReservation(newClient);
//                wsUpdate.execute();
//            }
        }
    }

    private void ShowSuccesUpdateDialog(Boolean b) {

        customProgress1.hide();

        alertDialogBuilder = new AlertDialog.Builder(getActivity());

        if (b) {
            alertDialogBuilder.setMessage(getResources().getText(
                    R.string.msg_succes_update));
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //ResetFields();

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
        alertDialogBuilder = new AlertDialog.Builder(getActivity());

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

    private String TAG = OneFragment.class.getSimpleName();

    /*
     *
     *
     */



    private class HttpRequestTaskUpdateReservation extends AsyncTask<Void, Void, Boolean> {
        Boolean response = null;

        Client cliToUpdate;

        public HttpRequestTaskUpdateReservation(Client cliToUpdate) {
            this.cliToUpdate = cliToUpdate;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {

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
                RestTemplate restTemplate = new RestTemplate();
                //log.i(TAG, "Update Reservation :" + url.toString());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                response = restTemplate.getForObject(url, Boolean.class);
                //log.i(TAG, "Update Reservation :" + response.toString());

                return response;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);

            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean greeting) {
            customProgress1.hide();
            ShowSuccesUpdateDialog(response);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customProgress1.show();
            ////log.i(TAG, "GET Reservation");
        }
    }

    public String getURLAPI() {
        String URL = null;
        try {
            SharedPreferences sp = PreferenceManager
                    .getDefaultSharedPreferences(getActivity());
            URL = sp.getString("SERVEUR", "192.168.0.100");

            URL = "http://" + URL + "/HNGAPI/api/apiPreCheckIn/";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return URL;
    }

}
