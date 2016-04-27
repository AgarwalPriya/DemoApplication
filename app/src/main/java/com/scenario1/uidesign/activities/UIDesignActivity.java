package com.scenario1.uidesign.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.demoapp.R;
import com.scenario1.uidesign.viewAdapter.ViewPagerAdapter;
import com.scenario1.uidesign.viewFragments.PageFragment1;
import com.scenario1.uidesign.viewFragments.PageFragment2;
import com.scenario1.uidesign.viewFragments.PageFragment3;
import com.scenario1.uidesign.viewFragments.PageFragment4;

import java.util.ArrayList;
import java.util.List;

public class UIDesignActivity extends FragmentActivity {

    private static final String TAG = "UIDesignActivity";
    private static final int PAGE_MARGIN = 16;
    /**
     * The number of tabs to show in this app.
     */
    private static final int NUM_OF_TABS = 5;
    /**
     * The number of pages to show in this app.
     */
    private static final int NUM_OF_PAGES = 4;
    /**
     * The pager indicator, which indicates the page in the viewPager.
     */
    private LinearLayout mPageIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_main);

        setupTablayout();
        setupViewPager();
    }

    /**
     * populate the TabLayout and
     * handle the tab selection
     */
    private void setupTablayout() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        if (tabLayout != null) {
            for (int i = 1; i <= NUM_OF_TABS; i++) {
                tabLayout.addTab(tabLayout.newTab()
                        .setText(getString(R.string.tab_item)+ i));
            }
            /* since by default first tab is selected, set the text view to the selected item text value */
            setupTabTextDisplayLayout(getString(R.string.tab_item)
                                        +(tabLayout.getSelectedTabPosition() + 1));

            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    setupTabTextDisplayLayout((String) tab.getText());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
        }
    }

    /**
     * Set up view pager layout
     * handle the onscroll event, and inflate the page indicator
     */
    private void setupViewPager() {
        // Instantiate a ViewPager and a PagerAdapter.
        /**
         *The pager widget, which handles animation and allows swiping horizontally to access previous
         *and next wizard steps.
         */
        ViewPager mPager = (ViewPager) findViewById(R.id.view_pager);
        /* The pager adapter, which provides the pages to the view pager widget.*/
        ViewPagerAdapter mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),getFragments());
        if (mPager != null) {

            if (mPagerAdapter != null) {
                mPagerAdapter.setNumberOfPages(NUM_OF_PAGES);
                List<Fragment> fragments = getFragments();
                mPager.setAdapter(mPagerAdapter);
            }
             /* set margin between fragment pages, to make it distinguishable */
            mPager.setPageMargin(PAGE_MARGIN);
            mPager.setCurrentItem(0);
            /** Inflate page indicator layout
             *  and set it to first page, on launch
             */
            mPageIndicator = (LinearLayout) findViewById(R.id.page_indicator);
            setPageIndicator(0);
            // Attach the page change listener
            mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                // This method will be invoked when a new page is selected.
                @Override
                public void onPageSelected(int position) {
                    /**  Remove the old indicator layout
                     * and add a new one with the selected  position                     *
                     */
                    if (mPageIndicator != null) {
                        mPageIndicator.removeAllViews();
                    }
                    setPageIndicator(position);
                }
                // This method will be invoked when the current page is scrolled
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }
                /** Called when the scroll state changes:
                 * state :
                 * SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
                 */
                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }
    }
    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(PageFragment1.newInstance("FirstFragment"));
        fList.add(PageFragment2.newInstance("SecondFragment"));
        fList.add(PageFragment3.newInstance("ThirdFragment"));
        fList.add(PageFragment4.newInstance("FourthFragment"));
        return fList;
    }

    /**
     * setup page indicator for the view pager
     * @param pos, selectd page ion
     */
    private void setPageIndicator(int pos) {
        for (int i = 0; i < NUM_OF_PAGES; i++) {
            try {
                View circle = LayoutInflater.from(UIDesignActivity.this).inflate(R.layout.pageindicator, null);
                ImageView circleIcon = (ImageView) circle.findViewById(R.id.circleicon);
                circleIcon.setSelected(i == pos);
                mPageIndicator.addView(circle);
            } catch (Exception e) {
                Log.e (TAG,"Error inflating the page indicator layout");
            }
        }
    }

    /**
     * Update textView , on tab item selection
     * @param textValue, selected tabitem's text
     */
    private void setupTabTextDisplayLayout(String textValue) {
        /**
         * The text view which which will display teh selected tab item
         */
        TextView tabTextDisplay = (TextView) findViewById(R.id.tabItemText);
        if (tabTextDisplay != null && textValue != null) {
            tabTextDisplay.setText(textValue);
        }
    }

    /** onClick listener for buttons
     * @param view
     */
    public void buttonOnClick(View view) {
        /**
         * The layout which will change color , according to the button clicked in it.
         */
        RelativeLayout btnLayout = (RelativeLayout) findViewById(R.id.buttonLayout);
        if (btnLayout == null) {
            Log.e(TAG, "Button layout is null");
            return;
        }
        switch(view.getId()) {
            case R.id.btn1:
                btnLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                        R.color.indianRed));
                break;

            case R.id.btn2:
                btnLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                        R.color.steelBlue));
                break;

            case R.id.btn3:
                btnLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                        R.color.oliveGreen));
                break;

            default:
                btnLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                        R.color.lightGray));

        }
    }
    @Override
    protected void onDestroy() {
        //set objects to null, so that they can be cleaned up by GC whenever, it runs
        //local objects will be GCed as they scope out.
        super.onDestroy();
    }
}