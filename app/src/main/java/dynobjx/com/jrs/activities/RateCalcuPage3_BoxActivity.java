package dynobjx.com.jrs.activities;

import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.helpers.AppConstants;

public class RateCalcuPage3_BoxActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_calcu_page3__box);
        ButterKnife.bind(this);
        initActionBar("Rate Calculator");
    }

    @OnClick(R.id.llExtraSmallBox)
    public void showNextPageExtraSmallBox() {
        AppConstants.RATE_CALCU_SERVICE = 42;
        AppConstants.SERVICE_TYPE = "Extra Small Box";
        getHeightWidthAndLength(AppConstants.extraSmallBox_height, AppConstants.extraSmallBox_width, AppConstants.extraSmallBox_length);
    }

    @OnClick(R.id.llExpressSmallBox)
    public void showNextExpressSmallBox() {
        AppConstants.RATE_CALCU_SERVICE = 21;
        AppConstants.SERVICE_TYPE = "Express Small Box";
        getHeightWidthAndLength(AppConstants.expressSmallBox_height, AppConstants.expressSmallBox_width, AppConstants.expressSmallBox_length);

    }

    @OnClick(R.id.llExpressBoxMedium)
    public void showNextExpressMediumBox() {
        AppConstants.RATE_CALCU_SERVICE = 23;
        AppConstants.SERVICE_TYPE = "Extra Medium Box";
        getHeightWidthAndLength(AppConstants.expressMediumBox_height, AppConstants.expressMediumBox_width, AppConstants.expressMediumBox_length);
    }

    @OnClick(R.id.llExpressBoxLarge)
    public void showNextExpressLargeBox() {
        AppConstants.RATE_CALCU_SERVICE = 24;
        AppConstants.SERVICE_TYPE = "Extra Large Box";
        getHeightWidthAndLength(AppConstants.expressLargeBox_height, AppConstants.expressLargeBox_width, AppConstants.expressLargeBox_length);
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
