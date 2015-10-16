package dynobjx.com.jrs.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;


import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.activities.BaseActivity;
import dynobjx.com.jrs.activities.PickupActivity;
import dynobjx.com.jrs.dao.Barangay;
import dynobjx.com.jrs.dao.City;
import dynobjx.com.jrs.dao.Province;
import dynobjx.com.jrs.helpers.ApiRequestHelper;
import dynobjx.com.jrs.helpers.AppConstants;
import dynobjx.com.jrs.helpers.DaoHelper;
import dynobjx.com.jrs.helpers.Prefs;

/**
 * Created by rsbulanon on 8/4/15.
 */
public class PickupDialogFragment extends DialogFragment implements ApiRequestHelper.OnAPIRequestListener {

    @Bind(R.id.spnrProvince) Spinner spnrProvince;
    @Bind(R.id.spnrCity) Spinner spnrCity;
    @Bind(R.id.spnrBrgy) Spinner spnrBrgy;
    private View view;
    private ArrayList<String> provinces = new ArrayList<>();
    private ApiRequestHelper apiRequestHelper;
    private BaseActivity activity;
    private int selectedProvinceId = 0;
    private int selectedCityId = 0;
    private int selectedBrygId = 0;
    private String action;
    private OnLocationListener onLocationListener;

