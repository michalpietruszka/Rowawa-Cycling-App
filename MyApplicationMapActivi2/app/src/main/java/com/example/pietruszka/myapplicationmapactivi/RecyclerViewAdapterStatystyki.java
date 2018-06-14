package com.example.pietruszka.myapplicationmapactivi;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by Pietruszka on 27.03.2018.
 */

public class RecyclerViewAdapterStatystyki extends RecyclerView.Adapter<RecyclerViewAdapterStatystyki.MyViewHolder>{

    Context mContext;
    List<String> itemL;
    List<String> item2;
    List<String> itemId;
    public RecyclerViewAdapterStatystyki(Context mContext, List<String> itemL, List<String> item2, List<String> itemId) {
        this.mContext = mContext;
        this.itemL = itemL;
        this.item2 = item2;
        this.itemId = itemId;
    }

    @Override
    public RecyclerViewAdapterStatystyki.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v= LayoutInflater.from(mContext).inflate(R.layout.item_stat,parent,false);
        MyViewHolder vHolder=new MyViewHolder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapterStatystyki.MyViewHolder holder, final int position) {
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
                                sharIntent.putExtra(Intent.EXTRA_TEXT,"Tutaj nasz tekst");
                                sharIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(Intent.createChooser(sharIntent, "Share via"));
                                break;
                            case R.id.menu_item_delete:

                                FirebaseDatabase.getInstance().getReference("user1").child("history").child(itemId.get(position)).setValue(null);
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
