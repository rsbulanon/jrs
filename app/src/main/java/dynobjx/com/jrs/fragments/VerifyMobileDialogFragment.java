package dynobjx.com.jrs.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.activities.LoginActivity;
import dynobjx.com.jrs.activities.MainActivity;
import dynobjx.com.jrs.customviews.TextViewPlus;
import dynobjx.com.jrs.helpers.ApiRequestHelper;
import dynobjx.com.jrs.helpers.AppConstants;
import dynobjx.com.jrs.helpers.DaoHelper;
import dynobjx.com.jrs.helpers.Prefs;

/**
 * Created by rsbulanon on 8/5/15.
 */
public class VerifyMobileDialogFragment extends DialogFragment implements ApiRequestHelper.OnAPIRequestListener {

    @Bind(R.id.etMobileNo) EditText etMobileNo;
    @Bind(R.id.etCode) EditText etCode;
    private View view;
    private MainActivity activity;
    private ApiRequestHelper apiRequestHelper;
    private OnVerificationListener onVerificationListener;

    public static VerifyMobileDialogFragment newInstance() {
        VerifyMobileDialogFragment frag = new VerifyMobileDialogFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_verify_mobile, null);
        ButterKnife.bind(this, view);
        activity = (MainActivity)getActivity();
        apiRequestHelper = new ApiRequestHelper();
        apiRequestHelper.setOnAPIRequestListener(this);
        final Dialog mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        return mDialog;
    }



    @OnClick(R.id.btnVerify)
    public void verifyCode() {
        if (etMobileNo.getText().toString().isEmpty()) {
            etMobileNo.setError(AppConstants.WARNING_FIELD_REQUIRED);
            etMobileNo.requestFocus();
        } else if (etCode.getText().toString().isEmpty()) {
            etCode.setError(AppConstants.WARNING_FIELD_REQUIRED);
            etCode.requestFocus();
        } else if (!activity.isNetworkAvailable()) {
            activity.showToast(AppConstants.ERR_CONNECTION);
        } else {
            apiRequestHelper.verifyPhoneNumber(etMobileNo.getText().toString(),
                    etCode.getText().toString(),
                    Prefs.getMyStringPrefs(getActivity(),AppConstants.ACCESS_TOKEN));
        }
    }

    @OnClick(R.id.tvLogout)
    public void logOut() {
        SweetAlertDialog sw  = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        sw.setTitleText("Logout");
        sw.setContentText("Are you sure you want to exit from the app?");
        sw.setConfirmText("Yes");
        sw.setCancelText("No");
        sw.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        Prefs.setMyStringPref(activity, AppConstants.ACCESS_TOKEN, "");
                        Prefs.setMyStringPref(activity,AppConstants.ACCESS_TOKEN_EXPIRY,"");
                        Prefs.setMyStringPref(activity,AppConstants.USERNAME,"");
                        DaoHelper.clearUserInfo();
                        activity.showToast("You've been logged out successfully!");
                        startActivity(new Intent(activity,LoginActivity.class));
                        activity.animateToRight(activity);
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onRequestStart(String action) {
        activity.showProgressDialog("Verification","Verifying your mobile no, Please wait...");
    }

    @Override
    public void onRequestException(Exception e) {
        activity.dismissProgressDialog();
        onVerificationListener.onVerificationFailed(e);
    }

    @Override
    public void onRequestSuccessful(String action, String result) {
        Log.d("verify ", "" + result);

        activity.dismissProgressDialog();
        if(result.equals(AppConstants.VERYFICATION_FAILED)) {
            activity.showSweetDialog("Failed", "Invalid code/phone number", "error");
        } else {
            onVerificationListener.onVerificationSuccessful(result);
        }
    }

    public interface OnVerificationListener {
        void onVerificationSuccessful(String result);
        void onVerificationFailed(Exception e);
    }

    public void setOnVerificationListener(OnVerificationListener onVerificationListener) {
        this.onVerificationListener = onVerificationListener;
    }
}
