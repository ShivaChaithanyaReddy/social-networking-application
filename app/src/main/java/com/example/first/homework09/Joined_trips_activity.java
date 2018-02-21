package com.example.first.homework09;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.first.homework09.Trips.ManageTripsFirebase;
import com.example.first.homework09.Trips.RecViewAdapter_for_frnd_trips;
import com.example.first.homework09.Trips.Rec_view_For_all_joined_trips;
import com.example.first.homework09.Trips.TripsInterface;

import java.util.ArrayList;
import java.util.List;

public class Joined_trips_activity extends AppCompatActivity implements TripsInterface{


    //for Recview
    private RecyclerView recyclerView;
    private Rec_view_For_all_joined_trips recViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    // for Recview

    List<Trip> FullTrips = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_trips_activity);


        new ManageTripsFirebase(this).getAllJJoinedTrips();
    }

    @Override
    public void OnSuccessAllFrndsTrips(List<Trip> allTrips) {

    }

    @Override
    public void OnClickFrndTripJoinRequest(Trip news, int position) {

    }

    @Override
    public void OnSuccessJoinFrndTrip(String s, Trip news, int position) {

    }

    @Override
    public void OnSuccessGetJoinedTrips(List<Trip> allTrips) {


        if(allTrips != null) {

            FullTrips = allTrips;

            //RecyclerView Code starts:
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView_for_joined_trips_view);
            mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recViewAdapter = new Rec_view_For_all_joined_trips(FullTrips, this,this);
            recyclerView.setAdapter(recViewAdapter);
            //RecyclerView Code ends:




        }else {
            Toast.makeText(this,"Sorry, no trips  available",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnSuccessGetMyTrips(List<Trip> allTrips) {

    }

    @Override
    public void OnGetAllFriendsSuccess(String friends) {

    }
}
