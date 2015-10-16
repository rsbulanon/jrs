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

import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.activities.FBActivity;
import dynobjx.com.jrs.activities.MainActivity;
import dynobjx.com.jrs.helpers.ApiRequestHelper;
import dynobjx.com.jrs.helpers.AppConstants;
import dynobjx.com.jrs.helpers.Prefs;

/**
 * Created by rsbulanon on 8/5/15.
 */
public class FBCommentDialogFragment extends DialogFragment {

    @Bind(R.id.etComment) EditText etComment;
    private View view;
    private FBActivity activity;
    private OnWallPostListener onWallPostListener;

    public static FBCommentDialogFragment newInstance() {
        FBCommentDialogFragment frag = new FBCommentDialogFragment();
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
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_fb_comment, null);
        ButterKnife.bind(this, view);
        activity = (FBActivity)getActivity();
        final Dialog mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        return mDialog;
    }

    @OnClick(R.id.btnPost)
    public void writeComment() {
        if (etComment.getText().toString().isEmpty()) {
            etComment.setError(AppConstants.WARNING_FIELD_REQUIRED);
            etComment.requestFocus();
        } else {
            if (activity.isNetworkAvailable()) {
                activity.showProgressDialog("Facebook", "Writing your comment, Please wait...");
                Bundle parameters = new Bundle();
                parameters.putString("message", etComment.getText().toString());
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/JRS.Express.OfficialFanPage/feed",
                        parameters,
                        HttpMethod.POST,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                activity.dismissProgressDialog();
                                if (response.getError() == null) {
                                    onWallPostListener.onPostSuccessful();
                                } else {
                                    onWallPostListener.onPostFailed(response.getError());
                                }
                                Log.d("fb", "response --> " + response.getRawResponse());
                            }
                        }
                ).executeAsync();
            } else {
                activity.showToast(AppConstants.ERR_CONNECTION);
            }
        }
    }

    public interface OnWallPostListener {
        void onPostSuccessful();
        void onPostFailed(FacebookRequestError error);
    }

    public void setOnWallPostListener(OnWallPostListener onWallPostListener) {
        this.onWallPostListener = onWallPostListener;
    }
}
