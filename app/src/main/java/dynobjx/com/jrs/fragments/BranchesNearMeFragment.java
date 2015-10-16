package dynobjx.com.jrs.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.navdrawer.SimpleSideDrawer;

import butterknife.Bind;
import butterknife.ButterKnife;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.activities.BranchDetailsActivity;
import dynobjx.com.jrs.activities.BranchLocatorActivity;
import dynobjx.com.jrs.dao.Branch;
import dynobjx.com.jrs.helpers.AppConstants;

/**
 * Created by rsbulanon on 7/9/15.
 */
public class BranchesNearMeFragment extends Fragment {

    private SupportMapFragment googleMap;
    private BranchLocatorActivity activity;
    private SimpleSideDrawer mDrawer;
    @Bind(R.id.tvBranchName) TextView tvBranchName;
    @Bind(R.id.tvBranchAddress) TextView tvBranchAddress;
    @Bind(R.id.tvContactPerson) TextView tvContactPerson;
    @Bind(R.id.tvContactNos) TextView tvContactNos;

    public static BranchesNearMeFragment newInstance() {
        return new BranchesNearMeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_branches_near_me, null, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        activity = (BranchLocatorActivity)getActivity();
        initBranchPreview();
        googleMap = (SupportMapFragment)fm.findFragmentById(R.id.mapContainer);
        if (googleMap == null) {
            Log.d("map","PUMASOK DTO");
            googleMap = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.mapContainer, googleMap).commit();
        } else {
            Log.d("map","ELLLLLLLLLLSEEEE");
        }
    }

    private void initBranchPreview() {
        mDrawer = new SimpleSideDrawer(getActivity());
        mDrawer.setRightBehindContentView(R.layout.branch_preview);
        ButterKnife.bind(this,mDrawer);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("map", "google map ready");
        if (googleMap.getMap() == null) {
            Log.d("map","NULL PA DIN");
        } else {
            Log.d("map","MAP NOT NULL");
        }
        googleMap.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.getUiSettings().setScrollGesturesEnabled(true);
            }
        });
    }

    /** add marker for branches near me */
    public void loadBranchesNearMe() {
        LatLng latLng = new LatLng(AppConstants.MY_LOC_LATITUDE, AppConstants.MY_LOC_LONGITUDE);
        googleMap.getMap().moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.getMap().animateCamera(CameraUpdateFactory.zoomTo(14));
        activity.addMapMarker(googleMap.getMap(), AppConstants.MY_LOC_LATITUDE, AppConstants.MY_LOC_LONGITUDE,
                "You are here", "", -1, false);

        for (Branch b : activity.getBranchesNearMe()) {
            activity.addMapMarker(googleMap.getMap(), b.getLatitude(), b.getLongitude(),
                    b.getBranchName(), "",R.drawable.map_icon, true);
        }
        googleMap.getMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (!marker.getTitle().equals("You are here")) {
                    Branch branch = activity.getBranchByName(marker.getTitle());
                    Intent i = new Intent(activity, BranchDetailsActivity.class);
                    i.putExtra("branchName",branch.getBranchName());
                    i.putExtra("branchId",branch.getBranchId());
                    activity.startActivity(i);
                    activity.animateToLeft(activity);

/*                    tvBranchName.setText(branch.getBranchName());
                    tvBranchAddress.setText(branch.getBranchAddress());
                    tvContactPerson.setText(branch.getContactPerson());
                    tvContactNos.setText(branch.getContactNumbers());
                    mDrawer.toggleRightDrawer();*/
                }
                return false;
            }
        });
    }
}