    public static PickupDialogFragment newInstance(String action) {
        PickupDialogFragment frag = new PickupDialogFragment();
        frag.action = action;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        view = getActivity().getLayoutInflater().inflate(R.layout.modal_pickup_location, null);
        activity = (BaseActivity)getActivity();
        apiRequestHelper = new ApiRequestHelper();
        apiRequestHelper.setOnAPIRequestListener(this);
        ButterKnife.bind(this, view);

        loadProvincesFromDB(spnrProvince);

        /** attach listener to origin province spinner */
        spnrProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    selectedProvinceId = i;
                    /** check first if there is cached cities under the selected
                     * province from local db, if size is greater than 0, no need for
                     * an API request to fetch town/municipality */
                    if (DaoHelper.getCitiesByProvId(i).size() > 0) {
                        loadCitiesFromDB(spnrCity, selectedProvinceId);
                    } else {
                        if (activity.isNetworkAvailable()) {
                            apiRequestHelper.getMunicipality(i, Prefs.getMyStringPrefs(getActivity(), AppConstants.ACCESS_TOKEN));
                        } else {
                            activity.showToast(AppConstants.ERR_CONNECTION);
                            loadCitiesFromDB(spnrCity, selectedProvinceId);
                        }
                    }
                    /** reset result view everytime new province is selected */
                    selectedCityId = 0;
                    loadBrgyFromDB(spnrBrgy);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /** attach to listener to origin city spinner */
        spnrCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    selectedCityId = i;
                    int muniId = activity.getCityId(selectedProvinceId,
                            spnrCity.getSelectedItem().toString());
                    /** check first if there are cached brgy result from local DB
                     * by provinceID and municipalityID, if size is greater than 0
                     * load cached result */
                    if (DaoHelper.getBarangays(selectedProvinceId, selectedCityId).size() > 0) {
                        loadBrgyFromDB(spnrBrgy);
                    } else {
                        /** fetch barangay from API if network is available */
                        if (activity.isNetworkAvailable()) {
                            apiRequestHelper.getBrgyByCityId(muniId, Prefs.getMyStringPrefs(getActivity(), AppConstants.ACCESS_TOKEN));
                        } else {
                            activity.showToast(AppConstants.ERR_CONNECTION);
                            loadBrgyFromDB(spnrBrgy);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /** attach listener to origin spnrBrgy */
        spnrBrgy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    selectedBrygId = activity.getBrgyId(selectedProvinceId, selectedCityId,
                            spnrBrgy.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final Dialog mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        return mDialog;
    }

    @OnClick(R.id.btnProceed)
    public void validateLocation() {
        if (selectedProvinceId == 0) {
            activity.showToast(AppConstants.WARNING_PROVINCE);
        } else if (selectedCityId == 0) {
            activity.showToast(AppConstants.WARNING_TOWN);
        } else if (selectedBrygId == 0) {
            activity.showToast(AppConstants.WARNING_BRGY);
        } else {
            if (action.equals("origin")) {
                if (activity.isNetworkAvailable()) {
                    selectedBrygId = activity.getBrgyId(selectedProvinceId, selectedCityId,
                            spnrBrgy.getSelectedItem().toString());
                    apiRequestHelper.validatePickupLocation(selectedBrygId, Prefs.getMyStringPrefs(getActivity(), AppConstants.ACCESS_TOKEN));
                } else {
                    activity.showToast(AppConstants.ERR_CONNECTION);
                }
            } else {
                selectedLocation();
            }
        }
    }

    @Override
    public void onRequestStart(String action) {
        if (action.equals(AppConstants.GET_CITY_MUN)) {
            activity.showProgressDialog("Pickup Request", "Getting list of City/Municipality, Please wait...");
        } else if (action.equals(AppConstants.GET_BARANGAY)) {
            activity.showProgressDialog("Pickup Request", "Getting list of Barangay, Please wait...");
        } else if (action.equals(AppConstants.GET_VALIDATE_PU_LOC)) {
            activity.showProgressDialog("Pickup Request","Validating your pickup location, Please wait..");
        } else if (action.equals(AppConstants.POST_PICKUP)) {
            activity.showProgressDialog("Pickup Request", "Sending Pickup request, Please wait..");
        }
    }

    @Override
    public void onRequestException(Exception e) {

    }

    @Override
    public void onRequestSuccessful(String action, String result) {
        activity.dismissProgressDialog();
        if (action.equals(AppConstants.GET_CITY_MUN)) {
            activity.saveCityToLocalDB(result, selectedProvinceId);
            loadCitiesFromDB(spnrCity,selectedProvinceId);
        } else if (action.equals(AppConstants.GET_BARANGAY)) {
            if (result == null) {
                activity.showToast(AppConstants.ERR_NO_RESULT);
            } else {
                activity.saveBrgyToLocalDB(result, selectedProvinceId, selectedCityId);
                loadBrgyFromDB(spnrBrgy);
            }
        } else if (action.equals(AppConstants.GET_VALIDATE_PU_LOC)) {
            if (result.contains("Ok")) {
                selectedLocation();
            } else if (result.contains("Pickup is out of range")) {
                activity.showSweetDialog("Pickup Request", "Pickup location is out of range", "warning");
            } else if (result.contains("Google API could not locate your address")) {
                activity.showSweetDialog("Pickup Request", "Our service could not locate your pickup location", "error");
            }
        }
    }

    private void loadProvincesFromDB(Spinner spinner) {
        ArrayList<String> provinces = new ArrayList<>();
        for (Province p : DaoHelper.getAllProvinces()) {
            provinces.add(p.getProvinceName());
        }
        activity.initSpinner(spinner, provinces, "Select Province");
    }

    private void loadCitiesFromDB(Spinner spinner, int provId) {
        ArrayList<String> cities = new ArrayList<>();
        for (City c : DaoHelper.getCitiesByProvId(provId)) {
            cities.add(c.getCityName());
        }
        activity.initSpinner(spinner, cities, "Select Town/Municipality");
    }

    private void loadBrgyFromDB(Spinner spinner) {
        ArrayList<String> brgys = new ArrayList<>();
        for (Barangay c : DaoHelper.getBarangays(selectedProvinceId, selectedCityId)) {
            brgys.add(c.getBrgyName());
        }
        activity.initSpinner(spinner,brgys,"Select Barangay");
    }

    public interface OnLocationListener {
        void onSelectedLocation(int brgyId, String location);
    }

    public void setOnLocationListener(OnLocationListener onLocationListener) {
        this.onLocationListener = onLocationListener;
    }

    private void selectedLocation() {
        String location = spnrBrgy.getSelectedItem().toString() + ","
                + spnrCity.getSelectedItem().toString() + ","
                + spnrProvince.getSelectedItem().toString();
        onLocationListener.onSelectedLocation(selectedBrygId,location);
    }
}
