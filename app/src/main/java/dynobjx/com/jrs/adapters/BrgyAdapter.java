package dynobjx.com.jrs.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dynobjx.com.jrs.R;

/**
 * Created by rsbulanon on 7/29/15.
 */
public class BrgyAdapter extends BaseAdapter {

    private ArrayList<String> brgy;
    private Context context;
    private LayoutInflater inflater;

    public BrgyAdapter(Context context, ArrayList<String> brgy) {
        this.context = context;
        this.brgy = brgy;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return brgy.size();
    }

    @Override
    public String getItem(int i) {
        return brgy.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.row_brgy,null);
        ((TextView)view.findViewById(R.id.tvBrgy)).setText(getItem(i));
        return view;
    }
}
