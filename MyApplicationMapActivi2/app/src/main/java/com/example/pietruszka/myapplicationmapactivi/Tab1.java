package com.example.pietruszka.myapplicationmapactivi;

/**
 * Created by Pietruszka on 27.03.2018.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;


public class Tab1 extends Fragment {

    View rootView;
    private RecyclerView myrecyclerview;
    private List<String> dates1;
    private List<String> history;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dates1=new ArrayList<>();
        history=new ArrayList<>();
        for (int i=0;i<6;i++){
            dates1.add("Pytanie"+(i+1));
            history.add("Odpowiedz"+(i+1));
        }

        //odebranie maila
        //Bundle args = getArguments();
        //Log.d("msg","odebralem tab1");
        //Log.d("tab1",Double.toString(args.getStringArrayList("cos").size()));
        //historiaPrzejazdow=args.getStringArrayList("cos");
        //List<String> tab1=new ArrayList<>();
        //for(int i=0;i<historiaPrzejazdow.size();i++){
        //    tab1.add(historiaPrzejazdow.get(i));
        //}

        rootView = inflater.inflate(R.layout.tab1, container, false);
        myrecyclerview= rootView.findViewById(R.id.faqR);
        RecyclerViewAdapterInformacje recyclerAdapter= new RecyclerViewAdapterInformacje(getContext(),dates1 ,history );
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerAdapter);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
