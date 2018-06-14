package com.example.pietruszka.myapplicationmapactivi;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.CalendarView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.Calendar;

import java.text.SimpleDateFormat;


/**
 * Created by Weronika on 11.05.2018.
 */

public class statystykiActivity extends AppCompatActivity {

    View rootView;
    private RecyclerView myrecyclerview;
    private RecyclerViewAdapterStatystyki adapter;
    private List<String> mainText;
    private Context mycontext;
    private List<String> detailText;
    private List<String> idOfSaved;

    private Integer[] data = {1900, 0, 0};
    //private static String[] parts;
    //private static String[] parts1;
    //private static String[] time;
    private double czas_laczny;
    private double dystans_laczny;
    private Map<String,Object> all_data;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //setContentView(R.layout.XXX);
        setContentView(R.layout.activity_stat);
        CalendarView simpleCalendarView = (CalendarView) findViewById(R.id.simpleCalendarView);
        final TextView Text_czas = (TextView) findViewById(R.id.czas);
        final TextView Text_dystans = (TextView) findViewById(R.id.dystans);
        //long selectedDate = simpleCalendarView.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(new Date(simpleCalendarView.getDate()));
        //String date = "8/5/2018";
        String parts_date[] = date.split("/");

        data[2] = Integer.valueOf(parts_date[0]);
        data[1] = Integer.valueOf(parts_date[1]);
        data[0] = Integer.valueOf(parts_date[2]);

        //Calendar calendar = Calendar.getInstance();
        //calendar.set(Calendar.YEAR, year);
        //calendar.set(Calendar.MONTH, (month - 1));
        //calendar.set(Calendar.DAY_OF_MONTH, day);

        //long milliTime = calendar.getTimeInMillis();
        //simpleCalendarView.setDate(milliTime,true,true);

        mycontext=this;
        mainText= new ArrayList<>();
        detailText= new ArrayList<>();
        idOfSaved =new ArrayList<>();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user1").child("history");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        collectData((Map<String,Object>) dataSnapshot.getValue());
                        all_data = (Map<String,Object>)dataSnapshot.getValue();
                        Log.d("DataChange", Double.toString(czas_laczny));
                        Text_czas.setText("Laczny czas: " + new DecimalFormat("##.##").format(czas_laczny) + "min");
                        Text_dystans.setText("Laczny dystans: " + new DecimalFormat("##.##").format(dystans_laczny) + "km");
                        myrecyclerview = findViewById(R.id.statystykiR);
                        myrecyclerview.setHasFixedSize(true);
                        myrecyclerview.setLayoutManager(new LinearLayoutManager(mycontext));
                        adapter = new RecyclerViewAdapterStatystyki(mycontext, mainText, detailText,idOfSaved);
                        myrecyclerview.setAdapter(adapter);
                }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(mycontext, "Brak połączenia z bazą!", Toast.LENGTH_LONG).show();
                    }


                });
        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // display the selected date by using a toast
                data[0] = new Integer(year);
                data[1] = new Integer(month + 1);
                data[2] = new Integer(dayOfMonth);
                collectData(all_data);
                //Toast.makeText(getApplicationContext(), dayOfMonth + "/" + (month + 1) + "/" + year, Toast.LENGTH_LONG).show();
                Log.d("CalendarChange", Double.toString(czas_laczny));
                Text_czas.setText("Laczny czas: " + new DecimalFormat("##.##").format(czas_laczny) + "min");
                Text_dystans.setText("Laczny dystans: " + new DecimalFormat("##.##").format(dystans_laczny) + "km");
                adapter = new RecyclerViewAdapterStatystyki(mycontext, mainText, detailText,idOfSaved);
                myrecyclerview.setAdapter(adapter);
            }
        });

    }

    private void collectData(Map<String,Object> users) {
        mainText.clear();
        detailText.clear();
        idOfSaved.clear();
        czas_laczny = 0;
        dystans_laczny = 0;

        for (Map.Entry<String, Object> entry : users.entrySet()) {
            Map singleUser = (Map) entry.getValue();
            String[] parts = singleUser.get("start").toString().split("\\,");
            String[] parts1 = singleUser.get("koniec").toString().split(",");
            String[] time = singleUser.get("data").toString().split("-|T");
            String czas_str = (String) singleUser.get("czas");
            String dystans_str = (String) singleUser.get("dystans");


            for (String elem : time)
            {
                elem.trim();
                if (elem.substring(0, 1).equals("0"))
                {
                    elem.substring(1);
                }
            }
            Log.d("start", parts1[0]);
            Log.d("time", time[0]);
            Log.d("rok", Integer.toString(data[0]));
            if (Integer.valueOf(time[0]).equals(data[0]) & Integer.valueOf(time[1]).equals(data[1]))
            {
                Log.d("time", time[1]);
                Log.d("miesiac", Integer.toString(data[1]));
                String[] czas_min = czas_str.split(" ");
                String[] dystans_km = dystans_str.split(" ");
                czas_laczny += Float.parseFloat(czas_min[0]);
                Log.d("czas1", czas_str);
                Log.d("czas2", Double.toString(czas_laczny));
                dystans_laczny += Float.parseFloat(dystans_km[0].replaceAll(",",""));

                if (Integer.valueOf(time[2]).equals(data[2]))
                {
                    mainText.add(parts[0] + " - " + parts1[0]);
                    detailText.add(czas_str + " ," + dystans_str);
                    idOfSaved.add(entry.getKey());
                    Log.d("time", time[2]);
                    Log.d("dzien", Integer.toString(data[2]));
                }
            }


        }
        Log.d("mk", Integer.toString(mainText.size()));
    }


}
