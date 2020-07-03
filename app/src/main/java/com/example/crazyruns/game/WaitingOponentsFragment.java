package com.example.crazyruns.game;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.crazyruns.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.ThreadLocalRandom;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaitingOponentsFragment extends Fragment {
    private TextView tvPlayersAmount;
    private int playersAmount = 0;
    private int playerNumber = -1;
    private String name;
    private String roomNumber;
    SharedPreferences sPref;
    SharedPreferences.Editor ed;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    ValueEventListener listener;


    public WaitingOponentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_waiting_oponents, container, false);
        tvPlayersAmount = root.findViewById(R.id.tv_waiting_number);
        roomNumber = "1";
        myRef = database.getReference("rooms").child(roomNumber);
        playersAmount = 0;
        sPref = getActivity().getPreferences(MODE_PRIVATE);
        ed = sPref.edit();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                playersAmount = dataSnapshot.child("amount").getValue(Integer.class);
                tvPlayersAmount.setText(String.valueOf(playersAmount) + "/5");
                if (playerNumber == -1) {
                    playerNumber = playersAmount;
                    name = sPref.getString("NAME0", "Noname");
                    myRef.child("amount").setValue(playerNumber + 1);
                    Player player = new Player(name, 200, 200, 200, 200);
                    myRef.child("player" + String.valueOf(playerNumber)).setValue(player);
                    ed.putInt("PLAYER_NUMBER", playerNumber);
                    ed.commit();
                    if (playerNumber == 0) {
                        randomRaces();
                    }
                }
                if (playersAmount == 3) {
                    ed.putInt("PLAYERS_AMOUNT", playersAmount);
                    for (int i = 0; i < playersAmount; i++) {
                        Player player = new Player(name, 200, 200, 200, 200);
                        String name = dataSnapshot.child("player" + String.valueOf(i)).child("name").getValue(String.class);
                        player.setName(name);
//                        player = dataSnapshot.child("player" + String.valueOf(i)).getValue(Player.class);
                        ed.putInt("POINTS" + String.valueOf(i), player.getPoints());
                        ed.putString("NAME" + String.valueOf(i), player.getName());
                        ed.putInt("SPEED_POINTS" + String.valueOf(i), player.getSpeed());
                        ed.putInt("STAMINA_POINTS" + String.valueOf(i), player.getStamina());
                        ed.putInt("AGILITY_POINTS" + String.valueOf(i), player.getAgility());
                        ed.putInt("REACTION_POINTS" + String.valueOf(i), player.getReaction());
                    }
                    ed.commit();
                    goToGame();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("MY", "Failed to read value.", error.toException());
            }
        };
        myRef.addValueEventListener(listener);


        return root;
    }

    void randomRaces() {
        for (int raceNumber = 1; raceNumber <= 10; raceNumber++) {
            int randomDistance = ThreadLocalRandom.current().nextInt(1, Math.min(11, 3 + raceNumber));
            int distance = randomDistance * 100;
            DatabaseReference racesRef = myRef.child("races").child(String.valueOf(raceNumber));
            racesRef.child("distance").setValue(distance);
//            ed.putInt("DISTANCE" + String.valueOf(raceNumber), distance);
            int jumpsAmount = -2;
            for (int i = 0; i <= distance; i += 100) {
                int randomNum = ThreadLocalRandom.current().nextInt(0, 2);
                if (randomNum == 1 || i == 0 || i == distance) {
                    racesRef.child("jump").child(String.valueOf(i)).setValue(true);
//                    ed.putBoolean("JUMP" + String.valueOf(raceNumber) + String.valueOf(i), true);
                    jumpsAmount++;
                } else {
                    racesRef.child("jump").child(String.valueOf(i)).setValue(false);
//                    ed.putBoolean("JUMP" + String.valueOf(raceNumber) + String.valueOf(i), false);
                }
            }
            ed.commit();
        }
    }


    void goToGame() {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.popBackStack();
        sPref = getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean("MULTIPLAYER", true);
        ed.putString("ROOM_NUMBER", roomNumber);
        ed.commit();
        navController.navigate(R.id.gameFragment);
        myRef.removeEventListener(listener);
    }

}
