package dynobjx.com.jrs.activities;

import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dynobjx.com.jrs.R;

/**
 * Created by rsbulanon on 7/9/15.
 */
public class RateCalcuPage2DomesticActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_calcu_page_2);
        ButterKnife.bind(this);
        initActionBar("Rate Calculator");

    }
    @OnClick(R.id.llMails)
    public void showNextPage() {
        //nextStepIntent("0");
        startActivity(new Intent(this,RateCalcuPage3_MailActivity.class));
        animateToLeft(this);
    }
    @OnClick(R.id.llCargo)
    public void showNextPageCargo() {
        startActivity(new Intent(this,RateCalcuPage3_CargoActivity.class));
        animateToLeft(this);
    }@OnClick(R.id.llBox)
    public void showNextPageBox() {
        startActivity(new Intent(this,RateCalcuPage3_BoxActivity.class));
        animateToLeft(this);
    }@OnClick(R.id.llPackages)
    public void showNextPagePouch() {
        startActivity(new Intent(this,RateCalcuPage3_PouchActivity.class));
        animateToLeft(this);
    }



}
