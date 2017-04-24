package com.yunding.dut.ui.reading;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yunding.dut.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReadingOriginalFragment extends Fragment {


    public ReadingOriginalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reading_original, container, false);
    }

}
