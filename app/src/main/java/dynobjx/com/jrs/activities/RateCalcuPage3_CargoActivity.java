package dynobjx.com.jrs.activities;

import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.helpers.AppConstants;

/**
 * Created by jobelle on 7/10/15.
 */
public class RateCalcuPage3_CargoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_calcu_page_3_cargo);
        ButterKnife.bind(this);
        initActionBar("Rate Calculator");
    }

    @OnClick(R.id.llExpressCargo)
    public void showNextPageExpressCargo() {
        AppConstants.RATE_CALCU_SERVICE = 10;
        AppConstants.SERVICE_TYPE = "Express Cargo";
        startActivity(new Intent(this, RateCalcuFinalPageDomesticHeavyPackageActivity.class));
        animateToLeft(this);
    }

    @OnClick(R.id.llGeneralCargo)
    public void showNextPageGeneralCargo() {
        AppConstants.RATE_CALCU_SERVICE = 25;
        AppConstants.SERVICE_TYPE = "General Cargo";
        startActivity(new Intent(this, RateCalcuFinalPageDomesticHeavyPackageActivity.class));
        animateToLeft(this);
    }

}
