package dynobjx.com.jrs.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.helpers.ApiRequestHelper;
import dynobjx.com.jrs.helpers.AppConstants;
import dynobjx.com.jrs.helpers.Prefs;


public class RateCalcuSummaryActivity extends BaseActivity implements ApiRequestHelper.OnAPIRequestListener{

    @Bind(R.id.tvService) TextView tvService;
    @Bind(R.id.tvType) TextView tvType;
    @Bind(R.id.tvFrom) TextView tvFrom;
    @Bind(R.id.tvTo) TextView tvTo;
    @Bind(R.id.tvWeight) TextView tvWeight;
    @Bind(R.id.tvValue) TextView tvValue;
    @Bind(R.id.tvFreight) TextView tvFreight;
    @Bind(R.id.tvExcess) TextView tvExcess;
    @Bind(R.id.tvInsurance) TextView tvInsurance;
    @Bind(R.id.tvValuation) TextView tvValuation;
    @Bind(R.id.tvTotal) TextView tvTotal;
    private int origin;
    private int destination;
    private boolean valuation;
    private boolean insurance;
    private double declaredValue;
    private double weight;
    private double height;
    private double length;
    private double width;
    private String origin_string;
    private String destination_string;
    private String packageType;
    private double service;

    private Double Freight;
    private Double ExcessCharge;
    private Double Valuation;
    private Double Insurance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_calculator_summary);
        ButterKnife.bind(this);
        initActionBar("Rate Calculator");

        showProgressDialog("Rate Calculator", "Calculating, Please wait ...");

        Intent intent = getIntent();
        origin =intent.getIntExtra("Origin", 0);
        destination= intent.getIntExtra("Destination", 0);
        valuation = intent.getBooleanExtra("Valuation", false);
        insurance = intent.getBooleanExtra("Insurance", false);
        declaredValue = intent.getDoubleExtra("DeclaredValue", 0.0);
        weight = intent.getDoubleExtra("Weight", 0.0);
        packageType = intent.getStringExtra("PackageType");
        height = intent.getDoubleExtra("Height", 0.0);
        length = intent.getDoubleExtra("Length", 0.0);
        width = intent.getDoubleExtra("Width", 0.0);
        origin_string = intent.getStringExtra("OriginString");
        destination_string = intent.getStringExtra("DestString");

        Log.d("RATE", ""+origin);
        Log.d("RATE", ""+destination);
        Log.d("RATE", ""+valuation);
        Log.d("RATE", ""+insurance);
        Log.d("RATE", ""+declaredValue);
        Log.d("RATE", ""+weight);
        Log.d("RATE", ""+packageType);
        Log.d("RATE", ""+height);
        Log.d("RATE", ""+length);
        Log.d("RATE", ""+width);
        Log.d("RATE", ""+origin_string);
        Log.d("RATE", ""+destination_string);

        ApiRequestHelper helper = new ApiRequestHelper();
        helper.setOnAPIRequestListener(this);
        helper.getRateCalcu(origin, destination, valuation, insurance, declaredValue, weight, length,width,height, Prefs.getMyStringPrefs(this, AppConstants.ACCESS_TOKEN));
    }

    @OnClick (R.id.tvGoBack)
    public void showNextpage() {
        startActivity(new Intent(this, RateCalcuPage1Activity.class));
        animateToBottom(this);
    }

    @Override
    public void onRequestStart(String action) {

    }

    @Override
    public void onRequestException(Exception e) {

    }

    @Override
    public void onRequestSuccessful(String action, String body) {
        dismissProgressDialog();
        Log.d("RATE", body);

        String messageError = "{\"Message\":\"An error has occurred.\"}";
        Log.d("message", messageError);

        if (body.equals(messageError) || body.equals("{}") || body.equals("[]")) {
                showSweetDialog("Error","Sorry an error has occurred" + "\n Computation cancelled", "error");
        } else {
            try {
                JSONObject arr = new JSONObject(body);
                for (int i = 0 ; i < arr.length() ; i++) {
                    Freight = arr.getDouble("Freight");
                    ExcessCharge = arr.getDouble("ExcessCharge");
                    Valuation = arr.getDouble("Valuataion");
                    Insurance = arr.getDouble("Insurance");
                }

            } catch (JSONException ex) {
                dismissProgressDialog();
                Log.d("error", ex.toString());
            }

            dismissProgressDialog();

            Log.d("RATE", "Freight -->"+Freight);
            Log.d("RATE", "ExcessCharge -->" + ExcessCharge);
            Log.d("RATE", "Valuation -->"+Valuation);
            Log.d("RATE", "Insurance -->" + Insurance);

            Double totalValue = Freight + ExcessCharge + Insurance + Valuation;

            tvService.setText(AppConstants.SERVICE_TYPE);
            tvType.setText(AppConstants.SCOPE_OF_DELIVERY);
            tvFrom.setText(origin_string);
            tvTo.setText(destination_string);
            tvWeight.setText((weight == 0 ? "0.00" : getDecFormat().format(weight))
                    + (AppConstants.SERVICE_TYPE.contains("Cargo") ? " kilo/s" : " gram/s"));
            tvExcess.setText((ExcessCharge == 0 ? "0.00" : getDecFormat().format(ExcessCharge)));
            tvFreight.setText((Freight == 0 ? "0.00" : getDecFormat().format(Freight)));
            tvInsurance.setText((Insurance) == 0 ? "0.00" : getDecFormat().format(Insurance));
            tvValuation.setText((Valuation) == 0 ? "0.00" : getDecFormat().format(Valuation));
            tvValue.setText((declaredValue) == 0 ? "0.00" : getDecFormat().format(declaredValue));
            tvTotal.setText((totalValue) == 0 ? "0.00" : getDecFormat().format(totalValue));
        }


    }
}
