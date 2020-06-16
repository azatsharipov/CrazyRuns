package com.example.crazyruns.game;


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

/**
 * A simple {@link Fragment} subclass.
 */
public class SpeedFragment extends Fragment {
    private Button bt1;
    private Button bt2;
    private Button bt3;
    private Button bt4;
    private Button bt5;
    private Button bt6;
    private Button bt7;
    private Button bt8;
    private Button bt9;
    private TextView tvPoints;
    private TextView tvTime;
    private View.OnClickListener onNumberClick;
    private int level = 0;
    private ArrayList<Integer> waitingNumbers = new ArrayList<>();
    private int waitingNumber;
    private int points;
    private int timeLeft;
    private Timer timer;
    private MyTimeTask timerTask;

    public SpeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_speed, container, false);
        points = 0;
        tvPoints = root.findViewById(R.id.tv_points);
        tvTime = root.findViewById(R.id.tv_time);
        timeLeft = 10;
        bt1 = root.findViewById(R.id.bt_1);
        bt2 = root.findViewById(R.id.bt_2);
        bt3 = root.findViewById(R.id.bt_3);
        bt4 = root.findViewById(R.id.bt_4);
        bt5 = root.findViewById(R.id.bt_5);
        bt6 = root.findViewById(R.id.bt_6);
        bt7 = root.findViewById(R.id.bt_7);
        bt8 = root.findViewById(R.id.bt_8);
        bt9 = root.findViewById(R.id.bt_9);
        onNumberClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button bt = (Button) view;
                int number = Integer.valueOf(bt.getText().toString());
                if (number == waitingNumber) {
                    bt.setVisibility(View.INVISIBLE);
                    waitingNumber++;
                    points++;
                } else {
                    points--;
                    if (points < 0)
                        points = 0;
                }
                tvPoints.setText(String.valueOf(points));
                if (waitingNumber > level)
                    setWaiting();
            }
        };
        bt1.setOnClickListener(onNumberClick);
        bt2.setOnClickListener(onNumberClick);
        bt3.setOnClickListener(onNumberClick);
        bt4.setOnClickListener(onNumberClick);
        bt5.setOnClickListener(onNumberClick);
        bt6.setOnClickListener(onNumberClick);
        bt7.setOnClickListener(onNumberClick);
        bt8.setOnClickListener(onNumberClick);
        bt9.setOnClickListener(onNumberClick);
        setWaiting();
        timer = new Timer();
        timerTask = new MyTimeTask();
        timer.schedule(timerTask, 0, 1000);
        return root;
    }

    class MyTimeTask extends TimerTask {
        public void run() {
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
        bundle.putInt("SPEED_POINTS", points);
        navController.popBackStack();
        navController.navigate(R.id.gameFragment, bundle);
//        navController.navigate(R.id.backToGameFragment, bundle);
    }

    private void setWaiting() {
        level++;
        waitingNumber = 1;
        if (level > 9)
            level = 9;
        waitingNumbers = new ArrayList<>();

        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i <= 9; i++) {
            list.add(new Integer(i));
        }
        Collections.shuffle(list);

        for (int i = 1; i <= level; i++) {
            waitingNumbers.add(list.get(i - 1));
        }
        bt1.setVisibility(View.INVISIBLE);
        bt2.setVisibility(View.INVISIBLE);
        bt3.setVisibility(View.INVISIBLE);
        bt4.setVisibility(View.INVISIBLE);
        bt5.setVisibility(View.INVISIBLE);
        bt6.setVisibility(View.INVISIBLE);
        bt7.setVisibility(View.INVISIBLE);
        bt8.setVisibility(View.INVISIBLE);
        bt9.setVisibility(View.INVISIBLE);
        if (waitingNumbers.contains(1)) {
            bt1.setVisibility(View.VISIBLE);
            bt1.setText(String.valueOf(waitingNumbers.indexOf(1) + 1));
        } if (waitingNumbers.contains(2)) {
            bt2.setVisibility(View.VISIBLE);
            bt2.setText(String.valueOf(waitingNumbers.indexOf(2) + 1));
        } if (waitingNumbers.contains(3)) {
            bt3.setVisibility(View.VISIBLE);
            bt3.setText(String.valueOf(waitingNumbers.indexOf(3) + 1));
        } if (waitingNumbers.contains(4)) {
            bt4.setVisibility(View.VISIBLE);
            bt4.setText(String.valueOf(waitingNumbers.indexOf(4) + 1));
        } if (waitingNumbers.contains(5)) {
            bt5.setVisibility(View.VISIBLE);
            bt5.setText(String.valueOf(waitingNumbers.indexOf(5) + 1));
        } if (waitingNumbers.contains(6)) {
            bt6.setVisibility(View.VISIBLE);
            bt6.setText(String.valueOf(waitingNumbers.indexOf(6) + 1));
        } if (waitingNumbers.contains(7)) {
            bt7.setVisibility(View.VISIBLE);
            bt7.setText(String.valueOf(waitingNumbers.indexOf(7) + 1));
        } if (waitingNumbers.contains(8)) {
            bt8.setVisibility(View.VISIBLE);
            bt8.setText(String.valueOf(waitingNumbers.indexOf(8) + 1));
        } if (waitingNumbers.contains(9)) {
            bt9.setVisibility(View.VISIBLE);
            bt9.setText(String.valueOf(waitingNumbers.indexOf(9) + 1));
        }
    }
}
