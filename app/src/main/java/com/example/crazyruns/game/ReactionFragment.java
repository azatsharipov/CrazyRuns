package com.example.crazyruns.game;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.crazyruns.R;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReactionFragment extends Fragment {
    private TextView tvTime;
    private TextView tvTouch;
    private Timer timer;
    private MyTimeTask timerTask;
    private float timeLeft;
    private int points;
    private View screen;


    public ReactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reaction, container, false);
        tvTime = root.findViewById(R.id.tv_reaction_time);
        tvTouch = root.findViewById(R.id.tv_touch);
        screen = root.findViewById(R.id.ll_reaction);
        screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (points == -1) {
                    view.setBackgroundColor(Color.GREEN);
                    points = Math.max(0, (300 - Math.round(Math.abs(timeLeft) * 100)) / 6);
                    tvTouch.setText(R.string.touched);
                    tvTouch.setText(tvTouch.getText() + " " + String.valueOf(round(timeLeft, 2)) +
                            "(" + String.valueOf(points) + " points)");
                }
            }
        });
        timeLeft = 10.00f;
        timer = new Timer();
        timerTask = new MyTimeTask();
        timer.schedule(timerTask, 1000, 10);
        points = -1;

        return root;
    }

    void gameOver() {
        if (points == -1)
            points = 0;
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        Bundle bundle = new Bundle();
        bundle.putInt("REACTION_POINTS", points);
        navController.popBackStack();
        navController.navigate(R.id.gameFragment, bundle);
    }

    class MyTimeTask extends TimerTask {
        public void run() {
            timeLeft -= 0.01;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (timeLeft > 8)
                        tvTime.setText(String.valueOf(round(timeLeft, 2)));
                    else
                        tvTime.setText("?");
                }
            });
            if (timeLeft <= -3) {
                gameOver();
                cancel();
            }
        }
    }

    BigDecimal round (float f, int d) {
        BigDecimal bd = new BigDecimal(Float.toString(f));
        bd = bd.setScale(d, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

}
