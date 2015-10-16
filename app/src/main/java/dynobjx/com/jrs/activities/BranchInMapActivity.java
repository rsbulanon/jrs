package dynobjx.com.jrs.activities;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import dynobjx.com.jrs.R;

/**
 * Created by rsbulanon on 8/3/15.
 */
public class BranchInMapActivity extends BaseActivity {

    private SupportMapFragment googleMap;
    private double latitude;
    private double longitude;
    private String branchName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_branches_near_me);
        latitude = getIntent().getDoubleExtra("latitude",0);
        longitude = getIntent().getDoubleExtra("longitude",0);
        branchName = getIntent().getStringExtra("branchName");
        initActionBar("JRS " + branchName);
        googleMap = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapContainer);
        if (googleMap == null) {
            googleMap = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.mapContainer, googleMap).commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("map", "google map ready");
        googleMap.getMap().setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getMap().getUiSettings().setScrollGesturesEnabled(true);
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.getMap().moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.getMap().animateCamera(CameraUpdateFactory.zoomTo(14));
        addMapMarker(googleMap.getMap(), latitude, longitude,branchName, "", -1, false);
    }
}
