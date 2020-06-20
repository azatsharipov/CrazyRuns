package com.example.crazyruns.game;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.crazyruns.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class RaceFragment extends Fragment {
    private Race race;
    private ArrayList<Racer> racers = new ArrayList<>();
    private int distance;
    private ArrayList<Boolean> jumps = new ArrayList<>();
    private Timer timer;
    private RaceFragment.MyTimeTask timerTask;
    private int timeLeft;

    public RaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_race, container, false);
        Log.e("MY", "Fragment");
        loadStats();
        setRace();
        race = new Race(getActivity(), this, racers, distance, jumps);

        timeLeft = 4;
        timer = new Timer();
        timerTask = new RaceFragment.MyTimeTask();
        timer.schedule(timerTask, 0, 1000);

        return race;
    }

    void setRace() {
        distance = 200;
        jumps.clear();
        for (int i = 0; i <= distance; i += 100) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, 2);
            if (randomNum == 1 || i == 0 || i == distance) {
                jumps.add(true);
            } else {
                jumps.add(false);
            }
        }
    }

    void loadStats() {
        /*
        Bundle bundle = getArguments();
        if (bundle != null) {
            int speed = bundle.getInt("SPEED_POINTS", 1);
            int stamina = bundle.getInt("STAMINA_POINTS", 1);
            int agility = bundle.getInt("AGILITY_POINTS", 1);
            int reaction = bundle.getInt("REACTION_POINTS", 1);
            player = new Player(speed, stamina, agility, reaction);
        } else {
            player = new Player(1, 1, 1, 1);
        }
         */
        SharedPreferences sPref;
        sPref = getActivity().getPreferences(MODE_PRIVATE);
        int playersAmount = sPref.getInt("PLAYERS_AMOUNT", 1);
        racers = new ArrayList<>();
        for (int i = 0; i < playersAmount; i++) {
            int speedPoints = sPref.getInt("SPEED_POINTS" + String.valueOf(i), 1);
            int staminaPoints = sPref.getInt("STAMINA_POINTS" + String.valueOf(i), 1);
            int agilityPoints = sPref.getInt("AGILITY_POINTS" + String.valueOf(i), 1);
            int reactionPoints = sPref.getInt("REACTION_POINTS" + String.valueOf(i), 1);
            racers.add(new Racer(speedPoints, staminaPoints, agilityPoints, reactionPoints, 300 + i * 100));
        }



    }

    class MyTimeTask extends TimerTask {
        public void run() {
            timeLeft--;
            race.drawTimer(timeLeft);

            if (timeLeft == -1) {
                race.startGame();
                cancel();
            }
        }
    }

    public void stopRace() {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.popBackStack();
        ArrayList<Integer> places = race.getPlaces();
        Bundle bundle = new Bundle();
        for (int i = 0; i < places.size(); i++) {
            bundle.putInt("POINTS" + String.valueOf(places.get(i)), 10 - i);
        }
        navController.navigate(R.id.gameFragment, bundle);
    }
}
