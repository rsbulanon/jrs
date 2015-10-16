package dynobjx.com.jrs.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.activities.MainActivity;
import dynobjx.com.jrs.helpers.ApiRequestHelper;
import dynobjx.com.jrs.helpers.AppConstants;
import dynobjx.com.jrs.helpers.DaoHelper;
import dynobjx.com.jrs.helpers.Prefs;

/**
 * Created by rsbulanon on 8/5/15.
 */
public class ChangeMobileNoDialogFragment extends DialogFragment implements ApiRequestHelper.OnAPIRequestListener {

    @Bind(R.id.etMobileNo) EditText etMobileNo;
    @Bind(R.id.tvCurrNo) TextView tvCurrNo;
    private View view;
    private ApiRequestHelper apiRequestHelper;
    private OnChangeMobileNoListener onChangeMobileNoListener;
    private MainActivity activity;

    public static ChangeMobileNoDialogFragment newInstance() {
        ChangeMobileNoDialogFragment frag = new ChangeMobileNoDialogFragment();
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
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_change_mobile_no, null);
        ButterKnife.bind(this, view);
        activity = (MainActivity)getActivity();
        tvCurrNo.setText(DaoHelper.getUserInfo().getPhone());
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

    @OnClick(R.id.tvChange)
    public void changeMobileNo() {
        if (etMobileNo.getText().toString().isEmpty()) {
            etMobileNo.setError(AppConstants.WARNING_FIELD_REQUIRED);
            etMobileNo.requestFocus();
        } else {
            if (activity.isNetworkAvailable()) {
                apiRequestHelper.changePhoneNumber(etMobileNo.getText().toString(),
                        Prefs.getMyStringPrefs(getActivity(),AppConstants.ACCESS_TOKEN));
            } else {
                activity.showToast(AppConstants.ERR_CONNECTION);
            }
        }
    }

    @Override
    public void onRequestStart(String action) {
        activity.showProgressDialog("Change Mobile No","Changing your mobile no, Please wait...");
    }

    @Override
    public void onRequestException(Exception e) {
        activity.dismissProgressDialog();
        onChangeMobileNoListener.onChangedFailed(e);
    }

    @Override
    public void onRequestSuccessful(String action, String body) {
        activity.dismissProgressDialog();
        onChangeMobileNoListener.onChangedSuccessful(body,etMobileNo.getText().toString());
    }

    public interface OnChangeMobileNoListener {
        void onChangedSuccessful(String result, String newNumber);
        void onChangedFailed(Exception e);
    }

    public void setOnChangeMobileNoListener(OnChangeMobileNoListener onChangeMobileNoListener) {
        this.onChangeMobileNoListener = onChangeMobileNoListener;
    }
}
