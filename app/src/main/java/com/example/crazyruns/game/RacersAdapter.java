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

public class RacersAdapter extends RecyclerView.Adapter<RacersAdapter.MyViewHolder> {
    ArrayList<Player> players = new ArrayList<>();

    public RacersAdapter(ArrayList<Player> players) {
        Comparator cmp = new Comparator<Player>() {
            @Override
            public int compare(Player a, Player b) {
                if (a.getPoints() > b.getPoints())
                    return -1;
                else if (a.getPoints() < b.getPoints())
                    return 1;
                else
                    return 0;
            }
        };
        this.players = (ArrayList<Player>) players.clone();
        Collections.sort(this.players, cmp);
        for (int i = 0; i < this.players.size(); i++) {
            this.players.get(i).setListNumber(i + 1);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumber;
        TextView tvName;
        TextView tvPoints;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.racer_item_number);
            tvName = itemView.findViewById(R.id.racer_item_name);
            tvPoints = itemView.findViewById(R.id.racer_item_points);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.racer_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Player player = players.get(position);
        holder.tvNumber.setText(String.valueOf(player.getListNumber()));
        holder.tvName.setText(player.getName());
        holder.tvPoints.setText(String.valueOf(player.getPoints()));
    }

    @Override
    public int getItemCount() {
        return players.size();
    }
}
