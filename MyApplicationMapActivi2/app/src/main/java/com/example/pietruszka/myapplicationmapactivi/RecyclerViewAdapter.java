package com.example.pietruszka.myapplicationmapactivi;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;

import static android.app.Activity.*;

/**
 * Created by Pietruszka on 27.03.2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    Context mContext;
    List<String> itemL;
    List<String> item2;
    List<String> itemId;
    public RecyclerViewAdapter(Context mContext, List<String> itemL, List<String> item2,List<String> itemId) {
        this.mContext = mContext;
        this.itemL = itemL;
        this.item2 = item2;
        this.itemId = itemId;
    }

    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v= LayoutInflater.from(mContext).inflate(R.layout.item_his,parent,false);
        MyViewHolder vHolder=new MyViewHolder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.MyViewHolder holder, final int position) {
        holder.tv_date.setText(itemL.get(position));
        holder.tv_det.setText(item2.get(position));
        holder.txtOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupmenu=new PopupMenu(mContext,holder.txtOption);
                popupmenu.inflate(R.menu.option_menu);
                popupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId()){
                            case R.id.menu_item_share:
                                Toast.makeText(mContext,"Sharing",Toast.LENGTH_LONG).show();
                                Intent sharIntent=new Intent(Intent.ACTION_SEND);
                                sharIntent.setType("text/plain");
                                String origin = itemL.get(position).replaceAll(" ", "").split("-", 2)[0];
                                String destination = itemL.get(position).replaceAll(" ", "").split("-", 2)[1];
                                sharIntent.putExtra(Intent.EXTRA_TEXT,"https://maps.googleapis.com/maps/api/directions/json?origin=" + origin + "&destination=" + destination + "&mode=bicycling");
                                sharIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(Intent.createChooser(sharIntent, "Share via"));
                                break;

                            case R.id.menu_item_again:
                                final String[] value=new String[4];
                                FirebaseDatabase.getInstance().getReference().child("user1")
                                                .child("saved").child(itemId.get(position)).child("startWsp").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot snapshot) {
                                                try {
                                                    if (snapshot.getValue() != null) {
                                                        try {
                                                            Log.e("lat", "" + snapshot.child("latitude").getValue()); // your name values you will get here
                                                            Log.e("lon", "" + snapshot.child("longitude").getValue());
                                                            value[0]=snapshot.child("latitude").getValue().toString();
                                                            value[1]=snapshot.child("longitude").getValue().toString();
                                                            Toast.makeText(mContext,"Wyznacz z tymi samymi parametrami"+" "+snapshot.child("latitude").getValue()+" "+snapshot.child("longitude").getValue(),Toast.LENGTH_LONG).show();

                                                      
                                                      
                                                      

                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        Log.e("TAG", " it's null.");
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                FirebaseDatabase.getInstance().getReference().child("user1")
                                        .child("saved").child(itemId.get(position)).child("koniecWsp").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        try {
                                            if (snapshot.getValue() != null) {
                                                try {
                                                    Log.e("lat", "" + snapshot.child("latitude").getValue()); // your name values you will get here
                                                    Log.e("lon", "" + snapshot.child("longitude").getValue());
                                                    value[2]=snapshot.child("latitude").getValue().toString();
                                                    value[3]=snapshot.child("longitude").getValue().toString();
                                                    Toast.makeText(mContext,"Wyznacz z tymi samymi parametrami"+" "+snapshot.child("latitude").getValue()+" "+snapshot.child("longitude").getValue(),Toast.LENGTH_LONG).show();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                Log.e("TAG", " it's null.");
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });

                                Intent i = new Intent(mContext, MapsActivity.class);
                                i.putExtra("key",value);
                                //startActivity(i);

                                break;

                            case R.id.menu_item_delete:

                                FirebaseDatabase.getInstance().getReference("user1").child("saved").child(itemId.get(position)).setValue(null);
                                Log.d("gf",itemId.get(position));
                                Toast.makeText(mContext,"Usunięto",Toast.LENGTH_LONG).show();
                                break;
                        }
                        return false;
                    }
                });
                popupmenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemL.size();
    }

    public static void restartActivity(Activity activity){
            activity.finish();
            activity.startActivity(activity.getIntent());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_date;
        private TextView tv_det;
        public TextView txtOption;
        public MyViewHolder(View itemView){
            super(itemView);

            tv_date=itemView.findViewById((R.id.data));
            tv_det=itemView.findViewById(R.id.detail);
            txtOption=itemView.findViewById(R.id.txtOpt);
        }
    }
}
