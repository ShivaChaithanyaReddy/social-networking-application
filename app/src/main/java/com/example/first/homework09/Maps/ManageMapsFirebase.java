package com.example.first.homework09.Maps;

import android.util.Log;

import com.example.first.homework09.Trip;
import com.example.first.homework09.TripPlaceClass;
import com.example.first.homework09.map_for_places_in_trip;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chaithanya on 4/29/2017.
 */

public class ManageMapsFirebase {

    private MapsInterface mapsInterface;

    public ManageMapsFirebase(MapsInterface mapsInterface) {
        this.mapsInterface = mapsInterface;
    }



    public void getAllPlacesOfTrip(final String trip_id) {

        final List<TripPlaceClass> allPlaces = new ArrayList<>();



        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("UserTrips").child(trip_id).getRef().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {



            //    Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
if(dataSnapshot.getKey().equals("Places")) {
    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {


        TripPlaceClass tripPlaceClass = postSnapshot.getValue(TripPlaceClass.class);

        allPlaces.add(tripPlaceClass);
        Log.d("kakak","In getAllPlacesOfTrip: each place is: key:"+postSnapshot.getValue()+" value is: "+postSnapshot.getValue());

    }
    mapsInterface.OnSuccessGetAllPlaces(allPlaces);

}
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        /*

        databaseReference.child("UserTrips").child(trip_id).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("UserTrips")
                        .child(trip_id).child("Places").addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String place = (String) dataSnapshot.getValue();

                        allPlaces.add(place);

Log.d("kakak","In getAllPlacesOfTrip: each place is: "+place);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                Log.d("kakak","In getAllPlacesOfTrip: all places are: "+allPlaces);

                mapsInterface.OnSuccessGetAllPlaces(allPlaces);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });*/

    }
}
