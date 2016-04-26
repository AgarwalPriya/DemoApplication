package com.scenario1.uidesign.viewFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.demoapp.Constants;
import com.app.demoapp.R;

/**
 * Fragment3 of ViewPager
 */
public class PageFragment3 extends Fragment {
    TextView frag_textview = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frag_view = inflater.inflate(R.layout.page_fragment3, container, false);

        frag_textview = (TextView) frag_view.findViewById(R.id.tvFrag3);

        frag_textview.setText(getArguments().getString(Constants.FRAG_MSG));
        frag_view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(v.getContext(),
                        getString(R.string.selected)+ getArguments().getString(Constants.FRAG_MSG), Toast.LENGTH_SHORT).show();
            }
        });
        return frag_view;
    }

    public static PageFragment3 newInstance(String text) {

        PageFragment3 f = new PageFragment3();
        Bundle b = new Bundle();
        b.putString(Constants.FRAG_MSG, text);
        f.setArguments(b);
        return f;
    }

}
