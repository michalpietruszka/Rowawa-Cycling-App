package com.example.pietruszka.myapplicationmapactivi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Pietruszka on 12.04.2018.
 */

public class ulubione extends AppCompatActivity {


    View rootView;
    private RecyclerView myrecyclerview;
    private RecyclerViewAdapter adapter;
    private List<String> mainText;
    private Context mycontext;
    private List<String> detailText;
    private List<String> idOfSaved;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ulubione_layout);
        mycontext=this;
        mainText= new ArrayList<>();
        detailText= new ArrayList<>();
        idOfSaved =new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user1").child("saved");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        collectData((Map<String,Object>) dataSnapshot.getValue());
                        myrecyclerview = findViewById(R.id.ulubioneRecView);
                        myrecyclerview.setHasFixedSize(true);
                        myrecyclerview.setLayoutManager(new LinearLayoutManager(mycontext));
                        adapter = new RecyclerViewAdapter(mycontext, mainText, detailText,idOfSaved);
                        myrecyclerview.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(mycontext, "Brak połączenia z bazą!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void collectData(Map<String,Object> users) {
        for (Map.Entry<String, Object> entry : users.entrySet()){
            Map singleUser = (Map) entry.getValue();
            String[] parts = singleUser.get("start").toString().split("\\,");
            String[] parts1 = singleUser.get("koniec").toString().split(",");
            mainText.add(parts[0]+" - "+parts1[0]);
            detailText.add((String) singleUser.get("czas")+" ,"+(String) singleUser.get("dystans"));
            idOfSaved.add(entry.getKey());
        }
        Log.d("mk",Integer.toString(mainText.size()));
    }
}
