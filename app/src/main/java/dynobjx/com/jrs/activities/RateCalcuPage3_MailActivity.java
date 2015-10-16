package dynobjx.com.jrs.activities;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.helpers.AppConstants;
import dynobjx.com.jrs.models.RateCalculator;

/**
 * Created by rsbulanon on 7/9/15.
 */
public class RateCalcuPage3_MailActivity extends BaseActivity {

    public static ArrayList<RateCalculator> ratecalculator = new ArrayList<>();
    private RateCalculator rateCalculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_calcu_page_3);
        ButterKnife.bind(this);
        initActionBar("Rate Calculator");
    }

    @OnClick(R.id.llExpressLetter)
    public void showNextPageExpress() {
        AppConstants.RATE_CALCU_SERVICE = 2;
        AppConstants.SERVICE_TYPE = "Express Letter";
        getHeightWidthAndLength(AppConstants.expressLetter_height, AppConstants.expressLetter_width,AppConstants.expressLetter_length);
    }

    @OnClick(R.id.llOnePounder)
    public void showNextPageOnePounder() {
        AppConstants.RATE_CALCU_SERVICE = 3;
        AppConstants.SERVICE_TYPE = "One Pounder";
        getHeightWidthAndLength(AppConstants.onePounder_height, AppConstants.onePounder_width,AppConstants.onePounder_length);
    }

    @OnClick(R.id.llRegLetter)
    public void showNextPageRegularLetter() {
        AppConstants.RATE_CALCU_SERVICE = 8;
        AppConstants.SERVICE_TYPE = "Regular Letter";
        getHeightWidthAndLength(AppConstants.regularLetter_height,AppConstants.regularLetter_width, AppConstants.regularLetter_length);
    }

    @OnClick(R.id.llBrownEnve)
    public void showNextPageBrownEnvelope() {
        AppConstants.RATE_CALCU_SERVICE = 26;
        AppConstants.SERVICE_TYPE = "Brown Envelope";
        getHeightWidthAndLength(AppConstants.brownEvelope_height, AppConstants.brownEvelope_width, AppConstants.brownEvelope_length);
    }

    public void getHeightWidthAndLength(Double height, Double width, Double length) {
        //Log.d("SCOPE", value);
        Intent in = new Intent(this, RateCalcuFinalPageDomesticLightPackageActivity.class);
        in.putExtra("packageHeight", height);
        in.putExtra("packageLength", length);
        in.putExtra("packageWidth", width);
        startActivity(in);
        animateToLeft(this);
    }
}