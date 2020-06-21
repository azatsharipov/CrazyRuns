package com.example.crazyruns.game;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.crazyruns.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaitingOponentsFragment extends Fragment {


    public WaitingOponentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_waiting_oponents, container, false);
    }

}
