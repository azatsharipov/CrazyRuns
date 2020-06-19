package com.example.crazyruns.game;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.crazyruns.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {
    private Button btSpeed;
    private Button btStamina;
    private Button btAgility;
    private Button btReaction;
    private TextView tvSpeed;
    private TextView tvStamina;
    private TextView tvAgility;
    private TextView tvReaction;
    private TextView tvAvailablePoints;
    private Button btRace;
    private int playersAmount;
    private ArrayList<Player> players;
    private RecyclerView recyclerView;
    private RacersAdapter adapter;
    private int availablePoints = 2;
    private SharedPreferences sPref;

    public GameFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_game, container, false);
        btSpeed = root.findViewById(R.id.bt_speed);
        btStamina = root.findViewById(R.id.bt_stamina);
        btAgility = root.findViewById(R.id.bt_agility);
        btReaction = root.findViewById(R.id.bt_reaction);
        tvSpeed = root.findViewById(R.id.tv_speed_points);
        tvStamina = root.findViewById(R.id.tv_stamina_points);
        tvAgility = root.findViewById(R.id.tv_agility_points);
        tvReaction = root.findViewById(R.id.tv_reaction_points);
        tvAvailablePoints = root.findViewById(R.id.tv_available_points);
        btRace = root.findViewById(R.id.bt_race);

        tvAvailablePoints.setText("Points: " + String.valueOf(availablePoints));

        players = new ArrayList<>();
        players.add(new Player(100, 100, 100, 100));
        players.add(new Player(200, 200, 200, 200));
        players.add(new Player(500, 500, 500, 500));
        players.add(new Player(999, 999, 999, 999));
        playersAmount = players.size();
        loadData();

        updateStats();

        Bundle bundle = getArguments();
        if (bundle != null) {
            int points = bundle.getInt("SPEED_POINTS", 1);
            if (points != 1)
                players.get(0).setSpeed(players.get(0).getSpeed() + points);
            points = bundle.getInt("STAMINA_POINTS", 1);
            if (points != 1)
                players.get(0).setStamina(players.get(0).getStamina() + points);
            points = bundle.getInt("REACTION_POINTS", 1);
            if (points != 1)
                players.get(0).setReaction(players.get(0).getReaction() + points);
            for (int i = 0; i < 10; i++) {
                points = bundle.getInt("POINTS" + String.valueOf(i), -1);
                if (points != -1) {
//                    players.get(i).setPoints(points);
                    players.get(i).setPoints(players.get(i).getPoints() + points);
                }

            }
            updateStats();
        }


        recyclerView = root.findViewById(R.id.rv_players);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new RacersAdapter(players);
        recyclerView.setAdapter(adapter);

        btSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                availablePoints--;
                saveData();
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.popBackStack();
                navController.navigate(R.id.speedFragment);
            }
        });

        btStamina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                availablePoints--;
                saveData();
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.popBackStack();
                navController.navigate(R.id.staminaFragment);
            }
        });

        btReaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                availablePoints--;
                saveData();
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.popBackStack();
                navController.navigate(R.id.reactionFragment);
            }
        });

        btRace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.popBackStack();
                navController.navigate(R.id.raceFragment);
            }
        });

        return root;
    }

    void loadData() {
        sPref = getActivity().getPreferences(MODE_PRIVATE);
        for (int i = 0; i < playersAmount; i++) {
            int points = sPref.getInt("POINTS" + String.valueOf(i), 0);
            players.get(i).setPoints(points);
            int speedPoints = sPref.getInt("SPEED_POINTS" + String.valueOf(i), 1);
            if (speedPoints != 1)
                players.get(i).setSpeed(speedPoints);
            int staminaPoints = sPref.getInt("STAMINA_POINTS" + String.valueOf(i), 1);
            if (staminaPoints != 1)
                players.get(i).setStamina(staminaPoints);
            int agilityPoints = sPref.getInt("AGILITY_POINTS" + String.valueOf(i), 1);
            if (agilityPoints != 1)
                players.get(i).setAgility(agilityPoints);
            int reactionPoints = sPref.getInt("REACTION_POINTS" + String.valueOf(i), 1);
            if (reactionPoints != 1)
                players.get(i).setReaction(reactionPoints);
        }
    }

    void saveData() {
        sPref = getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt("PLAYERS_AMOUNT", playersAmount);
        for (int i = 0; i < playersAmount; i++) {
            ed.putInt("POINTS" + String.valueOf(i), players.get(i).getPoints());
            ed.putInt("SPEED_POINTS" + String.valueOf(i), players.get(i).getSpeed());
            ed.putInt("STAMINA_POINTS" + String.valueOf(i), players.get(i).getStamina());
            ed.putInt("AGILITY_POINTS" + String.valueOf(i), players.get(i).getAgility());
            ed.putInt("REACTION_POINTS" + String.valueOf(i), players.get(i).getReaction());
        }
        ed.commit();
    }

    void updateStats() {
        tvSpeed.setText(String.valueOf(players.get(0).getSpeed()));
        tvStamina.setText(String.valueOf(players.get(0).getStamina()));
        tvAgility.setText(String.valueOf(players.get(0).getAgility()));
        tvReaction.setText(String.valueOf(players.get(0).getReaction()));
    }

}
