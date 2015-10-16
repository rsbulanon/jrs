package dynobjx.com.jrs.helpers;

import android.widget.Spinner;

import java.util.Date;

/**
 * Created by rsbulanon on 7/28/15.
 */
public class AppConstants {

    /** API Request */
    public static final String JRS_URL = "http://jrs-api.azurewebsites.net/";
    public static final String FORM_URL_ENCODED = "x-www-form-urlencoded";
    public static final String APPLICATION_JSON = "application/json";
    public static final String UTF_8_CHARSET = "charset=utf-8";
    public static final String BEARER = "Bearer ";
    public static final String GRANT_TYPE = "grant_type";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String IS_MOBILE_VERIFIED = "isMobileVerified";
    public static final String ACCESS_TOKEN_EXPIRY = "accessTokenExpiry";
    public static final String PHONE_VERIFICATION = "phoneVerification";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String CODE = "Code";
    public static final String OLD_PW = "OldPassword";
    public static final String NEW_PW = "NewPassword";
    public static final String CONFIRM_PW = "ConfirmPassword";
    public static final String PHONE_NO = "PhoneNumber";
    public static final String POST_ACCOUNT_REGISTRATION = "api/Account/Register";
    public static final String POST_ACCOUNT_USER_INFORMATION= "api/Account/UserInfo";
    public static final String GET_BARANGAY = "api/Barangay";
    public static final String GET_BRANCHES = "api/Branches";
    public static final String GET_PROVINCES = "api/Province";
    public static final String GET_CITY_MUN = "api/CityMun";
    public static final String POST_PICKUP = "api/Pickup";
    public static final String POST_CHANGE_PW = "api/Account/ChangePassword";
    public static final String GET_VALIDATE_PU_LOC = "api/validatePickupLoc";
    public static final String POST_VERIFY_PHONE = "api/Account/VerifyPhoneNumber";
    public static final String POST_IS_PHONE_VERIFIED = "api/Account/IsPhoneVerified";
    public static final String POST_CHANGE_PHONE_NO = "api/Account/ChangePhoneNumber";
    public static final String POST_TOKEN = "Token";
    public static final String GET_USER_INFO = "api/MobileUserInfo";
    public static final String GET_BRANCH_DETAILS = "api/BranchDetails";
    public static final String TRACKING = "api/TrackingAirbill";
    public static final String RATE_CALCULATOR = "api/RatesCalculator";
    public static final String SERVICE = "api/Services";
    public static final String INTERNATIONAL_DESTINATION = "api/InternationalDestination";
    public static final String AUTHORIZATION = "Authorization";
    public static final String NUMBER = "Number";
    public static final String PHONE_NOT_VERIFIED = "Phone not verified";
    public static final String TAP_TO_ADD_ADDRESS = "Tap to add your address";
    public static String SCOPE_OF_DELIVERY = "";
    public static String SERVICE_TYPE = "";
    public static int RATE_CALCU_SERVICE = 0;

