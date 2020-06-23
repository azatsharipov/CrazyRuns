package com.example.crazyruns.game;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.crazyruns.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A simple {@link Fragment} subclass.
 */
public class StaminaFragment extends Fragment {
    Button[] bt = new Button[4];
    TextView tvPoints, tvTime;
    int waiting = -1, points = 0;
    ArrayList<String> colors = new ArrayList<String>();
    private int timeLeft;
    private Timer timer;
    private MyTimeTask timerTask;


    public StaminaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stamina, container, false);
        bt[0] = root.findViewById(R.id.bt_1_stamina);
        bt[1] = root.findViewById(R.id.bt_2_stamina);
        bt[2] = root.findViewById(R.id.bt_3_stamina);
        bt[3] = root.findViewById(R.id.bt_4_stamina);
        tvPoints = root.findViewById(R.id.tv_stamina_points_ingame);
        tvTime = root.findViewById(R.id.tv_stamina_time);
        colors.add("red");
        colors.add("green");
        colors.add("orange");
        colors.add("blue");
        colors.add("yellow");
        for (int i = 0; i < 4; i++)
            bt[i].setVisibility(View.INVISIBLE);
        bt[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (waiting == 0) {
                    points += 4;
                    setData();
                } else {
                    points -= 5;
                    if (points < 0)
                        points = 0;
                }
                tvPoints.setText(String.valueOf(points));
            }
        });
        bt[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (waiting == 1) {
                    points += 4;
                    setData();
                } else {
                    points -= 5;
                    if (points < 0)
                        points = 0;
                }
                tvPoints.setText(String.valueOf(points));
            }
        });
        bt[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (waiting == 2) {
                    points += 4;
                    setData();
                } else {
                    points -= 5;
                    if (points < 0)
                        points = 0;
                }
                tvPoints.setText(String.valueOf(points));
            }
        });
        bt[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (waiting == 3) {
                    points += 4;
                    setData();
                } else {
                    points -= 5;
                    if (points < 0)
                        points = 0;
                }
                tvPoints.setText(String.valueOf(points));
            }
        });
        timeLeft = 15;
        timer = new Timer();
        timerTask = new MyTimeTask();
        timer.schedule(timerTask, 1000, 1000);

        return root;
    }

    class MyTimeTask extends TimerTask {
        public void run() {
            if (timeLeft == 15) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setData();
                    }
                });
            }
            timeLeft--;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvTime.setText(String.valueOf(timeLeft));
                }
            });
            if (timeLeft == 0) {
                gameOver();
                cancel();
            }
        }
    }

    private void gameOver () {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        Bundle bundle = new Bundle();
        bundle.putInt("STAMINA_POINTS", points);
        navController.popBackStack();
        navController.navigate(R.id.gameFragment, bundle);
    }

    void setData() {
        Collections.shuffle(colors);
        for (int i = 0; i < 4; i++) {
            bt[i].setVisibility(View.VISIBLE);
            bt[i].setText(colors.get(i));
            bt[i].setBackgroundColor(getColor(colors.get(i)));
        }
        int randomNum = ThreadLocalRandom.current().nextInt(0, 4);
        int randomTarget = ThreadLocalRandom.current().nextInt(0, 2);
        if (randomTarget == 0)
            bt[randomNum].setText(colors.get(4));
        else
            bt[randomNum].setBackgroundColor(getColor(colors.get(4)));
        waiting = randomNum;
    }

    int getColor(String color) {
        if (color == "red")
            return Color.RED;
        else if (color == "blue")
            return Color.BLUE;
        else if (color == "green")
            return Color.GREEN;
        else if (color == "yellow")
            return Color.YELLOW;
        else
            return Color.rgb(255, 127, 80);
    }

}
