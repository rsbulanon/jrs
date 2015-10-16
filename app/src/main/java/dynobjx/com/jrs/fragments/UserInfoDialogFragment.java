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
import android.widget.TextView;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.activities.MainActivity;
import dynobjx.com.jrs.dao.UserInfo;
import dynobjx.com.jrs.helpers.AppConstants;
import dynobjx.com.jrs.helpers.DaoHelper;

/**
 * Created by rsbulanon on 7/31/15.
 */
public class UserInfoDialogFragment extends DialogFragment {

    @Bind(R.id.etFullName)EditText etFullName;
    @Bind(R.id.etAddress) EditText etAddress;
    @Bind(R.id.tvMyAddress) TextView tvMyAddress;
    private View view;
    private MainActivity activity;
    private int selectedBrgyId = 0;
    private OnUpdateProfileListener onUpdateProfileListener;

    public static UserInfoDialogFragment newInstance() {
        UserInfoDialogFragment frag = new UserInfoDialogFragment();
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
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_manage_info, null);
        ButterKnife.bind(this, view);
        activity = (MainActivity)getActivity();
        UserInfo userInfo = DaoHelper.getUserInfo();

        etFullName.setText(userInfo.getFullName().equals(AppConstants.NOT_SET) ? "" : userInfo.getFullName());
        if (userInfo.getAddress().equals(userInfo.getUserName())) {
            tvMyAddress.setText("");
        } else {
            Log.d("info", "address --> " + userInfo.getAddress());
            if (userInfo.getAddress().contains(" ")) {
                String[] address = userInfo.getAddress().split(" ");
                Log.d("info","split --> " + Arrays.toString(address));
                etAddress.setText(address[0]);
                tvMyAddress.setText(address[1]);
            } else {
                tvMyAddress.setText(AppConstants.TAP_TO_ADD_ADDRESS);
            }
        }

        final Dialog mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        return mDialog;
    }

    @OnClick(R.id.tvMyAddress)
    public void changeMyAddress() {
        final PickupDialogFragment fragment = PickupDialogFragment.newInstance("address");
        fragment.setOnLocationListener(new PickupDialogFragment.OnLocationListener() {
            @Override
            public void onSelectedLocation(int brgyId, String location) {
                fragment.dismiss();
                selectedBrgyId = brgyId;
                tvMyAddress.setText(location);
            }
        });
        fragment.show(getActivity().getSupportFragmentManager(),"address");
    }

    @OnClick(R.id.tvUpdate)
    public void updateProfile() {
        String fullName = etFullName.getText().toString();
        String address = etAddress.getText().toString();

        if (fullName.isEmpty()) {
            etFullName.setError("This field is required");
            etFullName.requestFocus();
        } else if (address.isEmpty()) {
            etAddress.setError("This field is required");
            etAddress.requestFocus();
        } else if (fullName.equalsIgnoreCase(AppConstants.NOT_SET)) {
            etFullName.setError("Invalid full name");
            etFullName.requestFocus();
        } else if (address.equalsIgnoreCase(AppConstants.NOT_SET)) {
            etAddress.setError("Invalid address");
            etAddress.requestFocus();
        } else if (selectedBrgyId == 0) {
            activity.showToast(AppConstants.WARNING_PROFILE_ADDRESS);
        } else {
            onUpdateProfileListener.onUpdateProfile(fullName,address,
                    selectedBrgyId,tvMyAddress.getText().toString());
        }
    }

    public interface OnUpdateProfileListener {
        void onUpdateProfile(String fullname, String address, int brgyId, String location);
    }

    public void setOnUpdateProfileListener(OnUpdateProfileListener onUpdateProfileListener) {
        this.onUpdateProfileListener = onUpdateProfileListener;
    }
}
