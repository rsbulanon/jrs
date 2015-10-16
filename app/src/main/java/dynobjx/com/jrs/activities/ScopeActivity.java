package dynobjx.com.jrs.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.adapters.BrgyAdapter;
import dynobjx.com.jrs.dao.Barangay;
import dynobjx.com.jrs.dao.City;
import dynobjx.com.jrs.dao.Province;
import dynobjx.com.jrs.helpers.ApiRequestHelper;
import dynobjx.com.jrs.helpers.AppConstants;
import dynobjx.com.jrs.helpers.DaoHelper;
import dynobjx.com.jrs.helpers.Prefs;


public class ScopeActivity extends BaseActivity implements ApiRequestHelper.OnAPIRequestListener {

    @Bind(R.id.spnrProvince) Spinner spnrProvice;
    @Bind(R.id.spnrCity) Spinner spnrCity;
    @Bind(R.id.lvBrgy) ListView lvBrgy;
    @Bind(R.id.tvResultHeader) TextView tvResultHeader;
    @Bind(R.id.tvStatus) TextView tvStatus;
    private Typeface tfRoboto;
    private ApiRequestHelper apiRequestHelper;
    private int selectedProvinceId = 0;
    private int selectedCityId = 0;
    private ArrayList<String> brgys = new ArrayList<>();
    //private String token =

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scope);
        ButterKnife.bind(this);
        initActionBar("Scope of Delivery");
        tfRoboto = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
        apiRequestHelper = new ApiRequestHelper();
        apiRequestHelper.setOnAPIRequestListener(this);
        lvBrgy.setAdapter(new BrgyAdapter(this, brgys));

        Log.d("token", " "+ Prefs.getMyStringPrefs(this, AppConstants.ACCESS_TOKEN));

        loadProvincesFromDB();

        /** attach listener to province spinner */
        spnrProvice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    selectedProvinceId = i;
                    /** check first if there is cached cities under the selected
                     * province from local db, if size is greater than 0, no need for
                     * an API request to fetch town/municipality */
                    if (DaoHelper.getCitiesByProvId(i).size() > 0) {
                        Log.d("prov","load from local DB");
                        loadCitiesFromDB();
                    } else {
                        Log.d("prov", "fetch from API");
                        if (isNetworkAvailable()) {
                            apiRequestHelper.getMunicipality(i, Prefs.getMyStringPrefs(ScopeActivity.this, AppConstants.ACCESS_TOKEN));

                        } else {
                            tvStatus.setText(AppConstants.ERR_CONNECTION);
                            loadCitiesFromDB();
                        }
                    }
                    /** reset result view everytime new province is selected */
                    selectedCityId = 0;
                    loadBrgyFromDB();
                    tvStatus.setText(getResources().getString(R.string.default_status));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /** attach to listener to city spinner */
        spnrCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    selectedCityId = i;
                    int muniId = getCityId(selectedProvinceId, spnrCity.getSelectedItem().toString());
                    /** check first if there are cached brgy result from local DB
                     * by provinceID and municipalityID, if size is greater than 0
                     * load cached result */
                    if (DaoHelper.getBarangays(selectedProvinceId,selectedCityId).size() > 0) {
                        Log.d("brgy","load from DB");
                        loadBrgyFromDB();
                    } else {
                        /** fetch barangay from API if network is available */
                        if (isNetworkAvailable()) {
                            Log.d("brgy","fetch brgy from API");
                            apiRequestHelper.getBrgyByCityId(muniId, Prefs.getMyStringPrefs(ScopeActivity.this, AppConstants.ACCESS_TOKEN));
                        } else {
                            tvStatus.setText(AppConstants.ERR_CONNECTION);
                            loadBrgyFromDB();
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        loadCitiesFromDB();
    }

      private void loadProvincesFromDB() {
        ArrayList<String> provinces = new ArrayList<>();
        for (Province p : DaoHelper.getAllProvinces()) {
            provinces.add(p.getProvinceName());
        }
        initSpinner(spnrProvice, provinces, "Select Province");
    }

    private void loadCitiesFromDB() {
        ArrayList<String> cities = new ArrayList<>();
        for (City c : DaoHelper.getCitiesByProvId(selectedProvinceId)) {
            cities.add(c.getCityName());
        }
        initSpinner(spnrCity,cities,"Select Town/Municipality");
    }

    private void loadBrgyFromDB() {
        brgys.clear();
        for (Barangay c : DaoHelper.getBarangays(selectedProvinceId, selectedCityId)) {
            brgys.add(c.getBrgyName());
        }
        tvResultHeader.setText(brgys.size() > 0 ? "Result (" + brgys.size() + " brgy/s)" : "Result");
        lvBrgy.setVisibility(brgys.size() > 0 ? View.VISIBLE : View.GONE);
        tvStatus.setVisibility(brgys.size() > 0 ? View.GONE : View.VISIBLE);
        ((BaseAdapter)lvBrgy.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onRequestStart(String action) {
        if (action.equals(AppConstants.GET_CITY_MUN)) {
            showProgressDialog("Scope","Getting list of City/Municipality, Please wait...");
        } else {
            showProgressDialog("Scope","Getting list of Barangay, Please wait...");
        }
    }

    @Override
    public void onRequestException(Exception e) {
        dismissProgressDialog();
    }

    @Override
    public void onRequestSuccessful(String action, String json) {
        dismissProgressDialog();
        if (action.equals(AppConstants.GET_CITY_MUN)) {
            saveCityToLocalDB(json,selectedProvinceId);
            loadCitiesFromDB();
        } else {
            if (json == null) {
                tvStatus.setText("No result/s found!");
            } else {
                saveBrgyToLocalDB(json, selectedProvinceId, selectedCityId);
                loadBrgyFromDB();
            }
        }
    }
}
