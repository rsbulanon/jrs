package dynobjx.com.jrs.helpers;

import android.os.AsyncTask;

import android.util.Log;


import com.github.kevinsawicki.http.HttpRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by rsbulanon on 7/22/15.
 */
public class ApiRequestHelper {

    private OnAPIRequestListener onAPIRequestListener;

    public void registerUser(String email, String password, String confirmPassword, String number) {
        Map<String, Object> data = new HashMap<>();
        data.put("Email", email);
        data.put("Password", password);
        data.put("ConfirmPassword", confirmPassword);
        data.put("Number", number);
        data.put("RegistrationTypeId", AppConstants.REG_TYPE_ID);
        new PostRequestTask(AppConstants.POST_ACCOUNT_REGISTRATION,data)
                .execute(HttpRequest.post(AppConstants.JRS_URL + AppConstants.POST_ACCOUNT_REGISTRATION));
    }

    /**Tracking*/
    public void tracking (int airBill, int trackingCode, String token) {
        new APIRequestTask(AppConstants.TRACKING).execute(HttpRequest.get(AppConstants.JRS_URL +
                AppConstants.TRACKING, true, "airbill", airBill, "trackingCode", trackingCode)
                .header(AppConstants.AUTHORIZATION, AppConstants.BEARER + token));
    }

    /** get all barangay */
    public void getBarangays() {
       new APIRequestTask(AppConstants.GET_BARANGAY)
               .execute(HttpRequest.get(AppConstants.JRS_URL + AppConstants.GET_BARANGAY));
    }

    /** get all branches */
    public void getBranches(String token) {
        new APIRequestTask(AppConstants.GET_BRANCHES)
                .execute(HttpRequest.get(AppConstants.JRS_URL + AppConstants.GET_BRANCHES)
                        .header(AppConstants.AUTHORIZATION, AppConstants.BEARER + token));
    }

    /** get all province */
    public void getProvinces(String token) {
        new APIRequestTask(AppConstants.GET_PROVINCES)
                .execute(HttpRequest.get(AppConstants.JRS_URL + AppConstants.GET_PROVINCES)
                        .header(AppConstants.AUTHORIZATION, AppConstants.BEARER + token));
    }

    /** change password*/
    public void changePassword(String old, String newPW, String confirmPW, String token) {
        HashMap<String,Object> body = new HashMap<>();
        body.put(AppConstants.OLD_PW,old);
        body.put(AppConstants.NEW_PW, newPW);
        body.put(AppConstants.CONFIRM_PW, confirmPW);
        new PostRequestTask(AppConstants.POST_CHANGE_PW,body)
                .execute(HttpRequest.post(AppConstants.JRS_URL + AppConstants.POST_CHANGE_PW)
                        .header(AppConstants.AUTHORIZATION, AppConstants.BEARER + token));
    }

    /** change phone number */
    public void changePhoneNumber(String mobileNo, String token) {
        HashMap<String,Object> body = new HashMap<>();
        body.put(AppConstants.NUMBER,mobileNo);
        new PostRequestTask(AppConstants.POST_CHANGE_PHONE_NO,body)
                .execute(HttpRequest.post(AppConstants.JRS_URL + AppConstants.POST_CHANGE_PHONE_NO)
                        .header(AppConstants.AUTHORIZATION, AppConstants.BEARER + token));
    }

    public void services() {
        new APIRequestTask(AppConstants.SERVICE)
                .execute(HttpRequest.get(AppConstants.JRS_URL + AppConstants.SERVICE));
    }

    public void getCountry(String token) {
        new APIRequestTask(AppConstants.INTERNATIONAL_DESTINATION)
                .execute(HttpRequest.get(AppConstants.JRS_URL + AppConstants.INTERNATIONAL_DESTINATION)
                        .header(AppConstants.AUTHORIZATION, AppConstants.BEARER + token));
    }

