package dynobjx.com.jrs.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.customviews.HashTagView;
import dynobjx.com.jrs.customviews.OverflowLayout;
import dynobjx.com.jrs.fragments.PickupDialogFragment;
import dynobjx.com.jrs.fragments.SelectPickUpLocationDialogFragment;
import dynobjx.com.jrs.helpers.ApiRequestHelper;
import dynobjx.com.jrs.helpers.AppConstants;
import dynobjx.com.jrs.helpers.DaoHelper;
import dynobjx.com.jrs.helpers.Prefs;

/**
 * Created by rsbulanon on 7/31/15.
 */
public class PickupActivity extends BaseActivity implements ApiRequestHelper.OnAPIRequestListener,
                                                            DatePickerDialog.OnDateSetListener,
                                                            TimePickerDialog.OnTimeSetListener {
    @Bind(R.id.tvPickUpDate) TextView tvPickupDate;
    @Bind(R.id.llPickUpLocation) LinearLayout llPickUpLocation;
    @Bind(R.id.llDestination) OverflowLayout llDestination;
    @Bind(R.id.tiAddress)TextInputLayout tiAddress;
    @Bind(R.id.etAddress) EditText etAddress;
    @Bind(R.id.etDescription) EditText etDescription;
    private int selectedBrgyId = 0;
    private Calendar pickupDate;
    private ApiRequestHelper apiRequestHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);
        ButterKnife.bind(this);
        apiRequestHelper = new ApiRequestHelper();
        apiRequestHelper.setOnAPIRequestListener(this);
        initActionBar("Pickup Request");
    }

    @OnClick(R.id.ivAddPickUp)
    public void addPickUpLocation() {
        final SelectPickUpLocationDialogFragment selectLocation = SelectPickUpLocationDialogFragment.newInstance();
        selectLocation.setOnSelectLocationListener(new SelectPickUpLocationDialogFragment.OnSelectLocationListener() {
            @Override
            public void onSelected(int brgyId) {
                selectLocation.dismiss();
                Log.d("pp", "on selected  --> " + brgyId);
                if (brgyId == 0) {
                    final PickupDialogFragment fragment = PickupDialogFragment.newInstance("origin");
                    fragment.setOnLocationListener(new PickupDialogFragment.OnLocationListener() {
                        @Override
                        public void onSelectedLocation(int brgyId, String location) {
                            addHashTagView(brgyId, location);
                            fragment.dismiss();
                        }
                    });
                    fragment.show(getSupportFragmentManager(), "pickup");
                } else {
                    if (selectedBrgyId != 0) {
                        llPickUpLocation.removeAllViews();
                        etAddress.setText("");
                    }
                    selectedBrgyId = brgyId;
                    addHashTagView(brgyId, DaoHelper.getUserInfo().getAddress());
                    tiAddress.setVisibility(View.GONE);
                    etAddress.setText("");
                }
            }
        });
        selectLocation.show(getSupportFragmentManager(), "select");
    }

    @OnClick(R.id.ivAddDesti)
    public void addDestinations() {
        final PickupDialogFragment fragment = PickupDialogFragment.newInstance("destination");
        fragment.setOnLocationListener(new PickupDialogFragment.OnLocationListener() {
            @Override
            public void onSelectedLocation(int brgyId, String location) {
                if (llDestination.getChildCount() > 0) {
                    boolean isExisting = false;
                    for (int i = 0; i < llDestination.getChildCount(); i++) {
                        if ((int) llDestination.getChildAt(i).getTag() == brgyId) {
                            isExisting = true;
                            break;
                        }
                    }
                    if (!isExisting) {
                        addDestination(brgyId, location);
                    } else {
                        showToast(AppConstants.WARNING_DESTI_EXISTS);
                    }
                } else {
                    addDestination(brgyId, location);
                }
                fragment.dismiss();
            }
        });
        fragment.show(getSupportFragmentManager(), "pickup");
    }

    @Override
    public void onRequestStart(String action) {
        if (action.equals(AppConstants.POST_PICKUP)) {
            showProgressDialog("Pickup Request", "Sending Pickup request, Please wait..");
        }
    }

    @Override
    public void onRequestException(Exception e) {
        dismissProgressDialog();
        if (e.toString().contains("java.net.SocketTimeoutException")) {
            showSweetDialog("Pickup Request",AppConstants.ERR_READ_TIMEOUT,"error");
        }
        Log.d("pickup", "ERROOR --> " + e.toString());
    }

    @Override
    public void onRequestSuccessful(String action, String result) {
        dismissProgressDialog();
        if (action.equals(AppConstants.POST_PICKUP)) {
            Log.d("pickup", "RESULT --> " + result);
            if (result.equals("true")) {
                showSweetDialog("Pickup Request", "Pickup request successfully delivered!", "success");
                tvPickupDate.setText("Select Pickup Date");
                llDestination.removeAllViews();
                llPickUpLocation.removeAllViews();
                selectedBrgyId = 0;
                etDescription.setText("");
            }
        }
    }

    @OnClick(R.id.tvPickUpDate)
    public void selectPickupDate() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        datePickerDialog.setYearRange(2000, 2030);
        datePickerDialog.setCloseOnSingleTapDay(true);
        datePickerDialog.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        pickupDate = Calendar.getInstance();
        Date now = pickupDate.getTime();
        pickupDate.set(year, month, day);

        if (pickupDate.getTime().after(now) || pickupDate.getTime().equals(now)) {
            Log.d("pickup","valid");
            TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this,
                    pickupDate.get(Calendar.HOUR_OF_DAY), pickupDate.get(Calendar.MINUTE), false, false);
            timePickerDialog.setCloseOnSingleTapMinute(true);
            timePickerDialog.show(getSupportFragmentManager(), "timepicker");
        } else {
            showToast(AppConstants.ERR_INVALID_PU_DATE);
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int min) {
        pickupDate.set(Calendar.HOUR_OF_DAY, hour);
        pickupDate.set(Calendar.MINUTE, min);
        if (pickupDate.getTime().after(Calendar.getInstance().getTime())) {
            tvPickupDate.setText(getPickupDateFormat().format(pickupDate.getTime()));
        } else {
            showToast(AppConstants.ERR_INVALID_PU_TIME  );
        }
    }

    private void addDestination(int brgyId,String location ) {
        HashTagView hashTagView = new HashTagView(this,location);
        hashTagView.setTag(brgyId);
        llDestination.addView(hashTagView);
        hashTagView.tvCloseHashTag().setTag(brgyId);
        hashTagView.tvCloseHashTag().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llDestination.removeView(llDestination.findViewWithTag(view.getTag()));
            }
        });
    }

    private void addHashTagView(int brgyId, String location) {
        if (selectedBrgyId != 0) {
            llPickUpLocation.removeAllViews();
            etAddress.setText("");
        }
        selectedBrgyId = brgyId;
        HashTagView hashTagView = new HashTagView(PickupActivity.this, location);
        tiAddress.setVisibility(View.VISIBLE);
        etAddress.requestFocus();
        llPickUpLocation.addView(hashTagView);
        hashTagView.tvCloseHashTag().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedBrgyId = 0;
                llPickUpLocation.removeAllViews();
                etAddress.setText("");
                tiAddress.setVisibility(View.GONE);
            }
        });
    }

    @OnClick(R.id.btnSendRequest)
    public void sendPickupRequest() {
        if (isNetworkAvailable()) {
            if (selectedBrgyId == 0) {
                showSweetDialog("Pickup Request",AppConstants.WARNING_ORIGIN,"warning");
            } else if (etAddress.isShown() && etAddress.getText().toString().isEmpty()) {
                showSweetDialog("Pickup Request",AppConstants.WARNING_ST_ADDRESS,"warning");
                etAddress.setError("This field is required");
                etAddress.requestFocus();
            } else if (llDestination.getChildCount() == 0) {
                showSweetDialog("Pickup Request",AppConstants.WARNING_DESTI_COUNT,"warning");
            } else if (tvPickupDate.getText().toString().equals("Select Pickup Date")) {
                showSweetDialog("Pickup Request",AppConstants.ERR_INVALID_PU_DATE,"warning");
            } else {
                if (isNetworkAvailable()) {
                    String description = etDescription.getText().toString();
                    if (llDestination.getChildCount() > 1) {
                        description += "\n";
                        for (int i = 0 ; i < llDestination.getChildCount(); i++) {
                            description += (i != 0 ? "," : "") + llDestination.getChildAt(i).getTag().toString();
                        }
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
                    Log.d("pickup","brgy id --> " + selectedBrgyId);
                    Log.d("pickup","address --> " + etAddress.getText().toString());
                    Log.d("pickup","token --> " + Prefs.getMyStringPrefs(this, AppConstants.ACCESS_TOKEN));
                    Log.d("pickup", "username --> " + Prefs.getMyStringPrefs(this, AppConstants.USERNAME));
                    Log.d("pickup", "pickup date --> " + sdf.format(pickupDate.getTime()));
                    Log.d("pickup", "desc --> " + description);

                    apiRequestHelper.sendPickupRequest(selectedBrgyId,
                            etAddress.getText().toString().isEmpty() ?
                                    DaoHelper.getUserInfo().getAddress() :
                                    etAddress.getText().toString(),
                            sdf.format(pickupDate.getTime()),
                            etDescription.getText().toString(),
                            Prefs.getMyStringPrefs(this,AppConstants.USERNAME),
                            Prefs.getMyStringPrefs(this,AppConstants.ACCESS_TOKEN));
                } else {
                    showToast(AppConstants.ERR_CONNECTION);
                }
            }
        } else {
            showToast(AppConstants.ERR_CONNECTION);
        }
    }
}
