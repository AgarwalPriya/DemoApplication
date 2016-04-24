package com.scenario1.uidesign;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.demoapp.R;

/**
 * Fragment4 of ViewPager
 */
public class PageFragment4 extends Fragment {
    TextView frag_textview = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frag_view = inflater.inflate(R.layout.page_fragment4, container, false);
        frag_textview = (TextView) frag_view.findViewById(R.id.tvFrag4);
        frag_textview.setText(getArguments().getString("msg"));
        frag_view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               Toast.makeText(v.getContext(),
                       getString(R.string.selected)+ getArguments().getString("msg"), Toast.LENGTH_SHORT).show();
            }
        });
        return frag_view;
    }

    public static PageFragment4 newInstance(String text) {

        PageFragment4 f = new PageFragment4();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

}
