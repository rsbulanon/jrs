package dynobjx.com.jrs.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by rsbulanon on 6/23/15.
 */
public class BranchesTabAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;

    public BranchesTabAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Branches by keyword";
            case 1:
            default:
                return "Branches Near Me";
        }
    }
}
