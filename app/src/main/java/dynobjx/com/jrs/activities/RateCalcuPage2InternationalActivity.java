package dynobjx.com.jrs.activities;

import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.helpers.AppConstants;

public class RateCalcuPage2InternationalActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_calcu_page2_international);
        ButterKnife.bind(this);
        initActionBar("Rate Calculator");
    }

    @OnClick(R.id.llDocuments)
    public void showNextPageDocuments() {
        AppConstants.RATE_CALCU_SERVICE = 12;
        AppConstants.SERVICE_TYPE = "Documents";
        Intent intent = new Intent (this, RateCalcuFinalPageInternationalActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("service", "12");
        startActivity(intent);
        animateToLeft(this);
    }

    @OnClick(R.id.llParcel)
    public void showNextPageParcel() {
        AppConstants.RATE_CALCU_SERVICE = 11;
        AppConstants.SERVICE_TYPE = "Parcel";
        startActivity(new Intent(this, RateCalcuFinalPageInternationalActivity.class));
        animateToLeft(this);
    }


}
