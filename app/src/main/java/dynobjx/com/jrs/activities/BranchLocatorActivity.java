package dynobjx.com.jrs.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.adapters.BranchesTabAdapter;
import dynobjx.com.jrs.fragments.BranchesByKeywordFragment;
import dynobjx.com.jrs.fragments.BranchesNearMeFragment;
import dynobjx.com.jrs.helpers.AppConstants;

/**
 * Created by rsbulanon on 7/9/15.
 */
public class BranchLocatorActivity extends BaseActivity implements LocationListener {

    @Bind(R.id.tabLayout) TabLayout tabLayout;
    @Bind(R.id.viewPager) ViewPager viewPager;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private static final String NOT_AVAILABLE = "Not Available";
    private static final int ENABLE_GPS = 1;
    private Location location;
    private LocationManager locationManager;
    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_locator);
        ButterKnife.bind(this);
        fragments.add(BranchesByKeywordFragment.newInstance());
        fragments.add(BranchesNearMeFragment.newInstance());
        viewPager.setAdapter(new BranchesTabAdapter(getSupportFragmentManager(), fragments));
        tabLayout.setTabTextColors(Color.WHITE, getResources().getColor(R.color.action_bar_bg));
        tabLayout.setupWithViewPager(viewPager);
        initActionBar("Branch Locator");

        /**
         * if BranchesNearMe tab is selected enable GPS if it's not enabled
         * */
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    if (!isGPSEnabled()) {
                        enableGPS();
                    } else {
                        getLocation();
                    }
                }
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onPause() {
        Log.d("act","on pause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d("act","on destroy");
        super.onDestroy();
    }

    private void enableGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enable GPS");
        builder.setMessage("Please do activate your GPS first to get your current location");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
                boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (!enabled) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, ENABLE_GPS);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                viewPager.setCurrentItem(0);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ENABLE_GPS) {
            if (resultCode == RESULT_CANCELED) {
                if (!isGPSEnabled()) {
                    enableGPS();
                } else {
                    getLocation();
                }
            } else {
                Log.d("gps","OK");
            }
        }
    }

    public void getLocation() {
        showProgressDialog("Branch Locator", "Getting your current location, Please wait...");
        try {
            Log.d("loc","1");
            locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
            /** getting GPS status */
            Log.d("loc","2");
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            /** getting network status */
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Log.d("loc","3");

            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.d("loc","4");
                enableGPS();
            } else {
                Log.d("loc","5");
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                    if (locationManager != null) {
                        Log.d("loc", "7");
                        dismissProgressDialog();
                        Log.d("loc","7a");
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location == null) {
                            Log.d("loc", "retry");
                            AlertDialog.Builder builder = new AlertDialog.Builder(BranchLocatorActivity.this);
                            builder.setMessage("Can't get your current location, Please try again");
                            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    getLocation();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    viewPager.setCurrentItem(0);
                                }
                            });
                            builder.show();

                        } else {
                            initLocation(location);
                            ((BranchesNearMeFragment)fragments.get(1)).loadBranchesNearMe();
                            Log.d("loc", "Network Enabled lat --> " + location.getLatitude() + "   long --> " + location.getLongitude());
                        }
                    } else {
                        Log.d("loc","8");
                    }
                } else if (isGPSEnabled) {
                    Log.d("loc","gps provider");
                    /** use GPS Provider */
                    if (location == null) {
                        Log.d("loc","5a");
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            dismissProgressDialog();
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            initLocation(location);
                            ((BranchesNearMeFragment)fragments.get(1)).loadBranchesNearMe();
                        }
                    } else {
                        Log.d("loc","7");
                    }
                }
            }
        } catch (Exception e) {
            Log.d("loc","error --> " + e.toString());
            dismissProgressDialog();
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        initLocation(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d("loc","bbbb --> " + s);
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("loc","ccc");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("loc","ddd");
    }

    private void initLocation(Location location) {
        AppConstants.MY_LOC_LATITUDE = location.getLatitude();
        AppConstants.MY_LOC_LONGITUDE = location.getLongitude();
        computeBranchesNearMe(AppConstants.MY_LOC_LATITUDE, AppConstants.MY_LOC_LONGITUDE);
    }
}