    public void getRateCalcu (int origin, int destination, boolean valuation, boolean insurance, Double declaredValue,
                              Double weight, Double length, Double width, Double height, String token) {
        Log.d("RATE", "ORIGIN"+origin);
        Log.d("RATE", "Destination"+destination);
        Log.d("RATE", "valuation"+valuation);
        Log.d("RATE", "insurance"+insurance);
        Log.d("RATE", "declaredValue"+declaredValue);
        Log.d("RATE", "weight"+weight);
        Log.d("RATE", "length"+length);
        Log.d("RATE", "width"+width);
        Log.d("RATE", "height"+height);
        Log.d("RATE", "service"+AppConstants.RATE_CALCU_SERVICE);

        new APIRequestTask(AppConstants.RATE_CALCULATOR)
                .execute(HttpRequest.get(AppConstants.JRS_URL + AppConstants.RATE_CALCULATOR, true,
                        "rProbOri", origin, "rProbDesti", destination, "rservi", AppConstants.RATE_CALCU_SERVICE,
                        "rValuation",valuation, "rInsurance", insurance,  "rdeclaredValue",
                        declaredValue, "rweight", weight, "rlenght", length, "rwidth", width,
                        "rheight", height)
                        .header(AppConstants.AUTHORIZATION, AppConstants.BEARER + token));
    }

    /** get town/municipality under a province */
    public void getMunicipality(int provinceId, String token) {
        new APIRequestTask(AppConstants.GET_CITY_MUN)
                .execute(HttpRequest.get(AppConstants.JRS_URL+AppConstants.GET_CITY_MUN,
                        true, AppConstants.PARAM_PROV_ID, provinceId)
                        .header(AppConstants.AUTHORIZATION, AppConstants.BEARER + token));
    }

    /** get barangay under a municipality */
    public void getBrgyByCityId(int cityId, String token) {
        new APIRequestTask(AppConstants.GET_BARANGAY)
                .execute(HttpRequest.get(AppConstants.JRS_URL+AppConstants.GET_BARANGAY,
                        true, AppConstants.PARAM_CITY_MUNI_ID, cityId)
                        .header(AppConstants.AUTHORIZATION, AppConstants.BEARER + token));
    }

    /** send pickup request */
    public void sendPickupRequest(int brgyId, String address, String pickupDate,
                                  String description, String username, String token ) {
        Map<String, Object> data = new HashMap<>();
        data.put("Id", 1);
        data.put("PplacesBrgyId", brgyId);
        data.put("Paddress", address);
        data.put("PickupDate", pickupDate);
        data.put("Description", description);
        data.put("UpdatedBy", "");
        data.put("LastDateEdited", "");
        data.put("AspNetUserName", username);
        data.put("PickupStatusId", 1);
        new PostRequestTask(AppConstants.POST_PICKUP,data)
                .execute(HttpRequest.post(AppConstants.JRS_URL + AppConstants.POST_PICKUP)
                        .header(AppConstants.AUTHORIZATION, AppConstants.BEARER + token));
    }

    /** get branch details */
    public void getBranchDetailsByBranchId(int branchId, String token) {
        new APIRequestTask(AppConstants.GET_BRANCH_DETAILS)
                .execute(HttpRequest.get(AppConstants.JRS_URL + AppConstants.GET_BRANCH_DETAILS,
                        true, AppConstants.PARAM_BRANCH_ID, branchId)
                        .header(AppConstants.AUTHORIZATION, AppConstants.BEARER + token));
    }

    /** get access token */
    public void getAccessToken(String username, String password) {
        HashMap<String,Object> body = new HashMap<>();
        body.put(AppConstants.GRANT_TYPE,AppConstants.PASSWORD);
        body.put(AppConstants.USERNAME, username);
        body.put(AppConstants.PASSWORD, password);
        new PostRequestTask(AppConstants.POST_TOKEN,body)
                .execute(HttpRequest.post(AppConstants.JRS_URL + AppConstants.POST_TOKEN)
                        .accept(AppConstants.FORM_URL_ENCODED));
    }

    /** validate pickup location */
    public void validatePickupLocation(int brgyId, String token) {
        new APIRequestTask(AppConstants.GET_VALIDATE_PU_LOC)
                .execute(HttpRequest.get(AppConstants.JRS_URL + AppConstants.GET_VALIDATE_PU_LOC,
                        true, AppConstants.PARAM_BRGY_ID, brgyId)
                        .header(AppConstants.AUTHORIZATION, AppConstants.BEARER + token));
    }

