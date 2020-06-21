package com.example.crazyruns.game;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crazyruns.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RacersResultAdapter extends RecyclerView.Adapter<RacersResultAdapter.MyViewHolder> {
    ArrayList<Racer> racers = new ArrayList<>();

    public RacersResultAdapter(ArrayList<Racer> racers) {
        Comparator cmp = new Comparator<Racer>() {
            @Override
            public int compare(Racer a, Racer b) {
                if (a.getPoints() > b.getPoints())
                    return -1;
                else if (a.getPoints() < b.getPoints())
                    return 1;
                else
                    return 0;
            }
        };
        this.racers = (ArrayList<Racer>) racers.clone();
        Collections.sort(this.racers, cmp);
        for (int i = 0; i < this.racers.size(); i++) {
            this.racers.get(i).setListNumber(i + 1);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumber;
        TextView tvName;
        TextView tvTime;
        TextView tvPoints;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.racer_result_item_number);
            tvName = itemView.findViewById(R.id.racer_result_item_name);
            tvTime = itemView.findViewById(R.id.racer_result_item_time);
            tvPoints = itemView.findViewById(R.id.racer_result_item_points);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.racer_result_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Racer racer = racers.get(position);
        holder.tvNumber.setText(String.valueOf(racer.getListNumber()));
        holder.tvName.setText(racer.getName());
        holder.tvTime.setText(String.valueOf(round(racer.getTime(), 2)));
        holder.tvPoints.setText(String.valueOf(racer.getPoints()));
    }

    BigDecimal round (float f, int d) {
        BigDecimal bd = new BigDecimal(Float.toString(f));
        bd = bd.setScale(d, BigDecimal.ROUND_HALF_UP);
        return bd;
    }


    @Override
    public int getItemCount() {
        return racers.size();
    }
}
