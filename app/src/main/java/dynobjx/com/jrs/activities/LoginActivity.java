package dynobjx.com.jrs.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.fragments.ForgotPasswordFragment;
import dynobjx.com.jrs.helpers.ApiRequestHelper;
import dynobjx.com.jrs.helpers.AppConstants;
import dynobjx.com.jrs.helpers.DaoHelper;
import dynobjx.com.jrs.helpers.Prefs;


/**
 * Created by rsbulanon on 7/8/15.
 */
public class LoginActivity extends BaseActivity implements ApiRequestHelper.OnAPIRequestListener {

    @Bind(R.id.etEmail) EditText etEmail;
    @Bind(R.id.etPassword) EditText etPassword;
    private  ApiRequestHelper apiRequestHelper = new ApiRequestHelper();
    private int requestCount = 2;
    private int successRequestCount = 0;
    private DateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initFields();
        /** check if access token is empty */
        if (Prefs.getMyStringPrefs(this,AppConstants.ACCESS_TOKEN).isEmpty()) {
            apiRequestHelper.setOnAPIRequestListener(this);
        } else {
            /** check if access token is expired */
            try {
                Date tokenExpiry = sdf.parse(Prefs.getMyStringPrefs(this,AppConstants.ACCESS_TOKEN_EXPIRY));
                Log.d("ex","token --> " + tokenExpiry);
                Log.d("ex","today --> " + Calendar.getInstance().getTime());

                if (Calendar.getInstance().getTime().before(tokenExpiry)) {
                    Log.d("ex","token not yet expired");
                    startActivity(new Intent(this, MainActivity.class));
                    animateToLeft(this);
                    finish();
                } else {
                    apiRequestHelper.setOnAPIRequestListener(this);
                    Log.d("ex","token already expired");
                }
            } catch (ParseException e) {
                Log.d("ex","error -->   " + e.toString());
            }
        }
    }
    @OnClick(R.id.btnLogin)
    public void authenticateUser() {
        String email = etEmail.getText().toString();
        String pw = etPassword.getText().toString();

        /** validation */
        if (email.isEmpty()) {
            etEmail.setError(AppConstants.WARNING_FIELD_REQUIRED);
            etEmail.requestFocus();
        } else if (pw.isEmpty()) {
            etPassword.setError(AppConstants.WARNING_FIELD_REQUIRED);
            etPassword.requestFocus();
        } else if (!isValidEmailAddress(email)) {
            etEmail.setError(AppConstants.WARNING_INVALID_EMAIL);
            etEmail.requestFocus();
        } else if (pw.length() < 6) {
            etPassword.setError(AppConstants.WARNING_INVALID_PASS_LENGTH);
            etPassword.requestFocus();
        } else {
            /** check if network connection is available */
            if (isNetworkAvailable()) {
                requestCount++;
                apiRequestHelper.getAccessToken(email,pw);
            } else {
                showToast(AppConstants.ERR_CONNECTION);
            }
        }
    }

    @OnClick(R.id.tvCreateAccount)
    public void createAccount() {
        startActivity(new Intent(this, CreateAccountActivity.class));
        animateToLeft(this);
    }

    @Override
    public void onRequestStart(String action) {
        Log.d("fetch", "action --> " + action);
        if (action.equals(AppConstants.POST_TOKEN)) {
            showProgressDialog("Login", "Authenticating your credentials, Please wait...");
        } else if (action.equals(AppConstants.GET_FORGOT_PASS)) {
            showProgressDialog("Forgot Password","Please wait...");
        } else {
            updateProgressDialog("Fetching data, Please wait...");
        }
    }

    @Override
    public void onRequestException(Exception e) {
        Log.d("fetch", "exception ---> " + e.toString());
        try {
            dismissProgressDialog();
            showToast(AppConstants.ERR_INVALID_USER);
        } catch (Exception ex) {
            Log.d("fetch", "exception ---> " + ex.toString());
            LoginActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    showToast("Connection to server lost.");
                }
            });
        }

    }

    @Override
    public void onRequestSuccessful(String action, String json) {
        Log.d("fetch", "------> " + json);
        if (json.contains("Unconfirmed or invalid email")) {
            dismissProgressDialog();
            showSweetDialog("Unverified Email", "Unconfirmed or invalid email, " +
                    "Re-sending email confirmation please check you email.","warning");
        } else {
            try {
                if (Prefs.getMyStringPrefs(this, "accessToken").isEmpty() && action.equals(AppConstants.POST_TOKEN)) {
                    if (isUserAuthenticated(json)) {
                        Log.d("login", "JSON --> " + json);
                        setUserCredentials(json);
                        String token = Prefs.getMyStringPrefs(this,AppConstants.ACCESS_TOKEN);

                        /** check if there are cached provinces in local db
                         * if none, fetch all provinces from API
                         * */
                        if (DaoHelper.getProvinceCount() == 0) {
                            requestCount++;
                            apiRequestHelper.getProvinces(token);
                        }

                        /** check if there are cached branches in local db
                         *  if none, fetch all branches from API
                         * */
                        if (DaoHelper.getBranchesCount() == 0) {
                            requestCount++;
                            apiRequestHelper.getBranches(token);
                        }

                        apiRequestHelper.getUserInfo(Prefs.getMyStringPrefs(this, AppConstants.USERNAME), token);
                        apiRequestHelper.checkIfPhoneIsVerified(token);
                    } else {
                        showToast(AppConstants.ERR_INVALID_USER);
                        successRequestCount = 0;
                        requestCount = 2;
                        dismissProgressDialog();
                    }
                } else if (action.equals(AppConstants.GET_BRANCHES)) {
                    saveBranchesToLocalDB(json);
                    Log.d("fetch","branches saved to local DB");
                } else if (action.equals(AppConstants.GET_PROVINCES)) {
                    saveProvinceToLocalDB(json);
                    Log.d("fetch","provinces saved to local DB");
                } else if (action.equals(AppConstants.GET_USER_INFO)) {
                    saveUserInfoToLocalDB(json);
                } else if (action.equals(AppConstants.POST_IS_PHONE_VERIFIED)) {
                    Log.d("fetch","phone verification --> " + json);
                    Prefs.setMyStringPref(this,AppConstants.PHONE_VERIFICATION,json);
                }
                successRequestCount++;
                Log.d("fetch", "SUCCESS REQUEST COUNT --> " + successRequestCount + " request count --> " + requestCount);
                if (successRequestCount == requestCount) {
                    dismissProgressDialog();
                    startActivity(new Intent(this, MainActivity.class));
                    animateToLeft(this);
                    finish();
                } else {
                    if (action.equals(AppConstants.GET_FORGOT_PASS)) {
                        dismissProgressDialog();
                        showSweetDialog("Forgot Password","Please check your email to reset your password","success");
                    }
                }
            } catch (Exception ex) {

            }
        }
    }

    private void initFields() {
        //etEmail.setText("nedflanders@gmail.com");
        //etPassword.setText("Passw0rd");

//        etEmail.setText("rsbulanon@dynamicobjx.com");
//        etPassword.setText("qwerty");
    }

    @OnClick(R.id.tvForgotPassword)
    public void forgotPassword() {
        final ForgotPasswordFragment forgotPasswordFragment = ForgotPasswordFragment.newInstance();
        forgotPasswordFragment.setOnForgotPasswordListener(new ForgotPasswordFragment.OnForgotPasswordListener(){
            @Override
            public void onForgot(String email) {
                if (!isNetworkAvailable()) {
                    showToast(AppConstants.ERR_CONNECTION);
                } else {
                    forgotPasswordFragment.dismiss();
                    apiRequestHelper.forgotPassword(email);
                }
            }
        });
        forgotPasswordFragment.show(getFragmentManager(), "forgot");
    }
}
