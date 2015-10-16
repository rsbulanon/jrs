package dynobjx.com.jrs.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.customviews.EditTextPlus;
import dynobjx.com.jrs.helpers.ApiRequestHelper;
import dynobjx.com.jrs.helpers.AppConstants;

public class CreateAccountActivity extends BaseActivity implements ApiRequestHelper.OnAPIRequestListener{

    @Bind(R.id.etEmail) EditTextPlus email;
    @Bind(R.id.etNumber) EditTextPlus number;
    @Bind(R.id.etPassword) EditTextPlus password;
    @Bind(R.id.etConfirmPassword) EditTextPlus confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        ButterKnife.bind(this);
        initActionBar("Registration");
    }

    public void regGetInformation () {
        String emailAdd = email.getText().toString();
        String numberPhone = number.getText().toString();
        String pass = password.getText().toString();
        String passConfirm = confirmPassword.getText().toString();

        ApiRequestHelper apiRequestHelper = new ApiRequestHelper();
        apiRequestHelper.setOnAPIRequestListener(CreateAccountActivity.this);
        apiRequestHelper.registerUser(emailAdd, pass, passConfirm, numberPhone);
    }

    @OnClick(R.id.btnRegister)
    public void register() {
        if (email.length() == 0) {
            email.setError(AppConstants.WARNING_FIELD_REQUIRED);
            email.requestFocus();
        } else if (number.length() == 0) {
            number.setError(AppConstants.WARNING_FIELD_REQUIRED);
            number.requestFocus();
        } else if (password.length() == 0) {
            password.setError(AppConstants.WARNING_FIELD_REQUIRED);
            password.requestFocus();
        } else if (confirmPassword.length() == 0) {
            confirmPassword.setError(AppConstants.WARNING_FIELD_REQUIRED);
            confirmPassword.requestFocus();
        } else if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            confirmPassword.setError(AppConstants.WARNING_PW_NOT_MATCH);
            confirmPassword.requestFocus();
        } else  {
            if (isNetworkAvailable()) {
                regGetInformation();

            } else {
                showToast(AppConstants.ERR_CONNECTION);
            }
        }
    }

    @Override
    public void onRequestStart(String action) {
        Log.d("API", "APIRequest start");
        showProgressDialog("Registration","Creating your account, Please wait...");
    }

    @Override
    public void onRequestException(Exception e) {
        Log.d("API", "APIRequest exception " + e.toString());
        dismissProgressDialog();
    }

    @Override
    public void onRequestSuccessful(String action, String body) {
        Log.d("API", "APIRequest successful " + body);
        dismissProgressDialog();

        if(body.isEmpty()) {
            showToast(AppConstants.OK_REGISTRATION);
            startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
            onBackPressed();
        } else {
            showToast("Registration failed");
        }

        if (action.equals(""))
        try {
            JSONObject obj = new JSONObject(body);
            String message = obj.getString("ModelState");
            String taken = "{\"\":[\"Name " + email.getText().toString() + " is already taken.\",\"Email '" + email.getText().toString() + "' is already taken.\"]}";
            if (message.equals(taken)) {
                email.setError(AppConstants.ERR_EMAIL_TAKEN);
            }
            else {
                showToast(AppConstants.OK_REGISTRATION);
                startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
                onBackPressed();
            }
        } catch (JSONException e) {
            Log.d("API","error while parsing json --> " + e.toString());
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        animateToRight(this);
    }
}
