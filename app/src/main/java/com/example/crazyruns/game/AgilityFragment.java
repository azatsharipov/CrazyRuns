package com.example.crazyruns.game;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.crazyruns.R;

import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgilityFragment extends Fragment {
    /*
    waiting:
    0 - top
    1 - right
    2 - bottom
    3 - left
    */
    TextView tvPoints, tvTime;
    ImageView iv;
    int waiting = -1;
    float pointsFloat = 0;
    private int timeLeft;
    private Timer timer;
    private MyTimeTask timerTask;
    View fl;


    public AgilityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_agility, container, false);
        tvPoints = root.findViewById(R.id.tv_agility_points);
        tvTime = root.findViewById(R.id.tv_agility_time);
        fl = root.findViewById(R.id.fl_agility);
        iv = root.findViewById(R.id.iv_agility);
        iv.setVisibility(View.INVISIBLE);
        fl.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeTop() {
                if (waiting == 0) {
                    pointsFloat += 1.5;
                    setData();
                } else {
                    pointsFloat -= 2;
                    if (pointsFloat < 0)
                        pointsFloat = 0;
                }
                tvPoints.setText(String.valueOf(Math.round(pointsFloat)));
            }
            @Override
            public void onSwipeRight() {
                if (waiting == 1) {
                    pointsFloat += 1.5;
                    setData();
                } else {
                    pointsFloat -= 2;
                    if (pointsFloat < 0)
                        pointsFloat = 0;
                }
                tvPoints.setText(String.valueOf(Math.round(pointsFloat)));
            }
            @Override
            public void onSwipeBottom() {
                if (waiting == 2) {
                    pointsFloat += 1.5;
                    setData();
                } else {
                    pointsFloat -= 2;
                    if (pointsFloat < 0)
                        pointsFloat = 0;
                }
                tvPoints.setText(String.valueOf(Math.round(pointsFloat)));
            }
            @Override
            public void onSwipeLeft() {
                if (waiting == 3) {
                    pointsFloat += 1.5;
                    setData();
                } else {
                    pointsFloat -= 2;
                    if (pointsFloat < 0)
                        pointsFloat = 0;
                }
                tvPoints.setText(String.valueOf(Math.round(pointsFloat)));
            }
        });

        timeLeft = 15;
        timer = new Timer();
        timerTask = new MyTimeTask();
        timer.schedule(timerTask, 1000, 1000);
        return root;
    }

    void setData() {
        int randomNum = waiting;
        while (randomNum == waiting)
            randomNum = ThreadLocalRandom.current().nextInt(0, 4);
        waiting = randomNum;
        iv.setVisibility(View.VISIBLE);
        if (waiting == 0)
            iv.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
        else if (waiting == 1)
            iv.setImageResource(R.drawable.ic_arrow_forward_black_24dp);
        else if (waiting == 2)
            iv.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
        else
            iv.setImageResource(R.drawable.ic_arrow_back_black_24dp);
    }

    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener (Context ctx){
            gestureDetector = new GestureDetector(ctx, new GestureListener());
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                            result = true;
                        }
                    }
                    else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                        result = true;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        public void onSwipeRight() {
        }

        public void onSwipeLeft() {
        }

        public void onSwipeTop() {
        }

        public void onSwipeBottom() {
        }
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
        bundle.putInt("AGILITY_POINTS", Math.round(pointsFloat));
        navController.popBackStack();
        navController.navigate(R.id.gameFragment, bundle);
    }

}
