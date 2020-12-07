package com.example.coderescue;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VictimLocationAdapter extends RecyclerView.Adapter<VictimLocationHolder>{

    Context c;
    ArrayList<VictimLocationCardModel> models;


    public VictimLocationAdapter(Context c, ArrayList<VictimLocationCardModel> models) {
        this.c = c;
        this.models = models;
    }

    @NonNull
    @Override
    public VictimLocationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.victim_location_card ,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        return new VictimLocationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VictimLocationHolder myHolder, int i) {
        String lat = models.get(i).getLatitude();
        String longi = models.get(i).getLongitude();
        myHolder.mTitle.setText(models.get(i).getTitle());
        myHolder.mLat.setText(lat);
        myHolder.mLong.setText(longi);
        System.out.println("jai shree ram2");

        //WHEN ITEM IS CLICKED
        myHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + longi + " (" + "label temp" + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                c.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

}
