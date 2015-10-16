package dynobjx.com.jrs.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.adapters.BannerAdapter;
import dynobjx.com.jrs.customviews.DrawerArrowDrawable;
import dynobjx.com.jrs.customviews.TextViewPlus;
import dynobjx.com.jrs.dao.UserInfo;
import dynobjx.com.jrs.fragments.BannerFragment;
import dynobjx.com.jrs.fragments.ChangeMobileNoDialogFragment;
import dynobjx.com.jrs.fragments.ChangePWDialogFragment;
import dynobjx.com.jrs.fragments.TrackingFragment;
import dynobjx.com.jrs.fragments.UserInfoDialogFragment;
import dynobjx.com.jrs.fragments.VerifyMobileDialogFragment;
import dynobjx.com.jrs.helpers.ApiRequestHelper;
import dynobjx.com.jrs.helpers.AppConstants;
import dynobjx.com.jrs.helpers.DaoHelper;
import dynobjx.com.jrs.helpers.Prefs;

public class MainActivity extends BaseActivity implements ApiRequestHelper.OnAPIRequestListener,
                                                            NavigationView.OnNavigationItemSelectedListener{

    @Bind(R.id.drawerLayout) DrawerLayout drawerLayout;
    @Bind(R.id.drawerIndicator) ImageView drawerIndicator;
    @Bind(R.id.viewPager) ViewPager viewPager;
    @Bind(R.id.tvURL) TextViewPlus textViewPlus;
    @Bind(R.id.navDrawer) NavigationView navDrawer;
    private DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;
    private int currentPage;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private int[] images = new int[]{R.drawable.img1,R.drawable.img2,R.drawable.img3};
    private final Handler mDrawerActionHandler = new Handler();
    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private ApiRequestHelper apiRequestHelper;
    private TextView tvFullName;
    private String userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initDrawerArrowDrawable();
        initCarousel();
        Log.d("map","home");
        apiRequestHelper = new ApiRequestHelper();
        apiRequestHelper.setOnAPIRequestListener(this);
        showMobileVerificationDialog();
    }

    @OnClick({R.id.llBranches,R.id.llRates,R.id.llScope,R.id.llTracking,R.id.llPickUp,R.id.tvURL})
    public void showBranchLocator(View view) {
        switch (view.getId()) {
            case R.id.llBranches:
                startActivity(new Intent(this, BranchLocatorActivity.class));
                break;
            case R.id.llRates:
                startActivity(new Intent(this, RateCalcuPage1Activity.class));
                break;
            case R.id.llScope:
                startActivity(new Intent(this, ScopeActivity.class));
                break;
            case R.id.llTracking:
                final TrackingFragment trackingFragment = TrackingFragment.newInstance();
                trackingFragment.setOnTrackingListener(new TrackingFragment.OnTrackingListener() {
                    @Override
                    public void onRequestSuccessful(String result) {
                        Intent intent = new Intent(MainActivity.this,TrackResultActivity.class);
                        intent.putExtra("result",result);
                        startActivity(intent);
                        animateToLeft(MainActivity.this);
                    }
                });
                trackingFragment.show(getSupportFragmentManager(),"tracking");
                break;
            case R.id.llPickUp:
                startActivity(new Intent(this,PickupActivity.class));
                break;
            case R.id.tvURL:
                String url = "http://jrs-express.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
        }
        animateToLeft(this);
    }

    private void initCarousel() {
        for (int i : images) {
            fragments.add(BannerFragment.newInstance(i));
        }
        viewPager.setAdapter(new BannerAdapter(getSupportFragmentManager(), fragments));
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (viewPager.getCurrentItem() == images.length - 1) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 4000);
    }

    private void initDrawerArrowDrawable() {
        drawerArrowDrawable = new DrawerArrowDrawable(getResources());
        drawerArrowDrawable.setStrokeColor(getResources().getColor(R.color.light_gray));
        drawerIndicator.setImageDrawable(drawerArrowDrawable);
        drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                offset = slideOffset;
                // Sometimes slideOffset ends up so close to but not quite 1 or 0.
                if (slideOffset >= .995) {
                    flipped = true;
                    drawerArrowDrawable.setFlip(flipped);
                } else if (slideOffset <= .005) {
                    flipped = false;
                    drawerArrowDrawable.setFlip(flipped);
                }

                drawerArrowDrawable.setParameter(offset);
            }
        });
        tvFullName = (TextView)navDrawer.findViewById(R.id.tvFullName);
        tvFullName.setText(DaoHelper.getUserInfo().getFullName());
        navDrawer.setNavigationItemSelectedListener(this);
    }

    @OnClick(R.id.drawerIndicator)
    public void toggleDrawerMenu() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onRequestStart(String action) {
        Log.d("verify", "on verification start");
        if (action.equals(AppConstants.POST_CHANGE_PW)) {
            showProgressDialog("Change Password", "Changing your password, Please wait..");
        } else if (action.equals(AppConstants.GET_USER_INFO)) {
            showProgressDialog("Manage Profile","Updating your profile, Please wait..");
        }
    }

    @Override
    public void onRequestException(Exception e) {
        Log.d("verify", "error --> " + e.toString());
    }

    @Override
    public void onRequestSuccessful(String action, String body) {
        Log.d("verify", "OK --> " + body);
        if (action.equals(AppConstants.POST_CHANGE_PW)) {
            showToast(AppConstants.OK_CHANGE_PW);
        } else if (action.equals(AppConstants.GET_USER_INFO)) {
           updateUserInfoInLocalDB(body,userLocation);
            Log.d("info","info --> " + body);
            if (body.contains("FullName")) {
                tvFullName.setText(DaoHelper.getUserInfo().getFullName());
                showToast(AppConstants.OK_UPDATE_PROFILE);
            } else {
                showToast(AppConstants.ERR_UPDATE_PROFILE);
            }
        }
        dismissProgressDialog();
    }

    @OnClick(R.id.ivFB)
    public void facebookIntegration() {
        if (isNetworkAvailable()) {
            startActivity(new Intent(this,FBActivity.class));
            animateToLeft(this);
        } else {
            showToast(AppConstants.ERR_CONNECTION);
        }
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        mDrawerActionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigate(menuItem.getItemId());
            }
        }, DRAWER_CLOSE_DELAY_MS);
        return true;
    }

    private void navigate(int menu) {
        switch (menu) {
            case R.id.navigation_item_1:
                /** manager user info
                 * update full name and address
                 * */
                final UserInfoDialogFragment userInfo = UserInfoDialogFragment.newInstance();
                userInfo.setOnUpdateProfileListener(new UserInfoDialogFragment.OnUpdateProfileListener() {
                    @Override
                    public void onUpdateProfile(String fullname, String address, int brgyId, String location) {
                        if (isNetworkAvailable()) {
                            userInfo.dismiss();
                            userLocation = location;
                            apiRequestHelper.updateUserInfo(
                                    Prefs.getMyStringPrefs(MainActivity.this,AppConstants.USERNAME),
                                    brgyId,address + " " + location,fullname,
                                    Prefs.getMyStringPrefs(MainActivity.this,AppConstants.ACCESS_TOKEN));
                        } else {
                            showToast(AppConstants.ERR_CONNECTION);
                        }
                    }
                });
                userInfo.show(getSupportFragmentManager(),"userinfo");
                break;
            case R.id.navigation_item_2:
                final ChangePWDialogFragment fragment = ChangePWDialogFragment.newInstance();
                fragment.setOnChangePWListener(new ChangePWDialogFragment.OnChangePWListener() {
                    @Override
                    public void onChangePassword(String old, String newPW, String conPW) {
                        fragment.dismiss();
                        apiRequestHelper.changePassword(old,newPW,conPW,
                                Prefs.getMyStringPrefs(MainActivity.this,AppConstants.ACCESS_TOKEN));
                    }
                });
                fragment.show(getSupportFragmentManager(),"password");
                break;
            case R.id.navigation_item_3:
                final ChangeMobileNoDialogFragment mobileFragment = ChangeMobileNoDialogFragment.newInstance();
                mobileFragment.setOnChangeMobileNoListener(new ChangeMobileNoDialogFragment.OnChangeMobileNoListener() {
                    @Override
                    public void onChangedSuccessful(String result, String newNo) {
                        mobileFragment.dismiss();
                        UserInfo userInfo = DaoHelper.getUserInfo();
                        userInfo.setPhone(newNo);
                        DaoHelper.updateUserInfo(userInfo);
                        Prefs.setMyStringPref(MainActivity.this, AppConstants.PHONE_VERIFICATION,
                                AppConstants.PHONE_NOT_VERIFIED);
                        showToast(AppConstants.OK_CHANGE_MOBILE);
                        showMobileVerificationDialog();
                    }

                    @Override
                    public void onChangedFailed(Exception e) {
                        mobileFragment.dismiss();
//                        showToast(AppConstants.ERR_CHANGE_MOBILE);
                        Log.d("err","error in changing mobile --> " + e.toString());
                    }
                });
                mobileFragment.show(getSupportFragmentManager(),"mobile");
                break;
            case R.id.navigation_item_4:
                new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Logout")
                        .setContentText("Are you sure you want to exit from the app?")
                        .setConfirmText("Yes")
                        .setCancelText("No")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                Prefs.setMyStringPref(MainActivity.this, AppConstants.ACCESS_TOKEN, "");
                                Prefs.setMyStringPref(MainActivity.this,AppConstants.ACCESS_TOKEN_EXPIRY,"");
                                Prefs.setMyStringPref(MainActivity.this,AppConstants.USERNAME,"");
                                DaoHelper.clearUserInfo();
                                showToast("You've been logged out successfully!");
                                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                                animateToRight(MainActivity.this);
                                finish();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();

                break;
        }
    }

    private void showMobileVerificationDialog() {
        /** check if phone is not verified, if it's not
         * show the phone verification window
         * */
        if (Prefs.getMyStringPrefs(this,AppConstants.PHONE_VERIFICATION).contains(AppConstants.PHONE_NOT_VERIFIED)) {
            final VerifyMobileDialogFragment fragment = VerifyMobileDialogFragment.newInstance();
            fragment.setCancelable(false);
            fragment.setOnVerificationListener(new VerifyMobileDialogFragment.OnVerificationListener() {
                @Override
                public void onVerificationSuccessful(String result) {
                    showToast(AppConstants.OK_VERIFICATION);
                    fragment.dismiss();
                    Log.d("veri","verification successful --> " + result);
                    Prefs.setMyStringPref(MainActivity.this,AppConstants.PHONE_VERIFICATION,result);
                }

                @Override
                public void onVerificationFailed(Exception e) {
                    showToast(AppConstants.ERR_VERIFICATION);
                    Log.d("vefi","EXCEPTION ---> " + e.toString());
                }
            });
            fragment.show(getSupportFragmentManager(), "verify");
        }
    }
}

