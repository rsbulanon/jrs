package dynobjx.com.jrs.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.adapters.TrackingRecylerAdapter;
import dynobjx.com.jrs.helpers.AppConstants;
import dynobjx.com.jrs.models.Track;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public class TrackResultActivity extends BaseActivity {


    @Bind(R.id.rvTracking) RecyclerView rvTracking;
    @Bind(R.id.tvBill) TextView tvBill;
    @Bind(R.id.tvOrigin) TextView tvOrigin;
    @Bind(R.id.tvDestination) TextView tvDestination;
    @Bind(R.id.tvService) TextView tvService;
    @Bind(R.id.tvCode) TextView tvCode;
    @Bind(R.id.tvAddressee) TextView tvAddressee;
    @Bind(R.id.tvSender) TextView tvSender;
    private List<Track> tracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_result);
        initActionBar("Tracking Summary");
        ButterKnife.bind(this);

        getTrackings().clear();
        try {
            JSONArray arr = new JSONArray(getIntent().getStringExtra("result"));
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);

                int airBill = obj.getInt("Airbill");
                int trackingCode = obj.getInt("TrackingCode");
                String sender = obj.getString("Sender").equals("null") ? AppConstants.NOT_AVAIL : obj.getString("Sender");
                String service = obj.getString("Service").equals("null") ? AppConstants.NOT_AVAIL : obj.getString("Service");
                String origin = obj.getString("Origin").equals("null") ? AppConstants.NOT_AVAIL : obj.getString("Origin");
                String destination = obj.getString("Destination").equals("null") ? AppConstants.NOT_AVAIL : obj.getString("Destination");
                String addressee = obj.getString("Addressee").equals("null") ? AppConstants.NOT_AVAIL : obj.getString("Addressee");
                String originRemarks = obj.getString("OriginRemarks").equals("null") ? AppConstants.NOT_AVAIL : obj.getString("OriginRemarks");
                String subAirBill = obj.getString("SubAirbill").equals("null") ? AppConstants.NOT_AVAIL : obj.getString("SubAirbill");
                String deliveryStatus = obj.getString("DeliveryStatus").equals("null") ? AppConstants.NOT_AVAIL : obj.getString("DeliveryStatus");
                String statusDate = obj.getString("StatusDate").equals("null") ? AppConstants.NOT_AVAIL : obj.getString("StatusDate");
                String statusTime = obj.getString("StatusTime").equals("null") ? AppConstants.NOT_AVAIL : obj.getString("StatusTime");
                String station = obj.getString("Station").equals("null") ? AppConstants.NOT_AVAIL : obj.getString("Station");
                String receiver = obj.getString("Receiver").equals("null") ? AppConstants.NOT_AVAIL : obj.getString("Receiver");
                String deliveryRemarks = obj.getString("DeliveryRemarks").equals("null") ? AppConstants.NOT_AVAIL : obj.getString("DeliveryRemarks");
                String clientRef = obj.getString("ClientRef").equals("null") ? AppConstants.NOT_AVAIL : obj.getString("ClientRef");
                String dateSent = obj.getString("DateSent").equals("null") ? AppConstants.NOT_AVAIL : obj.getString("DateSent");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                getTrackings().add(new Track(airBill, trackingCode, sender, service, origin, destination, addressee, "Hello",
                        originRemarks, subAirBill, deliveryStatus, statusDate, statusTime, station, receiver, deliveryRemarks, clientRef));

                tvBill.setText(airBill + "");
                tvCode.setText(trackingCode + "");
                tvOrigin.setText(origin + "");
                tvDestination.setText(destination + "");
                tvService.setText(service + "");
                tvAddressee.setText(addressee + "");
                tvSender.setText(sender+"");
            }

        } catch (JSONException ex) {
            Log.d("error", ex.toString());
        }

        TrackingRecylerAdapter adapter = new TrackingRecylerAdapter(this,getTrackings());
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        rvTracking.setAdapter(scaleAdapter);
        rvTracking.setLayoutManager(new LinearLayoutManager(this));


    }
}
