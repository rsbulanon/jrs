package dynobjx.com.jrs.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.activities.MainActivity;
import dynobjx.com.jrs.activities.TrackResultActivity;
import dynobjx.com.jrs.customviews.EditTextPlus;
import dynobjx.com.jrs.helpers.ApiRequestHelper;
import dynobjx.com.jrs.helpers.AppConstants;
import dynobjx.com.jrs.helpers.Prefs;
import dynobjx.com.jrs.models.Track;


public class TrackingFragment extends DialogFragment implements ApiRequestHelper.OnAPIRequestListener {

    @Bind(R.id.etORnumber) EditText etORnumber;
    @Bind(R.id.etBC) EditText etBC;
    private View view;
    private MainActivity activity;
    private ApiRequestHelper apiRequestHelper;
    private OnTrackingListener onTrackingListener;

    public static TrackingFragment newInstance() {
        TrackingFragment frag = new TrackingFragment();
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
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_tracking2, null);
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

    @OnClick(R.id.tvFind)
    public void showNextFind() {
        FullTrackingFragment helpFragment = FullTrackingFragment.newInstance();
        helpFragment.show(getActivity().getSupportFragmentManager(),"help");
    }

    @OnClick(R.id.btnTrack)
    public void showNextTrack() {
        String OR = etORnumber.getText().toString();
        String BC = etBC.getText().toString();

        if (OR.length() == 0) {
            etORnumber.setError("OR Number is required");
            etORnumber.requestFocus();
        } else if (BC.length() == 0) {
            etBC.setError("BC is required");
            etBC.requestFocus();
        } else if (etORnumber.getText().length() > 7) {
            Toast.makeText(view.getContext(), "Please enter no more than 7 characters for OR Number", Toast.LENGTH_LONG).show();
        } else if (etBC.getText().length() > 4) {
            Toast.makeText(view.getContext(), "Please enter no more than 4 characters for OR Number", Toast.LENGTH_LONG).show();
        } else {
            if (activity.isNetworkAvailable()) {
                apiRequestHelper.tracking(Integer.valueOf(OR),Integer.valueOf(BC), Prefs.getMyStringPrefs(getActivity(), AppConstants.ACCESS_TOKEN));
            } else {
                activity.showToast(AppConstants.ERR_CONNECTION);
            }
        }
    }

    @Override
    public void onRequestStart(String action) {
        activity.showProgressDialog("Tracking","Processing your request, Please wait..");
    }

    @Override
    public void onRequestException(Exception e) {
        activity.dismissProgressDialog();
        activity.showToast(AppConstants.ERR_TRACKING);
    }

    @Override
    public void onRequestSuccessful(String action, String json) {
        activity.dismissProgressDialog();
        Log.d("rr","json result --> " + json);
        onTrackingListener.onRequestSuccessful(json);
    }

    public interface OnTrackingListener {
        void onRequestSuccessful(String result);
    }

    public void setOnTrackingListener(OnTrackingListener onTrackingListener) {
        this.onTrackingListener = onTrackingListener;
    }
}
