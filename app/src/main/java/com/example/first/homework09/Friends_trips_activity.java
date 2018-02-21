package com.example.first.homework09;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.first.homework09.Trips.ManageTripsFirebase;
import com.example.first.homework09.Trips.RecViewAdapter_for_frnd_trips;
import com.example.first.homework09.Trips.TripsInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class Friends_trips_activity extends AppCompatActivity implements TripsInterface{


    //for Recview
    private RecyclerView recyclerView;
    private RecViewAdapter_for_frnd_trips recViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    // for Recview

List<Trip> FullTrips = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_trips_activity);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        new ManageTripsFirebase(this).getAllFriendsFromFirebase(userID);
    }

    @Override
    public void OnSuccessAllFrndsTrips(List<Trip> allTrips) {
        if(allTrips != null) {

            FullTrips = allTrips;

            Log.d("all allall:",""+FullTrips);

            //RecyclerView Code starts:
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView_for_frnds_trips_view);
            mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recViewAdapter = new RecViewAdapter_for_frnd_trips(FullTrips, this,this);
            recyclerView.setAdapter(recViewAdapter);
            //RecyclerView Code ends:

        }else {
            Toast.makeText(this,"Sorry, no trips  available",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnClickFrndTripJoinRequest(Trip news, int position) {

        new ManageTripsFirebase(this).SendFrndTripJoinRequest(news,position);
    }

    @Override
    public void OnSuccessJoinFrndTrip(String s, Trip news, int position) {

        FullTrips.remove(position);

        Log.d("remaining trips:",""+FullTrips);

      //  recViewAdapter.notifyDataSetChanged();
       // recViewAdapter = new RecViewAdapter_for_frnd_trips(FullTrips, this,this);
        //recyclerView.setAdapter(recViewAdapter);
    }

    @Override
    public void OnSuccessGetJoinedTrips(List<Trip> allTrips) {

    }

    @Override
    public void OnSuccessGetMyTrips(List<Trip> allTrips) {

    }

    @Override
    public void OnGetAllFriendsSuccess(String friends) {

        new ManageTripsFirebase(this).GetAllFrndsTrips(friends);

    }
}
