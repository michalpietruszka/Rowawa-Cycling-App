package com.example.pietruszka.myapplicationmapactivi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Pietruszka on 27.03.2018.
 */

public class RecyclerViewAdapterInformacje extends RecyclerView.Adapter<RecyclerViewAdapterInformacje.MyViewHolder>{

    Context mContext;
    List<String> itemL;
    List<String> item2;

    public RecyclerViewAdapterInformacje(Context mContext, List<String> itemL, List<String> item2) {
        this.mContext = mContext;
        this.itemL = itemL;
        this.item2 = item2;
    }

    @Override
    public RecyclerViewAdapterInformacje.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v= LayoutInflater.from(mContext).inflate(R.layout.item_faq,parent,false);
        MyViewHolder vHolder=new MyViewHolder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapterInformacje.MyViewHolder holder, int position) {
        holder.tv_date.setText(itemL.get(position));
        holder.tv_det.setText(item2.get(position));

    }

    @Override
    public int getItemCount() {
        return itemL.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_date;
        private TextView tv_det;
        public MyViewHolder(View itemView){
            super(itemView);

            tv_date=itemView.findViewById((R.id.data));
            tv_det=itemView.findViewById(R.id.detail);
        }
    }
}
