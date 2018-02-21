package com.example.first.homework09.Trips;

import android.util.Log;

import com.example.first.homework09.FirebaseUserEntity;
import com.example.first.homework09.Friends.ManageFriendsFirebase;
import com.example.first.homework09.Trip;
import com.example.first.homework09.my_trips_activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chaithanya on 4/22/2017.
 */

public class ManageTripsFirebase {

    private static final String TAG = ManageFriendsFirebase.class.getSimpleName();

    TripsInterface tripsInterface;

    public ManageTripsFirebase() {
    }

    public ManageTripsFirebase(TripsInterface tripsInterface) {
        this.tripsInterface = tripsInterface;
    }

    public void GetAllFrndsTrips(final String friends) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = user.getUid();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        final List<Trip> allTrips = new ArrayList<>();


        myRef.child("UserTrips").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                   for(DataSnapshot postSnap: postSnapshot.getChildren()){
                       if(postSnap.getKey().equals("Profile")) {
                           Trip t = postSnap.getValue(Trip.class);
                           String mems = t.getMembers();
                           if(mems != null) {
                               if(!mems.contains(userID) && !t.getAdmin().equals(userID)) {
                                   if(friends != null) {
                                       if(friends.contains(t.getAdmin()))
                                           allTrips.add(postSnap.getValue(Trip.class));
                                   }
                               }
                           }else {
                               if(!t.getAdmin().equals(userID)){
                                   if(friends != null) {
                                       if(friends.contains(t.getAdmin()))
                                           allTrips.add(postSnap.getValue(Trip.class));
                                   }
                               }
                           }

                       }
                   }
                }
                tripsInterface.OnSuccessAllFrndsTrips(allTrips);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void SendFrndTripJoinRequest(final Trip news, final int position) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = user.getUid();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        final List<Trip> allTrips = new ArrayList<>();

        myRef.child("UserTrips").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.d(TAG, "Value is: " + postSnapshot);
                    if(postSnapshot.getKey().equals(news.getTripID())) {

                        for(DataSnapshot snapShot: postSnapshot.getChildren()) {
                            if(snapShot.getKey().equals("Profile")) {
                                Trip t = snapShot.getValue(Trip.class);
                                Log.d("awawaw",""+t);

                                if(t.getMembers() == null || t.getMembers().isEmpty()) {
                                        t.setMembers(userID);
                                    }else {
                                        t.setMembers(t.getMembers()+" | "+userID);
                                    }
                                    myRef.child("UserTrips").child(news.getTripID()).child("Profile").setValue(t);
                            }
                        }

                    }
                }

                tripsInterface.OnSuccessJoinFrndTrip("Successfully Joined the trip "+news.getName(),news,position);


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });




    }

    public void getAllJJoinedTrips() {


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = user.getUid();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        final List<Trip> allTrips = new ArrayList<>();

        myRef.child("UserTrips").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.d(TAG, "Value is: " + postSnapshot);
                        for(DataSnapshot snapShot: postSnapshot.getChildren()) {
                            if(snapShot.getKey().equals("Profile")) {
                                Trip t = snapShot.getValue(Trip.class);
                                String mems = t.getMembers();
                                if(mems != null) {
                                    if(mems.contains(userID) && !t.getAdmin().equals(userID)) {
                                        allTrips.add(snapShot.getValue(Trip.class));
                                    }
                                }

                            }
                        }

                }

                tripsInterface.OnSuccessGetJoinedTrips(allTrips);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void getAllMyTrips() {


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = user.getUid();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        final List<Trip> allTrips = new ArrayList<>();

        myRef.child("UserTrips").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.d(TAG, "Value is: " + postSnapshot);
                    for(DataSnapshot snapShot: postSnapshot.getChildren()) {
                        if(snapShot.getKey().equals("Profile")) {
                            Trip t = snapShot.getValue(Trip.class);
                            if(t.getAdmin().equals(userID)) {
                                    allTrips.add(snapShot.getValue(Trip.class));

                            }

                        }
                    }

                }

                tripsInterface.OnSuccessGetMyTrips(allTrips);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



    }

    public void getAllFriendsFromFirebase(final String userID) {



        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("UserProfiles").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.d(TAG, "Value of chat is : Key is " + postSnapshot.getKey()+"value is : "+postSnapshot.getValue());

                    if(postSnapshot.getKey().equals(userID)) {

                        FirebaseUserEntity fib = postSnapshot.getValue(FirebaseUserEntity.class);

                        String friends = fib.getMyFriends();


                        tripsInterface.OnGetAllFriendsSuccess(friends);
                        // AllReqs.add(postSnapshot.getValue(FirebaseUserEntity.class));
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // chatInterface.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
            }
        });

    }
}
