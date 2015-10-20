package dynobjx.com.jrs.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import dynobjx.com.jrs.activities.BaseActivity;
import dynobjx.com.jrs.helpers.AppConstants;

/**
 * Created by rsbulanon on 10/20/15.
 */
public class ForgotPasswordFragment extends DialogFragment {

    @Bind(R.id.etEmail) EditText etEmail;
    private View view;
    private OnForgotPasswordListener onForgotPasswordListener;

    public static ForgotPasswordFragment newInstance() {
        ForgotPasswordFragment frag = new ForgotPasswordFragment();
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
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_forgot_password, null);
        ButterKnife.bind(this, view);

        final Dialog mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        return mDialog;
    }

    @OnClick(R.id.btnForgot)
    public void forgotPassword() {
        String email = etEmail.getText().toString();

        if (email.isEmpty()) {
            etEmail.setError(AppConstants.WARNING_FIELD_REQUIRED);
            etEmail.requestFocus();
        } else if (!((BaseActivity)getActivity()).isValidEmailAddress(email)) {
            etEmail.setError(AppConstants.WARNING_INVALID_EMAIL);
            etEmail.requestFocus();
        } else {
            onForgotPasswordListener.onForgot(email);
        }
    }

    public interface OnForgotPasswordListener {
        void onForgot(String email);
    }

    public void setOnForgotPasswordListener(OnForgotPasswordListener onForgotPasswordListener) {
        this.onForgotPasswordListener = onForgotPasswordListener;
    }
}
