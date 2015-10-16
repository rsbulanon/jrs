package dynobjx.com.jrs.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.customviews.EditTextPlus;
import dynobjx.com.jrs.helpers.ApiRequestHelper;
import dynobjx.com.jrs.helpers.AppConstants;
import dynobjx.com.jrs.helpers.Prefs;


public class TrackingActivity extends BaseActivity implements ApiRequestHelper.OnAPIRequestListener{
    @Bind(R.id.etORNumber)
    EditTextPlus ORNumber;
    @Bind(R.id.etBC) EditTextPlus BC;
    String NOT_AVAILABLE = "Not available";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        ButterKnife.bind(this);
        initActionBar("Tracking");
    }

    @OnClick(R.id.btnTrack)
    public void showNextTrack() {

        if(ORNumber.getText().toString().length() == 0) {
            ORNumber.setError("OR Number is required");
        }
        else if (BC.getText().toString().length() == 0) {
            BC.setError("BC is required");
        }
        else {
            ApiRequestHelper helper = new ApiRequestHelper();
            helper.setOnAPIRequestListener(this);
            helper.tracking(Integer.parseInt(ORNumber.getText().toString()), Integer.parseInt(BC.getText().toString()), Prefs.getMyStringPrefs(this, AppConstants.ACCESS_TOKEN));

            if(isNetworkAvailable()) {
                startActivity(new Intent(this, TrackResultActivity.class));
                animateToLeft(this);
            }
                else {
                Toast.makeText(this, AppConstants.ERR_CONNECTION, Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onRequestStart(String action) {

    }

    @Override
    public void onRequestException(Exception e) {

    }

    @Override
    public void onRequestSuccessful(String action, String body) {
        getTrackings().clear();
    }

}