    /** toast messages */
    public static final String VERYFICATION_FAILED = "{\"Message\":\"The request is invalid.\",\"ModelState\":{\"\":[\"Failed to verify phone\",\"Invalid token.\"]}}";
    public static final String ERR_CONNECTION = "Connection Error, Please check your connection and try again";
    public static final String ERR_NO_RESULT = "No Result/s Found!";
    public static final String ERR_INVALID_PU_DATE = "Invalid Pickup Date!";
    public static final String ERR_INVALID_PU_TIME = "Invalid Pickup Time!";
    public static final String ERR_INVALID_PU_LOC = "Invalid Pickup location!";
    public static final String ERR_INVALID_USER = "The username or password is incorrect!";
    public static final String ERR_UPDATE_PROFILE = "Failed to update your profile, Please try again!";
    public static final String ERR_BRANCH_DETAILS = "No branch details found!";
    public static final String ERR_TRACKING = "Failed to process your request, Please try again!";
    public static final String ERR_VERIFICATION = "Failed to verify your mobile no, Please try again!";
    public static final String ERR_CHANGE_MOBILE = "Failed to change your mobile no, Please try again!";
    public static final String ERR_EMAIL_TAKEN = "Email address already taken!";
    public static final String ERR_ADDRESS_NOT_SET = "Please specify your address first in Edit Profile Menu";
    public static final String ERR_READ_TIMEOUT = "Request was taking too long, Check your internet connectivity and try again! ";
    public static final String OK_CHANGE_PW = "Password successfully changed!";
    public static final String OK_UPDATE_PROFILE = "Your profile has been successfully updated!";
    public static final String OK_REGISTRATION = "Congratulations, your are now registered!";
    public static final String OK_VERIFICATION = "Your mobile no. successfully verified!";
    public static final String OK_CHANGE_MOBILE = "Your mobile no. successfully updated!";
    public static final String OK_FB_WALL_POST = "Your comment was successfully posted!";
    public static final String WARNING_PROVINCE = "Please select a province";
    public static final String WARNING_TOWN = "Please select a town or municipality";
    public static final String WARNING_BRGY = "Please select a barangay";
    public static final String WARNING_DESTI_EXISTS = "Selected destination already added in your list";
    public static final String WARNING_ORIGIN = "Please specify your pickup location";
    public static final String WARNING_ST_ADDRESS = "Please specify your street address";
    public static final String WARNING_DESTI_COUNT = "Please select atleast 1 location for destination";
    public static final String WARNING_FIELD_REQUIRED = "This field is required";
    public static final String WARNING_PW_NOT_MATCH = "Confirm password does not match the password";
    public static final String WARNING_PROFILE_ADDRESS = "Please specify your address";
    public static final String WARNING_SELECT_ORIGIN = "Please select origin location";
    public static final String WARNING_SELECT_DESTINATION = "Please select destination location";
    public static final String WARNING_DECLARED_VALUE = "Please specify the declared value";
    public static final String WARNING_WEIGHT_IN_GRAMS = "Please specify weight in grams";
    public static final String WARNING_HEIGHT = "Please specify the item height";
    public static final String WARNING_WIDTH = "Please specify the item width";
    public static final String WARNING_SERVER_DOWN = "com.github.kevinsawicki.http.HttpRequest$HttpRequestException: java.net.UnknownHostException: Unable to resolve host \"jrs-api.azurewebsites.net\": No address associated with hostname\n";
    public static final int REG_TYPE_ID = 1;

    public static final String NOT_AVAIL = "Not Available";
    public static final String NOT_SET = "Not Set";

    /** not available*/
    public static final String NOT_AVAILABLE = "Not Available";

    /**rate calculator package values*/
    public static final double expressLetter_width = 0.0;
    public static final double expressLetter_height = 6.3;
    public static final double expressLetter_length = 9.5;

    public static final double onePounder_width = 0.0;
    public static final double onePounder_length = 15.0;
    public static final double onePounder_height = 11.0;

    public static final double regularLetter_width = 0.0;
    public static final double regularLetter_height = 0.0;
    public static final double regularLetter_length = 0.0;

    public static final double brownEvelope_width = 0.0;
    public static final double brownEvelope_length = 0.0;
    public static final double brownEvelope_height= 0.0;

    public static final double extraSmallBox_height = 0.0;
    public static final double extraSmallBox_length = 0.0;
    public static final double extraSmallBox_width = 0.0;

    public static final double expressSmallBox_height= 0.0;
    public static final double expressSmallBox_length= 0.0;
    public static final double expressSmallBox_width= 0.0;

    public static final double expressMediumBox_height= 0.0;
    public static final double expressMediumBox_length= 0.0;
    public static final double expressMediumBox_width= 0.0;

    public static final double expressLargeBox_height= 0.0;
    public static final double expressLargeBox_length= 0.0;
    public static final double expressLargeBox_width= 0.0;

    public static final double threePounder_height= 14.0;
    public static final double threePounder_length= 18.0;

    public static final double fivePounder_height= 14.0;
    public static final double fivePounder_length= 20.0;

    public static final double Documents_height= 11.0;
    public static final double Documents_length= 15.0;

    public static final double Parcel_height= 14.0;
    public static final double Parcel_length= 20.0;


    /** my current location */
    public static double MY_LOC_LATITUDE = 0;
    public static double MY_LOC_LONGITUDE = 0;

    /** spinner dropdown */
    public static int SPINNER_DROP_DOWN = android.R.layout.simple_spinner_dropdown_item;

    /** request params */
    public static final String PARAM_PROV_ID = "provinceId";
    public static final String PARAM_CITY_MUNI_ID = "citymunId";
    public static final String PARAM_BRGY_ID = "BrgyId";
    public static final String PARAM_USERNAME = "userName";
    public static final String PARAM_BRANCH_ID = "BranchId";

    /** set api read timeout */
    public static final int READ_TIMEOUT = 15000;
}
