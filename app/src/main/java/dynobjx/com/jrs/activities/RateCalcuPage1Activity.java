package dynobjx.com.jrs.activities;

import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.helpers.AppConstants;

/**
 * Created by rsbulanon on 7/9/15.
 */
public class RateCalcuPage1Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_calcu_page_1);
        ButterKnife.bind(this);
        initActionBar("Rate Calculator");
    }

    @OnClick(R.id.llDomestic)
    public void showNextPageDomestic() {
        Intent intent = new Intent(getBaseContext(), RateCalcuPage2DomesticActivity.class);
        AppConstants.SCOPE_OF_DELIVERY = "Domestic";
        AppConstants.RATE_CALCU_SERVICE = 1;
        startActivity(intent);
        animateToLeft(this);
    }

    @OnClick(R.id.llInternational)
    public void showNextPageInternational() {
        Intent intent = new Intent(getBaseContext(), RateCalcuPage2InternationalActivity.class);
        intent.putExtra("scope", 1);
        AppConstants.SCOPE_OF_DELIVERY = "International";
        AppConstants.RATE_CALCU_SERVICE = 3;
        startActivity(intent);
        animateToLeft(this);
    }
}
