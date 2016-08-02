package com.wy.weinxin;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends Fragment {

   private String mTitle="default";
    public static  final String TITLE="title";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments()!=null) {
            mTitle=getArguments().getString(TITLE);
        }
        TextView textView = new TextView(getActivity());
        textView.setText(mTitle);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundColor(Color.parseColor("#ffffff"));
        return textView;
    }

}
