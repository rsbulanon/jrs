package dynobjx.com.jrs.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dynobjx.com.jrs.R;
import dynobjx.com.jrs.activities.BaseActivity;
import dynobjx.com.jrs.activities.BranchLocatorActivity;
import dynobjx.com.jrs.adapters.BranchesRecylerAdapter;
import dynobjx.com.jrs.dao.Branch;
import dynobjx.com.jrs.helpers.DaoHelper;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

/**
 * Created by rsbulanon on 7/9/15.
 */
public class BranchesByKeywordFragment extends Fragment {

    @Bind(R.id.rvBranches) RecyclerView rvBranches;
    @Bind(R.id.swipeRefresh) SwipeRefreshLayout swipeRefresh;
    private BaseActivity activity;
    private ArrayList<Branch> branches = new ArrayList<>();

    public static BranchesByKeywordFragment newInstance() {
        return new BranchesByKeywordFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_branches_by_keyword,null,false);
        ButterKnife.bind(this, view);
        activity = (BaseActivity)getActivity();
        BranchesRecylerAdapter adapter = new BranchesRecylerAdapter(getActivity(),branches);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        rvBranches.setAdapter(scaleAdapter);
        rvBranches.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshBranchesList(DaoHelper.getAllBranches());
                swipeRefresh.setRefreshing(false);
            }
        });
        /** load branches from local DB */
        if (DaoHelper.getBranchesCount() > 0) {
            refreshBranchesList(DaoHelper.getAllBranches());
        }
        return view;
    }

    @OnClick(R.id.btnSearch)
    public void showSearchDialogFragment() {
        final BranchByKeywordDialogFragment fragment = BranchByKeywordDialogFragment.newInstance();
        fragment.setOnEnteredKeywordListener(new BranchByKeywordDialogFragment.OnEnteredKeywordListener() {
            @Override
            public void onEnteredKeyword(String keyword) {
                if (activity.getBranchesByKeyword(keyword).size() == 0) {
                    activity.showToast("No result/s found!");
                } else {
                    refreshBranchesList(activity.getBranchesByKeyword(keyword));
                }
                fragment.dismiss();
            }
        });
        fragment.show(getFragmentManager(), "search");
    }

    public void refreshBranchesList(List<Branch> branches) {
        this.branches.clear();
        this.branches.addAll(branches);
        rvBranches.getAdapter().notifyDataSetChanged();
    }
}
