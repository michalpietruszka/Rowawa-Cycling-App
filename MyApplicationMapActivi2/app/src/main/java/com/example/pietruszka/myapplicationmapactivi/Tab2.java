package com.example.pietruszka.myapplicationmapactivi;

/**
 * Created by Pietruszka on 27.03.2018.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Tab2 extends Fragment {

    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab2, container, false);
        Log.d("back","skok");
        return rootView;
    }
}
