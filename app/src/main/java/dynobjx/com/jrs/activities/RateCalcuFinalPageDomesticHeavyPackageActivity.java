package dynobjx.com.jrs.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.customviews.EditTextPlus;
import dynobjx.com.jrs.dao.Province;
import dynobjx.com.jrs.helpers.ApiRequestHelper;
import dynobjx.com.jrs.helpers.AppConstants;
import dynobjx.com.jrs.helpers.DaoHelper;
import dynobjx.com.jrs.helpers.Prefs;

public class RateCalcuFinalPageDomesticHeavyPackageActivity extends BaseActivity implements ApiRequestHelper.OnAPIRequestListener{

    @Bind(R.id.spnrOrigin) Spinner spnrOrigin;
    @Bind(R.id.spnrDestination)  Spinner spnrDestination;
    @Bind(R.id.etWeight) EditTextPlus etWeight;
    @Bind(R.id.etWidth) EditTextPlus etWidth;
    @Bind(R.id.etLength) EditTextPlus etLength;
    @Bind(R.id.etHeight) EditTextPlus etHeight;
    @Bind(R.id.etDeclaredValue) EditTextPlus etDeclaredValue;
    @Bind(R.id.chkInsurance) CheckBox chkInsurance;
    @Bind(R.id.chkValuation) CheckBox chkValuation;

    private int selectedOriginId = 0;
    private int selectedDestinationId = 0;
    private ApiRequestHelper apiRequestHelper = new ApiRequestHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_calcu_final_page_domestic_heavy_package);
        ButterKnife.bind(this);
        initActionBar("Rate Calculator");

        apiRequestHelper = new ApiRequestHelper();
        apiRequestHelper.setOnAPIRequestListener(this);

        if (DaoHelper.getProvinceCount() == 0) {
            if (isNetworkAvailable()) {
                apiRequestHelper.getProvinces(Prefs.getMyStringPrefs(this,AppConstants.ACCESS_TOKEN));
            } else {
                Toast.makeText(this, AppConstants.ERR_CONNECTION, Toast.LENGTH_LONG).show();
            }
        } else {
            loadProvincesFromDB();
        }

        spnrOrigin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    selectedOriginId = i;
                    if (DaoHelper.getCitiesByProvId(i).size() > 0) {
                    } else {
                        if (isNetworkAvailable()) {
                            apiRequestHelper.getMunicipality(i, Prefs.getMyStringPrefs(RateCalcuFinalPageDomesticHeavyPackageActivity.this,AppConstants.ACCESS_TOKEN));
                        } else {
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrDestination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    selectedDestinationId = i;
                    if (DaoHelper.getCitiesByProvId(i).size() > 0) {
                    } else {
                        if (isNetworkAvailable()) {
                            apiRequestHelper.getMunicipality(i, Prefs.getMyStringPrefs(RateCalcuFinalPageDomesticHeavyPackageActivity.this,AppConstants.ACCESS_TOKEN));
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadProvincesFromDB() {
        ArrayList<String> provinces = new ArrayList<>();
        for (Province p : DaoHelper.getAllProvinces()) {
            provinces.add(p.getProvinceName());
        }
        initSpinner(spnrOrigin, provinces, "Select Origin");
        initSpinner(spnrDestination, provinces, "Select Destination");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_pu_request, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_send_request) {
            if (!isNetworkAvailable()) {
                showToast(AppConstants.ERR_CONNECTION);
            } else if (selectedOriginId == 0) {
                showToast(AppConstants.WARNING_SELECT_ORIGIN);
            } else if (selectedDestinationId == 0) {
                showToast(AppConstants.WARNING_SELECT_DESTINATION);
            } else if (etDeclaredValue.getText().toString().isEmpty()) {
                showToast(AppConstants.WARNING_DECLARED_VALUE);
            } else if (etWeight.getText().toString().isEmpty()) {
                showToast(AppConstants.WARNING_WEIGHT_IN_GRAMS);
            } else if (etHeight.getText().toString().isEmpty()) {
                showToast(AppConstants.WARNING_HEIGHT);
            } else {
                Double txtWeight = Double.parseDouble(etWeight.getText().toString());
                Double height = Double.parseDouble(etHeight.getText().toString());
                Double width = Double.parseDouble(etWidth.getText().toString());
                Double length = Double.parseDouble(etLength.getText().toString());

                Double declaredValue = Double.parseDouble(etDeclaredValue.getText().toString());
                String origin_string = spnrOrigin.getSelectedItem().toString();
                String dest_string = spnrDestination.getSelectedItem().toString();
                String packageType = "heavy";

                Bundle bundle = new Bundle();
                bundle.putDouble("Weight", txtWeight);
                bundle.putDouble("Height", height);
                bundle.putDouble("Width", width);
                bundle.putDouble("Length", length);
                bundle.putDouble("DeclaredValue", declaredValue);
                bundle.putInt("Origin", selectedOriginId);
                bundle.putInt("Destination", selectedDestinationId);
                bundle.putBoolean("Valuation", chkValuation.isChecked());
                bundle.putBoolean("Insurance", chkInsurance.isChecked());
                bundle.putString("OriginString", origin_string);
                bundle.putString("DestString", dest_string);
                bundle.putString("PackageType", packageType);

                Intent in = new Intent(RateCalcuFinalPageDomesticHeavyPackageActivity.this,RateCalcuSummaryActivity.class);
                in.putExtras(bundle);
                startActivity(in);
                animateToLeft(this);
            }
        }
        return false;
    }


    @Override
    public void onRequestStart(String action) {

        if (action.equals(AppConstants.GET_PROVINCES)) {

        }
    }

    @Override
    public void onRequestException(Exception e) {

    }
    @Override
    public void onRequestSuccessful(String action, String body) {

        Log.d("RATE", body);
        if (action.equals(AppConstants.GET_PROVINCES)) {
            saveProvinceToLocalDB(body);
            loadProvincesFromDB();
        }  else {
            if (body == null) {
                Toast.makeText(this, "NO RESULTS FOUND", Toast.LENGTH_LONG).show();
            } else {
            }
        }

        if (action.equals(AppConstants.INTERNATIONAL_DESTINATION)) {
            saveProvinceToLocalDB(body);
            loadProvincesFromDB();
        }  else {
            if (body == null) {
                Toast.makeText(this, "NO RESULTS FOUND", Toast.LENGTH_LONG).show();
            } else {
            }
        }
    }

}
