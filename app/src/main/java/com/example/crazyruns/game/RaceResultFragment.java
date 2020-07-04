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

import com.example.crazyruns.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class RaceResultFragment extends Fragment {
    private Button btOk;
    private RecyclerView recyclerView;
    private RacersResultAdapter adapter;
    private ArrayList<Racer> racers = new ArrayList<>();
    private Bundle bundle;


    public RaceResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_race_result, container, false);
        btOk = root.findViewById(R.id.bt_result_ok);
        recyclerView = root.findViewById(R.id.rv_race_result);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        setRacers();
        adapter = new RacersResultAdapter(racers);
        recyclerView.setAdapter(adapter);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.popBackStack();
                navController.navigate(R.id.gameFragment, bundle);

            }
        });
        return root;
    }

    void setRacers() {
        bundle = getArguments();
        SharedPreferences sPref = getActivity().getPreferences(MODE_PRIVATE);
        int playerNumber = sPref.getInt("PLAYER_NUMBER", 0);
        String roomNumber = sPref.getString("ROOM_NUMBER", "0");
        boolean isMultiplayer = sPref.getBoolean("MULTIPLAYER", false);
        for (int i = 0; i < 10; i++) {
            int points = bundle.getInt("POINTS" + String.valueOf(i), -1);
            String name = sPref.getString("NAME" + String.valueOf(i), "Noname");
            if (points != -1) {
                racers.add(new Racer(name, 0, 0, 0, 0, 0));
                racers.get(i).setPoints(points);
            }
            float time = bundle.getFloat("TIME" + String.valueOf(i), -1);
            if (time != -1)
                racers.get(i).setTime(time);
            if (isMultiplayer && playerNumber == i) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("rooms").child(roomNumber);
                myRef.child("player" + playerNumber).child("points").setValue(points);
            }
        }
    }

}
