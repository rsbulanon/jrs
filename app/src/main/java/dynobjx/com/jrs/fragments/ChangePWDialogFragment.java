package dynobjx.com.jrs.fragments;

import android.app.Dialog;
import android.content.Intent;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.activities.TrackResultActivity;
import dynobjx.com.jrs.customviews.EditTextPlus;

/**
 * Created by rsbulanon on 7/31/15.
 */
public class ChangePWDialogFragment extends DialogFragment {

    @Bind(R.id.etOldPassword) EditText etOldPassword;
    @Bind(R.id.etNewPass) EditText etNewPass;
    @Bind(R.id.etConfirmPass) EditText etConfirmPass;
    private View view;
    private OnChangePWListener onChangePWListener;

    public static ChangePWDialogFragment newInstance() {
        ChangePWDialogFragment frag = new ChangePWDialogFragment();
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
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_change_pass, null);
        ButterKnife.bind(this, view);

        final Dialog mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        return mDialog;
    }

    @OnClick(R.id.tvChange)
    public void showNextTrack() {
        String oldPW = etOldPassword.getText().toString();
        String newPW = etNewPass.getText().toString();
        String conPW = etConfirmPass.getText().toString();

        if (oldPW.isEmpty()) {
            etOldPassword.setError("This field is required!");
            etOldPassword.requestFocus();
        } else if (newPW.isEmpty()) {
            etNewPass.setError("This field is required!");
            etNewPass.requestFocus();
        } else if (conPW.isEmpty()) {
            etConfirmPass.setError("This field is required!");
            etConfirmPass.requestFocus();
        } else if (newPW.length() < 6) {
            etNewPass.setError("Password must be atleast 6 characters long");
            etNewPass.requestFocus();
        } else if (conPW.length() < 6) {
            etConfirmPass.setError("Password must be atleast 6 characters long");
            etConfirmPass.requestFocus();
        } else if (!newPW.equals(conPW)) {
            etConfirmPass.setError("Password do not match!");
            etConfirmPass.requestFocus();
        } else {
            onChangePWListener.onChangePassword(oldPW,newPW,conPW);
        }
    }

    public interface OnChangePWListener {
        void onChangePassword(String old,String newPW, String conPW);
    }

    public void setOnChangePWListener(OnChangePWListener onChangePWListener) {
        this.onChangePWListener = onChangePWListener;
    }
}
