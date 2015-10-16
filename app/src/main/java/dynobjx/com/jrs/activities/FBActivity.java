package dynobjx.com.jrs.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.adapters.FBFeedsAdapter;
import dynobjx.com.jrs.fragments.FBCommentDialogFragment;
import dynobjx.com.jrs.helpers.AppConstants;
import dynobjx.com.jrs.models.FacebookFeeds;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

/**
 * Created by rsbulanon on 7/30/15.
 */
public class FBActivity extends BaseActivity {

    @Bind(R.id.rvFeeds) RecyclerView rvFeeds;
    @Bind(R.id.btnWrite) FloatingActionButton btnWrite;
    @Bind(R.id.swipeRefresh) SwipeRefreshLayout swipeRefresh;
    private CallbackManager callbackManager;
    private boolean refresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        ButterKnife.bind(this);
        initActionBar("JRS Offical FB Page");

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvFeeds.setLayoutManager(llm);

        /** facebook integration */
        FacebookSdk.sdkInitialize(this);
        Log.d("fb", "fb sdk initialized");
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("feeds", "on success");
                getFeeds();
            }

            @Override
            public void onCancel() {
                Log.d("feeds", "on cancel");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("feeds", "fb error --> " + e.toString());
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("swipe","must refresh");
                getFeeds();
            }
        });
    }

    @OnClick(R.id.btnWrite)
    public void writeInWall() {
        final FBCommentDialogFragment fragment = FBCommentDialogFragment.newInstance();
        fragment.setOnWallPostListener(new FBCommentDialogFragment.OnWallPostListener() {
            @Override
            public void onPostSuccessful() {
                fragment.dismiss();
                showToast(AppConstants.OK_FB_WALL_POST);
                refresh = true;
                showProgressDialog("Facebook","Refreshing feeds, Please wait...");
                getFeeds();
            }

            @Override
            public void onPostFailed(FacebookRequestError error) {
                fragment.dismiss();
                showToast(error.getErrorMessage());
            }
        });
        fragment.show(getSupportFragmentManager(), "post");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        animateToRight(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void getFeeds() {
        btnWrite.setVisibility(View.INVISIBLE);
        final Bundle params = new Bundle();
        params.putString("fields", "id,from,message,created_time");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/JRS.Express.OfficialFanPage/feed",
               // "/1592115847689614/feed",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject json = response.getJSONObject();
                        Log.d("feeds",""+json);
                        ArrayList<FacebookFeeds> feeds = new ArrayList<>();
                        try {
                            JSONArray arr = json.getJSONArray("data");
                            for (int i = 0; i < arr.length(); i++) {
                                String from = arr.getJSONObject(i).getJSONObject("from").getString("name");
                                String id = arr.getJSONObject(i).getJSONObject("from").getString("id");
                                String message = arr.getJSONObject(i).getString("message");
                                String datePosted = arr.getJSONObject(i).getString("created_time");
                                feeds.add(new FacebookFeeds(from, id, message, datePosted));
                            }
                        } catch (JSONException e) {
                            Log.d("feed","error -->> " + e.toString());
                        }
                        FBFeedsAdapter adapter = new FBFeedsAdapter(FBActivity.this, feeds);
                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
                        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
                        rvFeeds.setAdapter(scaleAdapter);
                        btnWrite.setVisibility(View.VISIBLE);
                        if (swipeRefresh.isRefreshing()) {
                            Log.d("swipe","swipe is refreshing");
                            swipeRefresh.setRefreshing(false);
                        }
                        if (refresh) {
                            refresh = false;
                            dismissProgressDialog();
                        }
                    }
                }
        ).executeAsync();
    }

}
