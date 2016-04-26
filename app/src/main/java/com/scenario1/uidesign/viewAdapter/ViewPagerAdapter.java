package com.scenario1.uidesign.viewAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * A simple pager adapter that represents 4  ScreenSlidePageFragment objects, in
 * sequence.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private int no_of_pages = 0;
    private List<Fragment> fragments;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }
    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        if (fragments != null)
            return fragments.size();
        else
            return no_of_pages;
    }

   public void setNumberOfPages (int nPages) {
        no_of_pages = nPages;
    }
}
