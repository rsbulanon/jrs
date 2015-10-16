package dynobjx.com.jrs.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.adapters.CustomSpinnerAdapter;
import dynobjx.com.jrs.customviews.HintSpinner;
import dynobjx.com.jrs.dao.Barangay;
import dynobjx.com.jrs.dao.Branch;
import dynobjx.com.jrs.dao.City;
import dynobjx.com.jrs.dao.Country;
import dynobjx.com.jrs.dao.Province;
import dynobjx.com.jrs.dao.UserInfo;
import dynobjx.com.jrs.helpers.AppConstants;
import dynobjx.com.jrs.helpers.DaoHelper;
import dynobjx.com.jrs.helpers.Prefs;
import dynobjx.com.jrs.models.RateCalculator;
import dynobjx.com.jrs.models.Track;

/**
 * Created by rsbulanon on 7/8/15.
 */
public class BaseActivity extends AppCompatActivity {

    private SweetAlertDialog pDialog;
    private static ArrayList<Branch> branches = new ArrayList<>();
    private static ArrayList<Track> tracks = new ArrayList<>();
    private static ArrayList<Province> province = new ArrayList<>();
    public static ArrayList<RateCalculator> ratecalculator = new ArrayList<>();
    private static ArrayList<Branch> branchesNearMe = new ArrayList<>();
    private static int MIN_DISTANCE = 5;
    private static Typeface TF_ROBOTO = null;

