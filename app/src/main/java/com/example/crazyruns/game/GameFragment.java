package com.example.crazyruns.game;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableBoolean;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crazyruns.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private TextView tvRaceNumber;
    private Button btRace;
    private ArrayList<Player> players;
    private ArrayList<RaceInfo> races = new ArrayList<>();
    private RecyclerView recyclerView;
    private RacersAdapter adapter;
    private RecyclerView recyclerViewRaces;
    private RacesInfoAdapter adapterRaces;
    private int raceNumber;
    private SharedPreferences sPref;
    SharedPreferences.Editor ed;

    private boolean isMultiplayer = false;
    private int playerNumber = 0;
    private String roomNumber;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;


    public GameFragment() {
    }
/*
    class MyTimeTask extends TimerTask {
        public void run() {
            if (raceStarted) {
                raceStarted = false;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        players.get(playerNumber).setAvailablePoints(2);
                        myRef.child("player" + playerNumber).child("availablePoints").setValue(players.get(playerNumber).getAvailablePoints());
                        btRace.callOnClick();
                    }
                });
//                timer.cancel();
            }
        }
    }
 */

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("MULTIPLAYER", isMultiplayer);
        outState.putInt("PLAYER_NUMBER", playerNumber);
        outState.putString("ROOM_NUMBER", roomNumber);
        outState.putInt("RACE_NUMBER", raceNumber);
        outState.putInt("PLAYERS_AMOUNT", players.size());
        for (int i = 0; i < players.size(); i++) {
            outState.putInt("POINTS" + String.valueOf(i), players.get(i).getPoints());
            outState.putString("NAME" + String.valueOf(i), players.get(i).getName());
            outState.putInt("AVAILABLE_POINTS" + String.valueOf(i), players.get(i).getAvailablePoints());
            outState.putInt("SPEED_POINTS" + String.valueOf(i), players.get(i).getSpeed());
            outState.putInt("STAMINA_POINTS" + String.valueOf(i), players.get(i).getStamina());
            outState.putInt("AGILITY_POINTS" + String.valueOf(i), players.get(i).getAgility());
            outState.putInt("REACTION_POINTS" + String.valueOf(i), players.get(i).getReaction());
        }
        for (int raceNumber = 1; raceNumber <= 10; raceNumber++) {
            RaceInfo race = races.get(raceNumber - 1);
            outState.putInt("DISTANCE" + String.valueOf(raceNumber), race.getDistance());
            for (int i = 0; i <= race.getDistance(); i += 100) {
                boolean isJump = sPref.getBoolean("JUMP" + String.valueOf(raceNumber) + String.valueOf(i), false);
                outState.putBoolean("JUMP" + String.valueOf(raceNumber) + String.valueOf(i), isJump);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        tvRaceNumber = root.findViewById(R.id.tv_race_number);
        btRace = root.findViewById(R.id.bt_race);

        sPref = getActivity().getPreferences(MODE_PRIVATE);
        ed = sPref.edit();
        if (savedInstanceState != null) {
            isMultiplayer = savedInstanceState.getBoolean("MULTIPLAYER", false);
            playerNumber = savedInstanceState.getInt("PLAYER_NUMBER", 0);
            roomNumber = savedInstanceState.getString("ROOM_NUMBER", "0");
            raceNumber = savedInstanceState.getInt("RACE_NUMBER", 0);
            int playersAmount = savedInstanceState.getInt("PLAYERS_AMOUNT", 1);
            players = new ArrayList<>();
            for (int i = 0; i < playersAmount; i++) {
                int points = savedInstanceState.getInt("POINTS" + String.valueOf(i), 0);
                String name = savedInstanceState.getString("NAME" + String.valueOf(i), "Noname");
                int availablePoints = savedInstanceState.getInt("AVAILABLE_POINTS" + String.valueOf(i), 0);
                int speed = savedInstanceState.getInt("SPEED_POINTS" + String.valueOf(i), 0);
                int stamina = savedInstanceState.getInt("STAMINA_POINTS" + String.valueOf(i), 0);
                int agility = savedInstanceState.getInt("AGILITY_POINTS" + String.valueOf(i), 0);
                int reaction = savedInstanceState.getInt("REACTION_POINTS" + String.valueOf(i), 0);
                Player player = new Player(name, speed, stamina, agility, reaction);
                player.setAvailablePoints(availablePoints);
                player.setPoints(points);
                players.add(player);
            }
            races = new ArrayList<>();
            for (int raceNumber = 1; raceNumber <= 10; raceNumber++) {
                int distance = savedInstanceState.getInt("DISTANCE" + String.valueOf(raceNumber), 0);
                ed.putInt("DISTANCE" + String.valueOf(raceNumber), distance);
                int jumpsAmount = -2;
                for (int i = 0; i <= distance; i += 100) {
                    boolean isJump = savedInstanceState.getBoolean("JUMP" + String.valueOf(raceNumber) + String.valueOf(i), false);
                    if (isJump || i == 0 || i == distance) {
                        ed.putBoolean("JUMP" + String.valueOf(raceNumber) + String.valueOf(i), true);
                        jumpsAmount++;
                    } else {
                        ed.putBoolean("JUMP" + String.valueOf(raceNumber) + String.valueOf(i), false);
                    }
                }
                ed.commit();
                races.add(new RaceInfo(raceNumber, distance, jumpsAmount));
            }

        } else {
            isMultiplayer = sPref.getBoolean("MULTIPLAYER", false);
            if (isMultiplayer) {
                playerNumber = sPref.getInt("PLAYER_NUMBER", 0);
                roomNumber = sPref.getString("ROOM_NUMBER", "0");
                myRef = database.getReference("rooms").child(roomNumber);
            }
            loadData();
        }

        if (isMultiplayer) {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (int i = 0; i < players.size(); i++) {
//                        player = dataSnapshot.child("player" + String.valueOf(i)).getValue(Player.class);
                        Player player = players.get(i);
                        String name = dataSnapshot.child("player" + String.valueOf(i)).child("name").getValue(String.class);
                        player.setName(name);
                        int points = dataSnapshot.child("player" + String.valueOf(i)).child("points").getValue(Integer.class);
                        player.setPoints(points);
                        int speedPoints = dataSnapshot.child("player" + String.valueOf(i)).child("speed").getValue(Integer.class);
                        player.setSpeed(speedPoints);
                        int staminaPoints = dataSnapshot.child("player" + String.valueOf(i)).child("stamina").getValue(Integer.class);
                        player.setStamina(staminaPoints);
                        int agilityPoints = dataSnapshot.child("player" + String.valueOf(i)).child("agility").getValue(Integer.class);
                        player.setAgility(agilityPoints);
                        int reactionPoints = dataSnapshot.child("player" + String.valueOf(i)).child("reaction").getValue(Integer.class);
                        player.setReaction(reactionPoints);
                        int availablePoints = dataSnapshot.child("player" + String.valueOf(i)).child("availablePoints").getValue(Integer.class);
                        player.setAvailablePoints(availablePoints);
                        ed.putInt("POINTS" + String.valueOf(i), player.getPoints());
                        ed.putString("NAME" + String.valueOf(i), player.getName());
                        ed.putInt("SPEED_POINTS" + String.valueOf(i), player.getSpeed());
                        ed.putInt("STAMINA_POINTS" + String.valueOf(i), player.getStamina());
                        ed.putInt("AGILITY_POINTS" + String.valueOf(i), player.getAgility());
                        ed.putInt("REACTION_POINTS" + String.valueOf(i), player.getReaction());
                        ed.putInt("AVAILABLE_POINTS" + String.valueOf(i), player.getAvailablePoints());
                    }
                    updateStats();
                    if (adapter != null)
                        adapter.notifyDataSetChanged();
                    // getting races
                    for (int raceNumber = 1; raceNumber <= 10; raceNumber++) {
                        int distance = dataSnapshot.child("races").child(String.valueOf(raceNumber))
                                .child("distance").getValue(Integer.class);
                        ed.putInt("DISTANCE" + String.valueOf(raceNumber), distance);
                        int jumpsAmount = -2;
                        for (int i = 0; i <= distance; i += 100) {
                            boolean isJump = dataSnapshot.child("races").child(String.valueOf(raceNumber))
                                    .child("jump").child(String.valueOf(i)).getValue(Boolean.class);
                            if (isJump) {
                                jumpsAmount++;
                            }
                            ed.putBoolean("JUMP" + String.valueOf(raceNumber) + String.valueOf(i), isJump);
                        }
                        ed.commit();
                        races.get(raceNumber - 1).setDistance(distance);
                        races.get(raceNumber - 1).setJumpsAmount(jumpsAmount);
//                        races.add(new RaceInfo(raceNumber, distance, jumpsAmount));
                    }
                    if (adapterRaces != null)
                        adapterRaces.notifyDataSetChanged();
                    int allAvailablePoint = 0;
                    for (int i = 0; i < players.size(); i++) {
                        allAvailablePoint += players.get(i).getAvailablePoints();
                    }
                    if (allAvailablePoint == 0) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                players.get(playerNumber).setAvailablePoints(2);
                                myRef.child("player" + playerNumber).child("availablePoints").setValue(players.get(playerNumber).getAvailablePoints());
                                saveData();
                                try {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                                            navController.popBackStack();
                                            navController.navigate(R.id.raceFragment);
                                        }
                                    });
                                } catch (Exception e) {
                                }
