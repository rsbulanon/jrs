package dynobjx.com.jrs.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dynobjx.com.jrs.R;
import dynobjx.com.jrs.activities.BranchDetailsActivity;
import dynobjx.com.jrs.activities.BranchInMapActivity;
import dynobjx.com.jrs.activities.BranchLocatorActivity;
import dynobjx.com.jrs.activities.TrackResultActivity;
import dynobjx.com.jrs.dao.Branch;
import dynobjx.com.jrs.helpers.AppConstants;
import dynobjx.com.jrs.models.Track;

/**
 * Created by rsbulanon on 8/5/15.
 */
public class TrackingRecylerAdapter extends RecyclerView.Adapter<TrackingRecylerAdapter.ViewHolder> {

    private List<Track> tracking;
    private Context context;
    private TrackResultActivity activity;

    public TrackingRecylerAdapter(Context context, List<Track> tracking) {
        this.context = context;
        this.tracking = tracking;
        this.activity = (TrackResultActivity) context;
    }

    @Override
    public int getItemCount() {
        return tracking.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tracking, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView tvBill;
        TextView tvCode;
        TextView tvOrigin;
        TextView tvDestination;
        TextView tvService;
        TextView tvPOD;
        TextView tvStatus;
        TextView tvReceiver;
        TextView tvDate;
        TextView tvTime;


        ViewHolder(View view) {
            super(view);
            cv = (CardView) view.findViewById(R.id.cardView);
            tvPOD = (TextView) view.findViewById(R.id.tvPOD);
            tvStatus = (TextView) view.findViewById(R.id.tvStatus);
            tvReceiver = (TextView) view.findViewById(R.id.tvReceiver);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int i) {
        final Track track = tracking.get(i);
        holder.tvPOD.setText(track.getSubAirBill() +"");
        holder.tvStatus.setText(track.getDeliveryStatus()+"");
        holder.tvReceiver.setText(track.getReciever()+"");
        holder.tvDate.setText(track.getStatusDate());
        holder.tvTime.setText(track.getStatusTime()+"");
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}