    public void initActionBar(String header) {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_bg)));
        getSupportActionBar().setTitle(header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.drawable.actionbar_logo);
    }

    public void initActionBar(String header, boolean visible) {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_bg)));
        getSupportActionBar().setTitle(header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(visible);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setIcon(R.drawable.actionbar_logo);

    }

    public Bundle getRequestBundle(String method, String result, String property, String id) {
        Bundle bundle = new Bundle();
        bundle.putString("method", method);
        bundle.putString("result", result);
        bundle.putString("property", property);
        bundle.putString("id", id);
        return bundle;
    }

    public void showSweetDialog(String title, String message, String type) {
        int dialogType = 0;
        if (type.equals("error")) {
            dialogType = SweetAlertDialog.ERROR_TYPE;
        } else if (type.equals("warning")) {
            dialogType = SweetAlertDialog.WARNING_TYPE;
        } else if (type.equals("success")) {
            dialogType = SweetAlertDialog.SUCCESS_TYPE;
        }
        new SweetAlertDialog(this, dialogType)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText("Close")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                }).show();
    }

    public void showProgressDialog(String title, String message) {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(title);
        pDialog.setContentText(message);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void updateProgressDialog(String message) {
        pDialog.setContentText(message);
    }

    public void addMapMarker(GoogleMap map, double lat, double longi, String title,
                             String snippet, int marker, boolean nearByBranch) {
        map.addMarker(new MarkerOptions().position(new LatLng(lat, longi)).
                title(title).snippet(snippet).icon(
                marker == -1 ? (nearByBranch ? BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                        : null) : BitmapDescriptorFactory.fromResource(marker))).showInfoWindow();
    }

    public SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat("M/dd/yyyy");
    }

    public void dismissProgressDialog() {
        pDialog.dismiss();
    }

    public void animateToRight(Activity activity) {
        activity.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    public void animateToLeft(Activity activity) {
        activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void animateToUp(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_up, R.anim.slide_up);
    }

    public void animateToBottom(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_up, R.anim.slide_up);
    }

    public void animateFadeIn(Activity activity) {
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void animateFadeOut(Activity activity) {
        activity.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void AutoComplete(String[] place, AutoCompleteTextView autoComplete) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, place);
        autoComplete.setAdapter(adapter);
    }

    /**
     * population branches
     */
    public void initBranches(ArrayList<Branch> branches) {
        if (branches.size() == 0) {
            branches.addAll(branches);
        }
    }


    /**
     * get branches
     */
    public ArrayList<Branch> getBranches() {
        return branches;
    }

    /**
     * get trackings
     */
    public ArrayList<Track> getTrackings() {
        return tracks;
    }
    /**
     * get ratecalcu values
     */
    public ArrayList<RateCalculator> getRateCalculator() {
        return ratecalculator;
    }

    public ArrayList<Province> getProvince() {
        return province;
    }

    /**
     * compute branches distance near me
     */

    public void computeBranchesNearMe(double myLati, double myLongi) {
        branchesNearMe.clear();
        Location myLocation = new Location("");
        myLocation.setLatitude(myLati);
        myLocation.setLongitude(myLongi);
        for (Branch b : DaoHelper.getAllBranches()) {
            Location branchLocation = new Location("");
            branchLocation.setLatitude(b.getLatitude());
            branchLocation.setLongitude(b.getLongitude());

            /** get branches whose distance is within 10 kilometers */
            Log.d("branches", b.getBranchName() + " lat --> " + b.getLatitude() + "  long --> " + b.getLongitude()
                    + "  distance --> " + (myLocation.distanceTo(branchLocation) / 1000));
            if ((myLocation.distanceTo(branchLocation) / 1000) < MIN_DISTANCE) {
                branchesNearMe.add(b);
            }
        }
    }

    /**
     * get branches near me
     */
    public ArrayList<Branch> getBranchesNearMe() {
        return branchesNearMe;
    }


    /**
     * Rate Calculator
     * @param rateCalculator
     */
    public ArrayList<RateCalculator> getRateCalculator(RateCalculator rateCalculator) {
        return ratecalculator;
    }

    /**
     * get branch by name
     */
    public Branch getBranchByName(String name) {
        for (Branch b : getBranchesNearMe()) {
            if (b.getBranchName().equalsIgnoreCase(name)) {
                return b;
            }
        }
        return null;
    }

    /**
     * branches by keyword
     */
    public ArrayList<Branch> getBranchesByKeyword(String keyword) {
        ArrayList<Branch> branches = new ArrayList<>();
        for (Branch b : DaoHelper.getAllBranches()) {
            if (b.getBranchName().toLowerCase().contains(keyword.toLowerCase())) {
                branches.add(b);
            } else {
                Log.d("key", "branchname --> " + b.getBranchName() + "  x   " + keyword.toLowerCase());
            }
        }
        return branches;
    }

    public void setUserCredentials(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            Prefs.setMyStringPref(this,AppConstants.ACCESS_TOKEN,obj.getString("access_token"));
            Prefs.setMyStringPref(this,AppConstants.USERNAME,obj.getString("userName"));
            Prefs.setMyStringPref(this,AppConstants.ACCESS_TOKEN_EXPIRY,obj.getString(".expires"));
        } catch (JSONException ex) {
            Log.d("token", "error in parsing json token --> " + ex.toString());
        }
    }

    public boolean isUserAuthenticated(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            return obj.has("error_description") ? false : true;
        } catch (JSONException ex) {
            Log.d("token", "error in parsing json token --> " + ex.toString());
            return false;
        }
    }

    /**
     * check network availability
     */
    public boolean isNetworkAvailable() {
        boolean isConnected = false;
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
            isConnected = true;
        } else {
            NetworkInfo mData = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mData == null) {
                isConnected = false;
            } else {
                boolean isDataEnabled = mData.isConnected();
                isConnected = isDataEnabled ? true : false;
            }
        }
        return isConnected;

    }


    /** check if GPS is enabled */
    public boolean isGPSEnabled() {
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        }
        return true;
    }

    /** save list of province to local db */
    public void saveProvinceToLocalDB(String json) {
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0 ; i < array.length() ; i++) {
                JSONObject obj = array.getJSONObject(i);
                DaoHelper.addProvince(new Province(null,obj.getInt("Id"),obj.getString("Name")));
            }
        } catch (JSONException e) {
            Log.d("err","ERROR in parsing province json --> " + e.toString());
        }
    }
    /** save list of countries to local db */

    public void saveCountryToLocalDB(String json) {
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0 ; i < array.length() ; i++) {
                JSONObject obj = array.getJSONObject(i);
                DaoHelper.addCountry(new Country(null, obj.getInt("Id"), obj.getString("Name")));
            }
        } catch (JSONException e) {
            Log.d("err","ERROR in parsing province json --> " + e.toString());
        }
    }

    /** save user info to local db */
    public void saveUserInfoToLocalDB(String json) {
        try {
            Log.d("info", "json --> " + json);
            JSONObject obj = new JSONObject(json);
            DaoHelper.addUserInfo(new UserInfo(null,
                    obj.getString("AspNetUserName"),
                    obj.getInt("Brgyid"),
                    obj.getString("Address"),
                    obj.getString("FullName").equals("null") ? AppConstants.NOT_SET : obj.getString("FullName"),
                    obj.getString("Phone"),""));
            Log.d("info","address --> " + obj.getString("Address"));
        } catch (JSONException e) {
            Log.d("err","ERROR in parsing province json --> " + e.toString());
        }
    }

    /** update user info in local db */
    public void updateUserInfoInLocalDB(String json, String location) {
        try {
            Log.d("user","json --> " + json);
            JSONObject obj = new JSONObject(json);
            UserInfo userInfo = DaoHelper.getUserInfo();
            userInfo.setUserName(obj.getString("AspNetUserName"));
            userInfo.setAddress(obj.getString("Address"));
            userInfo.setBrgyId(obj.getInt("Brgyid"));
            userInfo.setFullName(obj.getString("FullName").equals("null") ? AppConstants.NOT_SET : obj.getString("FullName"));
            userInfo.setLocation(location);
            DaoHelper.updateUserInfo(userInfo);
        } catch (JSONException e) {
            Log.d("err","ERROR in parsing province json --> " + e.toString());
        }
    }

    /** save list of city to local db */
    public void saveCityToLocalDB(String json, int provId) {
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0 ; i < array.length() ; i++) {
                JSONObject obj = array.getJSONObject(i);
                DaoHelper.addCity(new City(null, obj.getInt("Id"), obj.getString("Name"), provId));
            }
        } catch (JSONException e) {
            Log.d("err","ERROR in parsing city json --> " + e.toString());
        }
    }

    /** save list of barangay to local db */
    public void saveBrgyToLocalDB(String json, int provId, int muniId) {
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0 ; i < array.length() ; i++) {
                JSONObject obj = array.getJSONObject(i);
                DaoHelper.addBarangay(new Barangay(null, obj.getInt("Id"), obj.getString("Name"), provId, muniId));
            }
        } catch (JSONException e) {
            Log.d("err","ERROR in parsing city json --> " + e.toString());
        }
    }

    /** save brancehs to local DB */
    public void saveBranchesToLocalDB(String json) {
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0 ; i < arr.length() ; i++) {
                /** populate local copy of Branches */
                JSONObject obj = arr.getJSONObject(i);

                int branchId = obj.getInt("Id");
                String branchName = obj.getString("Name");
                String branchAddress = obj.getString("Address").trim().isEmpty() ? AppConstants.NOT_AVAIL : obj.getString("Address") ;
                String contactPerson = obj.getString("ContactPerson").equals("null") ? AppConstants.NOT_AVAIL : obj.getString("ContactPerson") ;
                String contactNumbers = obj.getString("ContactNumbers").equals("null") ? AppConstants.NOT_AVAIL : obj.getString("ContactNumbers");
                double longitude = Double.parseDouble(obj.getString("Lon"));
                double latitude = Double.parseDouble(obj.getString("Lat"));

                /** add newly constructed Branch to Branches List */
                DaoHelper.addBranch(new Branch(null, branchId, branchName, branchAddress, contactPerson,
                        contactNumbers, longitude, latitude));
            }
        } catch (JSONException ex) {
            dismissProgressDialog();
            Log.d("branches","error in parsing JSON --> " + ex.toString());
        }
    }

    public int getCityId(int provId, String city) {
        for (City c : DaoHelper.getCitiesByProvId(provId)) {
            if (c.getCityName().equalsIgnoreCase(city)) {
                return c.getCityId();
            }
        }
        return -1;
    }

    public int getBrgyId(int provId, int muniId, String brgy) {
        for (Barangay b : DaoHelper.getBarangays(provId, muniId)) {
            if (b.getBrgyName().equalsIgnoreCase(brgy)) {
                return b.getBrgyId();
            }
        }
        return -1;
    }

    public void initSpinner(Spinner spinner, ArrayList<String> items,String hint) {
        if (TF_ROBOTO == null) {
            TF_ROBOTO = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
        }
        ArrayAdapter<String> adapter = new CustomSpinnerAdapter(this,items,TF_ROBOTO, Color.BLACK, 20);
        adapter.setDropDownViewResource(AppConstants.SPINNER_DROP_DOWN);
        spinner.setAdapter(new HintSpinner(adapter, R.layout.custom_spinner_hint,
                this, AppConstants.SPINNER_DROP_DOWN, Color.BLACK, 20, hint));
    }

    public SimpleDateFormat getPickupDateFormat() { return new SimpleDateFormat("MMM dd yyyy hh:mm a"); }

    public DecimalFormat getDecFormat() { return new DecimalFormat("###,###.00"); }

}

