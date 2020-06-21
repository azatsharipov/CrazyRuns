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
import java.util.concurrent.ThreadLocalRandom;

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
    private TextView tvDistance;
    private TextView tvJumpsAmount;
    private TextView tvRaceNumber;
    private Button btRace;
    private ArrayList<Player> players;
    private RecyclerView recyclerView;
    private RacersAdapter adapter;
    private int availablePoints = 2;
    private int distance;
    private int jumpsAmount;
    private int raceNumber;
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
        tvDistance = root.findViewById(R.id.tv_distance);
        tvJumpsAmount = root.findViewById(R.id.tv_jumps_amount);
        tvRaceNumber = root.findViewById(R.id.tv_race_number);
        btRace = root.findViewById(R.id.bt_race);

        players = new ArrayList<>();
        players.add(new Player("I", 200, 200, 200, 200));
        players.add(new Player("2", 200, 200, 200, 200));
        players.add(new Player("3", 200, 200, 200, 200));
        players.add(new Player("4", 200, 200, 200, 200));
        players.add(new Player("5", 200, 200, 200, 200));
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
            points = bundle.getInt("AGILITY_POINTS", 1);
            if (points != 1)
                players.get(0).setAgility(players.get(0).getAgility() + points);
            points = bundle.getInt("REACTION_POINTS", 1);
            if (points != 1)
                players.get(0).setReaction(players.get(0).getReaction() + points);
            boolean wasAdded = false;
            for (int i = 0; i < 10; i++) {
                points = bundle.getInt("POINTS" + String.valueOf(i), -1);
                if (points != -1) {
//                    players.get(i).setPoints(points);
                    wasAdded = true;
                    players.get(i).setPoints(players.get(i).getPoints() + points);
                }
            }
            if (wasAdded) {
                raceNumber++;
                if (raceNumber < 11) {
                    randomRace();
                    availablePoints = 2;
                    upgradeBots();
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
                if (availablePoints > 0) {
                    availablePoints--;
                    saveData();
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.popBackStack();
                    navController.navigate(R.id.speedFragment);
                }
            }
        });

        btStamina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (availablePoints > 0) {
                    availablePoints--;
                    saveData();
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.popBackStack();
                    navController.navigate(R.id.staminaFragment);
                }
            }
        });

        btAgility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (availablePoints > 0) {
                    availablePoints--;
                    saveData();
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.popBackStack();
                    navController.navigate(R.id.agilityFragment);
                }
            }
        });

        btReaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (availablePoints > 0) {
                    availablePoints--;
                    saveData();
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.popBackStack();
                    navController.navigate(R.id.reactionFragment);
                }
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
        for (int i = 0; i < players.size(); i++) {
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
        raceNumber = sPref.getInt("RACE_NUMBER", 1);
        availablePoints = sPref.getInt("AVAILABLE_POINTS", 2);
        if (sPref.getInt("DISTANCE", -1) == -1) {
            randomRace();
            upgradeBots();
        } else {
            distance = sPref.getInt("DISTANCE", -1);
            jumpsAmount = -2;
            for (int i = 0; i <= distance; i += 100) {
                if (sPref.getBoolean("JUMP" + String.valueOf(i), false))
                    jumpsAmount++;
            }
        }
    }

    void saveData() {
        sPref = getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt("RACE_NUMBER", raceNumber);
        ed.putInt("AVAILABLE_POINTS", availablePoints);
        ed.putInt("PLAYERS_AMOUNT", players.size());
        for (int i = 0; i < players.size(); i++) {
            ed.putInt("POINTS" + String.valueOf(i), players.get(i).getPoints());
            ed.putString("NAME" + String.valueOf(i), players.get(i).getName());
            ed.putInt("SPEED_POINTS" + String.valueOf(i), players.get(i).getSpeed());
            ed.putInt("STAMINA_POINTS" + String.valueOf(i), players.get(i).getStamina());
            ed.putInt("AGILITY_POINTS" + String.valueOf(i), players.get(i).getAgility());
            ed.putInt("REACTION_POINTS" + String.valueOf(i), players.get(i).getReaction());
        }
        ed.commit();
    }

    void randomRace() {
        int randomDistance = ThreadLocalRandom.current().nextInt(1, Math.min(11, 3 + raceNumber));
        distance = randomDistance * 100;
        sPref = getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt("DISTANCE", distance);
        jumpsAmount = -2;
        for (int i = 0; i <= distance; i += 100) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, 2);
            if (randomNum == 1 || i == 0 || i == distance) {
                ed.putBoolean("JUMP" + String.valueOf(i), true);
                jumpsAmount++;
            } else {
                ed.putBoolean("JUMP" + String.valueOf(i), false);
            }
        }
        ed.commit();
    }

    void upgradeBots() {
        for (int i = 1; i < players.size(); i++) {
            for (int j = 0; j < 2; j++) {
                int randomNum = ThreadLocalRandom.current().nextInt(0, 4);
                int randomPoints = ThreadLocalRandom.current().nextInt(30, 50);
                Player player = players.get(i);
                if (randomNum == 0)
                    player.setSpeed(player.getSpeed() + randomPoints);
                else if (randomNum == 1)
                    player.setStamina(player.getStamina() + randomPoints);
                else if (randomNum == 2)
                    player.setAgility(player.getAgility() + randomPoints);
                else
                    player.setReaction(player.getReaction() + randomPoints);
            }
        }
    }

    void updateStats() {
        tvRaceNumber.setText("Race number " + String.valueOf(raceNumber));
        tvAvailablePoints.setText("Stat Points " + String.valueOf(availablePoints));
        tvDistance.setText(String.valueOf(distance) + " m");
        tvJumpsAmount.setText(String.valueOf(jumpsAmount) + " jumps");
        tvSpeed.setText(String.valueOf(players.get(0).getSpeed()));
        tvStamina.setText(String.valueOf(players.get(0).getStamina()));
        tvAgility.setText(String.valueOf(players.get(0).getAgility()));
        tvReaction.setText(String.valueOf(players.get(0).getReaction()));
        if (raceNumber >= 11) {
            btSpeed.setVisibility(View.INVISIBLE);
            btStamina.setVisibility(View.INVISIBLE);
            btAgility.setVisibility(View.INVISIBLE);
            btReaction.setVisibility(View.INVISIBLE);
            tvDistance.setVisibility(View.INVISIBLE);
            tvJumpsAmount.setVisibility(View.INVISIBLE);
            tvAvailablePoints.setVisibility(View.INVISIBLE);
            tvRaceNumber.setText("Champ is over");
            btRace.setText("end game");
            btRace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.popBackStack();
                    navController.navigate(R.id.startMenuFragment);
                }
            });
        }
    }

}
