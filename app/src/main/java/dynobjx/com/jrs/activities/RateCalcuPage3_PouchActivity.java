package dynobjx.com.jrs.activities;

import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.helpers.AppConstants;

public class RateCalcuPage3_PouchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_calcu_page3__pouch);
        ButterKnife.bind(this);
        initActionBar("Rate Calculator");
    }

    @OnClick(R.id.ll3Pounder)
    public void showNextPage3Pounder() {
        AppConstants.RATE_CALCU_SERVICE = 4;
        AppConstants.SERVICE_TYPE = "3 Pounder";
        startActivity(new Intent(this, RateCalcuFinalPageDomesticLightPackageActivity.class));
        animateToLeft(this);
    }

    @OnClick(R.id.ll5Pounder)
    public void showNextPage5Pounder() {
        AppConstants.RATE_CALCU_SERVICE = 6;
        AppConstants.SERVICE_TYPE = "5 Pounder";
        startActivity(new Intent(this, RateCalcuFinalPageDomesticLightPackageActivity.class));
        animateToLeft(this);
    }
}