    /** update user info */
    public void updateUserInfo(String username, int brgyId, String address, String fullName, String token) {
        HashMap<String,Object> info = new HashMap<>();
        info.put("AspNetUserName", username);
        info.put("Brgyid", brgyId);
        info.put("Address", address);
        info.put("FullName", fullName);
        info.put("Phone", DaoHelper.getUserInfo().getPhone());
        new PostRequestTask(AppConstants.GET_USER_INFO,info).execute(HttpRequest.put(AppConstants.JRS_URL +
                AppConstants.GET_USER_INFO).header(AppConstants.AUTHORIZATION, AppConstants.BEARER + token));
    }

    /** get user info */
    public void getUserInfo(String userName, String token) {
        new APIRequestTask(AppConstants.GET_USER_INFO).execute(HttpRequest.get(AppConstants.JRS_URL +
                AppConstants.GET_USER_INFO, true, AppConstants.PARAM_USERNAME, userName)
                .header(AppConstants.AUTHORIZATION, AppConstants.BEARER + token));
    }

    /** check if phone number is verified */
    public void checkIfPhoneIsVerified(String token) {
        new APIRequestTask(AppConstants.POST_IS_PHONE_VERIFIED).execute(HttpRequest.post(AppConstants.JRS_URL +
                AppConstants.POST_IS_PHONE_VERIFIED)
                .header(AppConstants.AUTHORIZATION, AppConstants.BEARER + token));
    }

    /** verify phone number */
    public void verifyPhoneNumber(String mobileNo, String code, String token) {
        HashMap<String,Object> body = new HashMap<>();
        body.put(AppConstants.PHONE_NO, mobileNo);
        body.put(AppConstants.CODE, code);
        new PostRequestTask(AppConstants.POST_VERIFY_PHONE,body)
                .execute(HttpRequest.post(AppConstants.JRS_URL + AppConstants.POST_VERIFY_PHONE)
                .accept(AppConstants.APPLICATION_JSON)
                .acceptCharset(AppConstants.UTF_8_CHARSET)
                .authorization(AppConstants.BEARER+token));
    }

    public interface OnAPIRequestListener {
        void onRequestStart(String action);
        void onRequestException(Exception e);
        void onRequestSuccessful(String action, String body);
    }

    public void setOnAPIRequestListener(OnAPIRequestListener onAPIRequestListener) {
        this.onAPIRequestListener = onAPIRequestListener;
    }

    private class APIRequestTask extends AsyncTask<HttpRequest,Void,String> {

        String action;
        Exception ex;

        public APIRequestTask(String action) {
            this.action = action;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onAPIRequestListener.onRequestStart(action);
        }

        @Override
        protected String doInBackground(HttpRequest... httpRequests) {
            try {
                return httpRequests[0].body();
            } catch (Exception ex) {
                this.ex = ex;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String body) {
            super.onPostExecute(body);
            if (ex == null) {
                onAPIRequestListener.onRequestSuccessful(action, body);
            } else {
                onAPIRequestListener.onRequestException(ex);
            }
        }
    }

    private class PostRequestTask extends AsyncTask<HttpRequest,Void,String> {

        String action;
        Map<String, Object> data = new HashMap<>();
        Exception ex;

        public PostRequestTask(String action, Map<String, Object> data) {
            this.action = action;
            this.data = data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onAPIRequestListener.onRequestStart(action);
        }

        @Override
        protected String doInBackground(HttpRequest... httpRequests) {
            try {
                httpRequests[0].getConnection().setReadTimeout(AppConstants.READ_TIMEOUT);
                return httpRequests[0].form(data).body();
            } catch (Exception ex) {
                this.ex = ex;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String responseCode) {
            super.onPostExecute(responseCode);
            if (ex == null) {
                onAPIRequestListener.onRequestSuccessful(action,responseCode);
            } else {
                onAPIRequestListener.onRequestException(ex);
            }
        }
    }
}
