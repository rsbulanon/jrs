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
import android.widget.ImageView;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.List;

import dynobjx.com.jrs.R;
import dynobjx.com.jrs.activities.BranchDetailsActivity;
import dynobjx.com.jrs.activities.BranchInMapActivity;
import dynobjx.com.jrs.activities.BranchLocatorActivity;
import dynobjx.com.jrs.dao.Branch;

/**
 * Created by rsbulanon on 7/23/15.
 */
public class BranchesRecylerAdapter extends RecyclerView.Adapter<BranchesRecylerAdapter.ViewHolder> {

    private List<Branch> branches;
    private Context context;
    private BranchLocatorActivity activity;

    public BranchesRecylerAdapter(Context context, List<Branch> branches) {
        this.context = context;
        this.branches = branches;
        this.activity = (BranchLocatorActivity)context;
    }

    @Override
    public int getItemCount() {
        return branches.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_branches, parent, false);
        ViewHolder  holder = new ViewHolder (v);
        return holder;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView tvBranchName;
        TextView tvBranchAddress;
        TextView tvContactPerson;
        TextView tvContactNos;
        Toolbar toolbar;

        ViewHolder (View view) {
            super(view);
            cv = (CardView)view.findViewById(R.id.cardView);
            tvBranchName = (TextView)view.findViewById(R.id.tvBranchName);
            tvBranchAddress = (TextView)view.findViewById(R.id.tvBranchAddress);
            tvContactPerson = (TextView)view.findViewById(R.id.tvContactPerson);
            tvContactNos = (TextView)view.findViewById(R.id.tvContactNos);
            toolbar = (Toolbar)view.findViewById(R.id.tbMenu);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int i) {
        final Branch branch = branches.get(i);
        holder.tvBranchName.setText(branch.getBranchName());
        holder.tvBranchAddress.setText(branch.getBranchAddress());
        holder.tvContactPerson.setText(branch.getContactPerson());
        holder.tvContactNos.setText(branch.getContactNumbers());
        holder.toolbar.getMenu().clear();
        holder.toolbar.inflateMenu(R.menu.menu_branches);
        holder.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_branch_details:
                        Intent i = new Intent(activity, BranchDetailsActivity.class);
                        i.putExtra("branchName",branch.getBranchName());
                        i.putExtra("branchId",branch.getBranchId());
                        activity.startActivity(i);
                        activity.animateToLeft(activity);
                        break;
                    case R.id.action_view_in_map:
                        Intent intent = new Intent(activity, BranchInMapActivity.class);
                        intent.putExtra("latitude",branch.getLatitude());
                        intent.putExtra("longitude",branch.getLongitude());
                        intent.putExtra("branchName",branch.getBranchName());
                        activity.startActivity(intent);
                        activity.animateToLeft(activity);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
