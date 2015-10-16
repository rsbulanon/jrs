package dynobjx.com.jrs.fragments;

import android.app.Dialog;
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
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.activities.PickupActivity;
import dynobjx.com.jrs.dao.UserInfo;
import dynobjx.com.jrs.helpers.ApiRequestHelper;
import dynobjx.com.jrs.helpers.AppConstants;
import dynobjx.com.jrs.helpers.DaoHelper;
import dynobjx.com.jrs.helpers.Prefs;

/**
 * Created by rsbulanon on 8/5/15.
 */
public class SelectPickUpLocationDialogFragment extends DialogFragment implements
                                                                    ApiRequestHelper.OnAPIRequestListener {

    private View view;
    private PickupActivity activity;
    private OnSelectLocationListener onSelectLocationListener;
    private ApiRequestHelper apiRequestHelper;
    private int selectedBrgyId = 0;

    public static SelectPickUpLocationDialogFragment newInstance() {
        SelectPickUpLocationDialogFragment frag = new SelectPickUpLocationDialogFragment();
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
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_select_location, null);
        ButterKnife.bind(this, view);
        activity = (PickupActivity)getActivity();
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

    @OnClick(R.id.tvUseMyAddress)
    public void useMyAddress() {
        UserInfo userInfo = DaoHelper.getUserInfo();
        if (userInfo.getBrgyId() == 0) {
            activity.showToast(AppConstants.ERR_ADDRESS_NOT_SET);
        } else {
            if (activity.isNetworkAvailable()) {
                selectedBrgyId = userInfo.getBrgyId();
                apiRequestHelper.validatePickupLocation(selectedBrgyId, Prefs.getMyStringPrefs(getActivity(), AppConstants.ACCESS_TOKEN));
            } else {
                activity.showSweetDialog("Validate Location","Please turn on your WiFi/mobile data" +
                        " connection to validate if your location is covered by our service","warning");
            }
        }
    }

    @OnClick(R.id.tvSelectOther)
    public void selectOtherAddress() {
        onSelectLocationListener.onSelected(0);
    }

    public interface OnSelectLocationListener {
        void onSelected(int brgyId);
    }

    public void setOnSelectLocationListener(OnSelectLocationListener onSelectLocationListener) {
        this.onSelectLocationListener = onSelectLocationListener;
    }

    @Override
    public void onRequestStart(String action) {
        activity.showProgressDialog("Validate Location","Validating pickup location, Please wait...");
    }

    @Override
    public void onRequestException(Exception e) {
        activity.dismissProgressDialog();
        Log.d("err", "error in validating pickup location --> " + e.toString());
    }

    @Override
    public void onRequestSuccessful(String action, String result) {
        activity.dismissProgressDialog();
        Log.d("pp","result --> " + result);
        if (action.equals(AppConstants.GET_VALIDATE_PU_LOC)) {
            if (result.contains("Ok")) {
                onSelectLocationListener.onSelected(selectedBrgyId);
            } else if (result.contains("Pickup is out of range")) {
                activity.showSweetDialog("Pickup Request", "Your current address is not covered by our pickup service", "warning");
            } else if (result.contains("Google API could not locate your address")) {
                activity.showSweetDialog("Pickup Request", "Our service could not locate your pickup location", "error");
            }
        }
    }
}
