package dynobjx.com.jrs.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.dao.BranchDetail;
import dynobjx.com.jrs.helpers.ApiRequestHelper;
import dynobjx.com.jrs.helpers.AppConstants;
import dynobjx.com.jrs.helpers.DaoHelper;
import dynobjx.com.jrs.helpers.Prefs;

/**
 * Created by rsbulanon on 8/4/15.
 */
public class BranchDetailsActivity extends BaseActivity implements ApiRequestHelper.OnAPIRequestListener {

    @Bind(R.id.tvBranchAddress) TextView tvBranchAddress;
    @Bind(R.id.tvContactNos) TextView tvContactNos;
    @Bind(R.id.tvContactPerson) TextView tvContactPerson;
    @Bind(R.id.tvEmail) TextView tvEmail;
    @Bind(R.id.ivImage) ImageView ivImage;
    @Bind(R.id.tvNoImage) TextView tvNoImage;
    private ApiRequestHelper apiRequestHelper;
    private int branchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_details);
        ButterKnife.bind(this);
        initActionBar("JRS " + getIntent().getStringExtra("branchName"));

        /** check first if branch details is not existing in local db
         *  if its null, then fetch it from API
         * */
        branchId = getIntent().getIntExtra("branchId",0);
        BranchDetail branchDetail = DaoHelper.getBranchDetailByBranchId(branchId);
        if (branchDetail == null) {
            if (isNetworkAvailable()) {
                apiRequestHelper = new ApiRequestHelper();
                apiRequestHelper.setOnAPIRequestListener(this);
                apiRequestHelper.getBranchDetailsByBranchId(branchId, Prefs.getMyStringPrefs(this, AppConstants.ACCESS_TOKEN));
            } else {
                showToast(AppConstants.ERR_CONNECTION);
                finish();
            }
        } else {
            populateData(branchDetail);
        }
    }

    @Override
    public void onRequestStart(String action) {
        Log.d("details","get branch by id");
        showProgressDialog("Branch Details","Getting branch details, Please wait...");
    }

    @Override
    public void onRequestException(Exception e) {
        dismissProgressDialog();
        showToast(AppConstants.ERR_BRANCH_DETAILS);
        Log.d("details", "error --> " + e.toString());
        finish();
    }

    @Override
    public void onRequestSuccessful(String action, String json) {
        dismissProgressDialog();
        try {
            String img = "";
            JSONObject obj = new JSONObject(json);
            if (obj.has("Message")) {
                showToast(AppConstants.ERR_BRANCH_DETAILS);
                finish();
            } else {
                String image = "";
                if (obj.has("Image")) {
                    image = obj.getString("Image");
                }
                DaoHelper.addNewBranchDetails(new BranchDetail(null,
                        obj.getInt("Id"),
                        obj.getString("FullAddress").equals("null") ? AppConstants.NOT_AVAIL : obj.getString("FullAddress"),
                        obj.getString("ContactNumbers").equals("null") ? AppConstants.NOT_AVAIL : obj.getString("ContactNumbers"),
                        obj.getString("Manager").equals("null") ? AppConstants.NOT_AVAIL : obj.getString("Manager"),
                        image, obj.getString("Email").equals("null") ? AppConstants.NOT_AVAIL : obj.getString("Email")
                        , branchId));
                populateData(DaoHelper.getBranchDetailByBranchId(branchId));
                if (image.length() > 0) {
                    new DecodeImage().execute(image);
                } else {
                    tvNoImage.setVisibility(View.VISIBLE);
                    ivImage.setVisibility(View.GONE);
                }
            }
        } catch (JSONException ex) {
            Log.d("token", "error in parsing json token --> " + ex.toString());
        }
    }

    private class DecodeImage extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... arg) {
            if (arg.length == 1 && arg[0] != null) {
                byte[] byteArray = Base64.decodeBase64(arg[0].getBytes());
                return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            } else {
                throw new IllegalArgumentException("Only 1 parameter needed, but found " + arg.length);
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            tvNoImage.setVisibility(View.GONE);
            ivImage.setVisibility(View.VISIBLE);
            Log.d("token", "set image bitmap");
            ivImage.setImageBitmap(bitmap);
        }
    }

    private void populateData(BranchDetail branchDetail) {
        tvBranchAddress.setText(branchDetail.getFullAddress());
        tvContactNos.setText(branchDetail.getContactNumbers());
        tvContactPerson.setText(branchDetail.getManagers());
        tvEmail.setText(branchDetail.getEmail());
        if (branchDetail.getImage().length() > 0) {
            new DecodeImage().execute(branchDetail.getImage());
        } else {
            tvNoImage.setVisibility(View.VISIBLE);
            ivImage.setVisibility(View.GONE);
        }
    }
}
