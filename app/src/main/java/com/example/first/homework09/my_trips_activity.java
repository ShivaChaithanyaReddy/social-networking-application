package com.example.first.homework09;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.first.homework09.Trips.ManageTripsFirebase;
import com.example.first.homework09.Trips.RecViewAdapter_for_frnd_trips;
import com.example.first.homework09.Trips.Rec_View_for_my_Trips;
import com.example.first.homework09.Trips.TripsInterface;

import java.util.ArrayList;
import java.util.List;

public class my_trips_activity extends AppCompatActivity implements TripsInterface{


    //for Recview
    private RecyclerView recyclerView;
    private Rec_View_for_my_Trips recViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    // for Recview

    List<Trip> FullTrips = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_trips_activity);


        new ManageTripsFirebase(this).getAllMyTrips();

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

    }

    @Override
    public void OnSuccessGetMyTrips(List<Trip> allTrips) {

        if(allTrips != null) {

            Log.d("AllTips:",""+allTrips);
            //RecyclerView Code starts:
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView_for_frnds_trips_view);
            mLayoutManager = new LinearLayoutManager(my_trips_activity.this);
            recyclerView.setLayoutManager(mLayoutManager);
            recViewAdapter = new Rec_View_for_my_Trips(allTrips, this,this);
            recyclerView.setAdapter(recViewAdapter);
            //RecyclerView Code ends:


        }else {
            Toast.makeText(this,"Sorry, no trips  available",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void OnGetAllFriendsSuccess(String friends) {

    }
}
