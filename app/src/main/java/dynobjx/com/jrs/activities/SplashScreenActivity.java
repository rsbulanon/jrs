package dynobjx.com.jrs.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dynobjx.com.jrs.R;
import dynobjx.com.jrs.helpers.ApiRequestHelper;
import dynobjx.com.jrs.models.Province;

public class SplashScreenActivity extends BaseActivity implements ApiRequestHelper.OnAPIRequestListener {

    ApiRequestHelper apiRequestHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread background = new Thread() {
            public void run() {

                try {
                    sleep(3 * 1000);

//                    apiRequestHelper = new ApiRequestHelper();
//                    apiRequestHelper.setOnAPIRequestListener(SplashScreenActivity.this);
//                    apiRequestHelper.getProvince();

                    Intent i=new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(i);
                    animateToLeft(SplashScreenActivity.this);
                    finish();
                } catch (Exception e) {
                }
            }
        };

        background.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onRequestStart(String action) {

    }

    @Override
    public void onRequestException(Exception e) {

    }

    @Override
    public void onRequestSuccessful(String action, String body) {
        Log.d("Province", body);

        try {
            JSONArray arr = new JSONArray(body);
            for (int i = 0 ; i < arr.length() ; i++) {

                JSONObject obj = arr.getJSONObject(i);
                //String sender = obj.getInt("Id").equals("null") ? NOT_AVAILABLE : obj.getString("Sender");
                int provId = obj.getInt("Id");
                String provName = obj.getString("name");
                getProvince().add(new Province(provId, provName));
            }

        } catch (JSONException ex) {
            Log.d("error", ex.toString());
        }

    }
}
