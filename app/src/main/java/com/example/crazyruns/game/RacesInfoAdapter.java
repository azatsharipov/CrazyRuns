package com.example.crazyruns.game;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crazyruns.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RacesInfoAdapter extends RecyclerView.Adapter<RacesInfoAdapter.MyViewHolder> {
    ArrayList<RaceInfo> races = new ArrayList<>();

    public RacesInfoAdapter(ArrayList<RaceInfo> races) {
        this.races = (ArrayList<RaceInfo>) races.clone();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumber;
        TextView tvDistance;
        TextView tvJumps;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tv_race_info_number);
            tvDistance = itemView.findViewById(R.id.tv_distance);
            tvJumps = itemView.findViewById(R.id.tv_jumps_amount);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.race_info_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RaceInfo raceInfo = races.get(position);
        holder.tvNumber.setText(String.valueOf(raceInfo.getRaceNumber()));
        holder.tvDistance.setText(String.valueOf(raceInfo.getDistance()));
        holder.tvJumps.setText(String.valueOf(raceInfo.getJumpsAmount()));
    }

    @Override
    public int getItemCount() {
        return races.size();
    }
}

