package dynobjx.com.jrs.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import dynobjx.com.jrs.R;


/**
 * Created by rsbulanon on 5/25/15.
 */
public class HashTagView extends RelativeLayout {

    private TextView tvHashTag;
    private TextView tvClose;

    public HashTagView(Context context) {
        super(context);
    }

    public HashTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HashTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HashTagView(Context context, String hashTag) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.hashtag_view, null);
        tvClose = (TextView)view.findViewById(R.id.tvClose);
        tvHashTag = (TextView)view.findViewById(R.id.tvHashTag);
        tvHashTag.setText(hashTag);
        addView(view);
    }

    public HashTagView(Context context, String hashTag, int bgColor) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.hashtag_view, null);
        tvClose = (TextView)view.findViewById(R.id.tvClose);
        tvHashTag = (TextView)view.findViewById(R.id.tvHashTag);
        tvHashTag.setText(hashTag);
        view.setBackgroundColor(bgColor);
        addView(view);
    }

    public void createView(Context context,String hashTag) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.hashtag_view, null);
        tvClose = (TextView)view.findViewById(R.id.tvClose);
        tvHashTag = (TextView)view.findViewById(R.id.tvHashTag);
        tvHashTag.setText(hashTag);
        addView(view);
    }

    public void createView(Context context, Typeface tfSegoe, Typeface tfBootstrap,
                           String hashTag, int bgColor) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.hashtag_view, null);
        tvClose = (TextView)view.findViewById(R.id.tvClose);
        tvHashTag = (TextView)view.findViewById(R.id.tvHashTag);
        tvClose.setTypeface(tfBootstrap);
        view.setBackgroundColor(bgColor);
        tvHashTag.setTypeface(tfSegoe);
        tvHashTag.setText(hashTag);
        addView(view);
    }

    public void setHashTag(String hashTag) {
        this.tvHashTag.setText(hashTag);
    }

    public String getHashTag() {
        return tvHashTag.getText().toString();
    }

    public TextView tvCloseHashTag() {
        return (TextView)findViewById(R.id.tvClose);
    }
}
