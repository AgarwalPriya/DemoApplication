package com.scenario1.uidesign;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A simple pager adapter that represents 4  ScreenSlidePageFragment objects, in
 * sequence.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private int no_of_pages = 0;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);

    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                return PageFragment1.newInstance("FirstFragment");
            case 1:
                return PageFragment2.newInstance("SecondFragment");
            case 2:
                return PageFragment3.newInstance("ThirdFragment");
            case 3:
                return PageFragment4.newInstance("FourthFragment");
            default:
                return PageFragment1.newInstance("DefaultFragment");
        }
    }

    @Override
    public int getCount() {
        return no_of_pages;
    }

   public void setNumberOfPages (int nPages) {
        no_of_pages = nPages;
    }
}
