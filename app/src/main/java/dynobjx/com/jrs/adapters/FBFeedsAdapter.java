package dynobjx.com.jrs.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;


import java.text.ParseException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import dynobjx.com.jrs.R;
import dynobjx.com.jrs.activities.FBActivity;
import dynobjx.com.jrs.models.FacebookFeeds;


/**
 * Created by rsbulanon on 6/13/15.
 */
public class FBFeedsAdapter extends RecyclerView.Adapter<FBFeedsAdapter.ViewHolder> {

    private ArrayList<FacebookFeeds> feeds;
    private Context context;
    private FBActivity activity;
    private ImageLoader imageLoader;
    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

    public FBFeedsAdapter(Context context, ArrayList<FacebookFeeds> feeds) {
        this.context = context;
        this.feeds = feeds;
        this.activity = (FBActivity)context;
        this.imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_facebook_feeds, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView ivProfilePic;
        TextView tvPostedBy;
        TextView tvDatePosted;
        TextView tvFeed;
        Toolbar toolbar;

        ViewHolder(View view) {
            super(view);
            cv = (CardView)view.findViewById(R.id.cardView);
            ivProfilePic = (ImageView)view.findViewById(R.id.ivProfilePic);
            tvPostedBy = (TextView)view.findViewById(R.id.tvPostedBy);
            tvDatePosted = (TextView)view.findViewById(R.id.tvDatePosted);
            tvFeed = (TextView)view.findViewById(R.id.tvFeed);
            toolbar = (Toolbar)view.findViewById(R.id.tbMenu);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int i) {
        holder.tvPostedBy.setText(feeds.get(i).getPostedBy());
        try {
            Date datePosted = sdf.parse(feeds.get(i).getDatePosted());
            holder.tvDatePosted.setText(dateFormat.format(datePosted));
        } catch (ParseException e) {
            Log.d("date","error in parsing date");
        }
        holder.tvFeed.setText(feeds.get(i).getMessage());
        imageLoader.displayImage(feeds.get(i).getPicURL(), holder.ivProfilePic);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
