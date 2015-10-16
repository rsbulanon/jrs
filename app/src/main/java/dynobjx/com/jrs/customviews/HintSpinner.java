package dynobjx.com.jrs.customviews;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import dynobjx.com.jrs.R;

/**
 * Decorator Adapter to allow a Spinner to show a 'Nothing Selected...' initially
 * displayed instead of the first choice in the Adapter.
 */
public class HintSpinner implements SpinnerAdapter, ListAdapter {

    protected static final int EXTRA = 1;
    protected SpinnerAdapter adapter;
    protected Context context;
    protected int hintLayout;
    protected int customDropDownLayout;
    protected LayoutInflater layoutInflater;
    protected int textColor;
    protected int textSize;
    protected String hint;

    public HintSpinner(SpinnerAdapter spinnerAdapter, int hintLayout,
                       Context context, int customDropDownLayout, int textColor,
                       int textSize, String hint) {
        this(spinnerAdapter, hintLayout, customDropDownLayout, context, textColor, textSize, hint);
    }

    public HintSpinner(SpinnerAdapter spinnerAdapter,
                       int hintLayout, int customDropDownLayout, Context context,
                       int textColor, int textSize, String hint) {
        this.adapter = spinnerAdapter;
        this.context = context;
        this.hintLayout = hintLayout;
        this.textColor = textColor;
        this.textSize = textSize;
        this.hint = hint;
        this.customDropDownLayout = customDropDownLayout;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            return getCustomSpinnerView(parent);
        }
        return adapter.getView(position - EXTRA, null, parent); // Could re-use
    }

    protected View getCustomSpinnerView(ViewGroup parent) {
        TextView view = (TextView)layoutInflater.inflate(hintLayout, parent, false);
        view.setText(hint);
        view.setTextColor(textColor);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            Log.d("drop", "if");
            return customDropDownLayout == -1 ?
                    new View(context) :
                    getCustomSpinnerDropDown(parent);
        } else {
            Log.d("drop", "else");
        }
        return adapter.getDropDownView(position - EXTRA, null, parent);
    }

    protected View getCustomSpinnerDropDown(ViewGroup parent) {
        TextView view = (TextView)layoutInflater.inflate(R.layout.custom_spinner_drop_down, parent, false);
        view.setTextColor(textColor);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
        return view;
    }

    @Override
    public int getCount() {
        int count = adapter.getCount();
        return count == 0 ? 0 : count + EXTRA;
    }

    @Override
    public Object getItem(int position) {
        return position == 0 ? null : adapter.getItem(position - EXTRA);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position >= EXTRA ? adapter.getItemId(position - EXTRA) : position - EXTRA;
    }

    @Override
    public boolean hasStableIds() {
        return adapter.hasStableIds();
    }

    @Override
    public boolean isEmpty() {
        return adapter.isEmpty();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        adapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        adapter.unregisterDataSetObserver(observer);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return position == 0 ? false : true;
    }
}
