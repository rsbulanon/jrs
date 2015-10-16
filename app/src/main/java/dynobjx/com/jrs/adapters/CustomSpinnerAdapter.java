package dynobjx.com.jrs.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rsbulanon on 3/17/15.
 */
public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private Typeface tfSegoe;
    private Context context;
    private int textColor;
    private int size;

    public CustomSpinnerAdapter(Context context, Typeface tfSegoe) {
        super(context, android.R.layout.simple_spinner_item);
        this.tfSegoe = tfSegoe;
        this.context = context;
    }

    public CustomSpinnerAdapter(Context context, List<String> objects,
                                Typeface tfSegoe, int textColor, int size) {
        super(context, android.R.layout.simple_spinner_item, objects);
        this.tfSegoe = tfSegoe;
        this.context = context;
        this.textColor = textColor;
        this.size = size;
    }

    // Affects default (closed) state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTypeface(tfSegoe);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);
        view.setTextColor(textColor);
        return view;
    }

    public void setTextColor(int color) {
        this.textColor = color;
    }

    // Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTypeface(tfSegoe);
        view.setTextColor(Color.BLACK);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);
        return view;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}