//                                btRace.callOnClick();
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("MY", "Failed to read value.", error.toException());
                }
            });
        }

        updateStats();

        Bundle bundle = getArguments();
        if (bundle != null && savedInstanceState == null) {
            int points = bundle.getInt("SPEED_POINTS", 1);
            if (points != 1) {
                players.get(playerNumber).setSpeed(players.get(playerNumber).getSpeed() + points);
                players.get(playerNumber).setAvailablePoints(players.get(playerNumber).getAvailablePoints() - 1);
//                if (isMultiplayer) {
//                    myRef.child("player" + playerNumber).setValue(players.get(playerNumber));
//                }
            }
            points = bundle.getInt("STAMINA_POINTS", 1);
            if (points != 1) {
                players.get(playerNumber).setStamina(players.get(playerNumber).getStamina() + points);
                players.get(playerNumber).setAvailablePoints(players.get(playerNumber).getAvailablePoints() - 1);
//                if (isMultiplayer) {
//                    myRef.child("player" + playerNumber).setValue(players.get(playerNumber));
//                }
            }
            points = bundle.getInt("AGILITY_POINTS", 1);
            if (points != 1) {
                players.get(playerNumber).setAgility(players.get(playerNumber).getAgility() + points);
                players.get(playerNumber).setAvailablePoints(players.get(playerNumber).getAvailablePoints() - 1);
//                if (isMultiplayer) {
//                    myRef.child("player" + playerNumber).setValue(players.get(playerNumber));
//                }
            }
            points = bundle.getInt("REACTION_POINTS", 1);
            if (points != 1) {
                players.get(playerNumber).setReaction(players.get(playerNumber).getReaction() + points);
                players.get(playerNumber).setAvailablePoints(players.get(playerNumber).getAvailablePoints() - 1);
//                if (isMultiplayer) {
//                    myRef.child("player" + playerNumber).setValue(players.get(playerNumber));
//                }
            }
            boolean wasAdded = false;
            for (int i = 0; i < 10; i++) {
                points = bundle.getInt("POINTS" + String.valueOf(i), -1);
                if (points != -1) {
                    wasAdded = true;
                    if (!isMultiplayer)
                        players.get(i).setPoints(players.get(i).getPoints() + points);
//                    if (isMultiplayer) {
//                        myRef.child("player" + playerNumber).setValue(players.get(playerNumber));
//                    }
                }
            }
            if (wasAdded) {
                raceNumber++;
                players.get(playerNumber).setAvailablePoints(2);
                if (raceNumber < 11) {
                    upgradeBots();
                }
            }
            if (isMultiplayer)
                myRef.child("player" + playerNumber).setValue(players.get(playerNumber));
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

        recyclerViewRaces = root.findViewById(R.id.rv_races_info);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        recyclerViewRaces.setLayoutManager(layoutManager2);
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(recyclerViewRaces.getContext(),
                LinearLayout.VERTICAL);
        recyclerViewRaces.addItemDecoration(dividerItemDecoration2);
        adapterRaces = new RacesInfoAdapter(races);
        recyclerViewRaces.setAdapter(adapterRaces);

        btSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (players.get(playerNumber).getAvailablePoints() > 0) {
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
                if (players.get(playerNumber).getAvailablePoints() > 0) {
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
                if (players.get(playerNumber).getAvailablePoints() > 0) {
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
                if (players.get(playerNumber).getAvailablePoints() > 0) {
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
                if (((Button)view).getText().toString().toLowerCase() == "end game") {
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.popBackStack();
                    navController.navigate(R.id.startMenuFragment);
                } else {
//                    raceStarted.setValue(true);
                    saveData();
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.popBackStack();
                    navController.navigate(R.id.raceFragment);
                }
            }
        });

        return root;
    }

    void loadData() {
        int playersAmount = sPref.getInt("PLAYERS_AMOUNT", 0);
        if (playersAmount == 0) {
            players = new ArrayList<>();
            players.add(new Player("You", 200, 200, 200, 200));
            players.add(new Player("Bot 1", 200, 200, 200, 200));
            players.add(new Player("Bot 2", 200, 200, 200, 200));
            players.add(new Player("Bot 3", 200, 200, 200, 200));
            players.add(new Player("Bot 4", 200, 200, 200, 200));
            ed.putInt("PLAYERS_AMOUNT", players.size());
            ed.commit();
        } else {
            players = new ArrayList<>();
            for (int i = 0; i < playersAmount; i++) {
                String name = sPref.getString("NAME" + String.valueOf(i), "Noname");
                players.add(new Player(name, 200, 200, 200, 200));
            }
        }
        for (int i = 0; i < players.size(); i++) {
            String name = sPref.getString("NAME" + String.valueOf(i), "Noname");
            if (name != "Noname")
                players.get(i).setName(name);
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
            int availablePoints = sPref.getInt("AVAILABLE_POINTS" + String.valueOf(i), 2);
            players.get(i).setAvailablePoints(availablePoints);
        }
        raceNumber = sPref.getInt("RACE_NUMBER", 1);
        if (sPref.getInt("DISTANCE" + String.valueOf(raceNumber), -1) == -1) {
            if (!isMultiplayer) {
                randomRaces();
                upgradeBots();
            } else {
                races = new ArrayList<>();
                for (int raceNumber = 1; raceNumber <= 10; raceNumber++) {
                    races.add(new RaceInfo(raceNumber, 0, 0));
                }
            }
//        } else if (!isMultiplayer) {
            // tmp for multiplayer
        } else {
            races = new ArrayList<>();
            for (int raceNumber = 1; raceNumber <= 10; raceNumber++) {
                int distance = sPref.getInt("DISTANCE" + String.valueOf(raceNumber), 100);
                int jumpsAmount = -2;
                for (int i = 0; i <= distance; i += 100) {
                    boolean isJump = sPref.getBoolean("JUMP" + String.valueOf(raceNumber) + String.valueOf(i), false);
                    if (isJump) {
                        jumpsAmount++;
                    }
                }
                races.add(new RaceInfo(raceNumber, distance, jumpsAmount));
            }
        }
    }

    void saveData() {
        ed.putInt("RACE_NUMBER", raceNumber);
        ed.putInt("PLAYERS_AMOUNT", players.size());
        for (int i = 0; i < players.size(); i++) {
            ed.putInt("POINTS" + String.valueOf(i), players.get(i).getPoints());
            ed.putString("NAME" + String.valueOf(i), players.get(i).getName());
            ed.putInt("AVAILABLE_POINTS" + String.valueOf(i), players.get(i).getAvailablePoints());
            ed.putInt("SPEED_POINTS" + String.valueOf(i), players.get(i).getSpeed());
            ed.putInt("STAMINA_POINTS" + String.valueOf(i), players.get(i).getStamina());
            ed.putInt("AGILITY_POINTS" + String.valueOf(i), players.get(i).getAgility());
            ed.putInt("REACTION_POINTS" + String.valueOf(i), players.get(i).getReaction());
        }
        ed.commit();
    }

    void randomRaces() {
        races = new ArrayList<>();
        for (int raceNumber = 1; raceNumber <= 10; raceNumber++) {
            int randomDistance = ThreadLocalRandom.current().nextInt(1, Math.min(11, 3 + raceNumber));
            int distance = randomDistance * 100;
            ed.putInt("DISTANCE" + String.valueOf(raceNumber), distance);
            int jumpsAmount = -2;
            for (int i = 0; i <= distance; i += 100) {
                int randomNum = ThreadLocalRandom.current().nextInt(0, 2);
                if (randomNum == 1 || i == 0 || i == distance) {
                    ed.putBoolean("JUMP" + String.valueOf(raceNumber) + String.valueOf(i), true);
                    jumpsAmount++;
                } else {
                    ed.putBoolean("JUMP" + String.valueOf(raceNumber) + String.valueOf(i), false);
                }
            }
            ed.commit();
            races.add(new RaceInfo(raceNumber, distance, jumpsAmount));
        }
    }

    void upgradeBots() {
        // TODO correct check for multiplayer
        if (!isMultiplayer) {
            for (int i = 1; i < players.size(); i++) {
                for (int j = 0; j < 2; j++) {
                    int randomNum = ThreadLocalRandom.current().nextInt(0, 4);
                    int randomPoints = ThreadLocalRandom.current().nextInt(40, 50);
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
                players.get(i).setAvailablePoints(0);
            }
        }
    }

    void updateStats() {
        tvRaceNumber.setText("Race number " + String.valueOf(raceNumber));
        tvAvailablePoints.setText("Stat Points " + String.valueOf(players.get(playerNumber).getAvailablePoints()));
        tvSpeed.setText(String.valueOf(players.get(playerNumber).getSpeed()));
        tvStamina.setText(String.valueOf(players.get(playerNumber).getStamina()));
        tvAgility.setText(String.valueOf(players.get(playerNumber).getAgility()));
        tvReaction.setText(String.valueOf(players.get(playerNumber).getReaction()));
//        if (isMultiplayer)
//            btRace.setVisibility(View.INVISIBLE);
        if (raceNumber >= 11) {
            btSpeed.setVisibility(View.INVISIBLE);
            btStamina.setVisibility(View.INVISIBLE);
            btAgility.setVisibility(View.INVISIBLE);
            btReaction.setVisibility(View.INVISIBLE);
            tvAvailablePoints.setVisibility(View.INVISIBLE);
            tvRaceNumber.setText("Champ is over");
            btRace.setVisibility(View.VISIBLE);
            btRace.setText("end game");
        }
    }

}
