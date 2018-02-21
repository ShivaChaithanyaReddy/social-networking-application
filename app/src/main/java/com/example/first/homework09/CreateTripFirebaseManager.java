package com.example.first.homework09;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.location.places.Place;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by Chaithanya on 4/21/2017.
 */

public class CreateTripFirebaseManager {

    private static final String TAG = CreateTripFirebaseManager.class.getSimpleName();

    TripInterface tripInterface;

    public CreateTripFirebaseManager(TripInterface tripInterface) {
        this.tripInterface = tripInterface;
    }

    public CreateTripFirebaseManager() {
    }

    public void CreateTripinFirebase(final Trip trip) {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("UserTrips").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "trip created....");
                databaseReference.child("UserTrips").child(String.valueOf(trip.getTripID())).child("Profile").setValue(trip);
                tripInterface.OnSuccessTripCreate("Trip "+ trip.getName()+" Created Successfully", trip);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                tripInterface.onCreateTripFailure("Unable to send message: " + databaseError.getMessage());
            }
        });
    }


    public void SaveChatForTripInFirebase(final Chat chat) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("UserTrips").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                databaseReference.child("UserTrips").child(chat.receiverUid).child("Chats").child(String.valueOf(chat.timestamp)).setValue(chat);
                tripInterface.OnSuccessSendMessage(chat);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                tripInterface.onSendMessageFailure("Unable to send message: " + databaseError.getMessage());
            }
        });
    }

    public void GetTripChats(final Trip trip) {

        final List<Chat> AllChats = new ArrayList<>();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("UserTrips").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(trip.getTripID())) {
                    Log.e(TAG, "getMessageFromFirebaseTrips: " + trip.getTripID() + " exists");
                    if(dataSnapshot.child(trip.getTripID()).hasChild("Chats")) {
                        for (DataSnapshot postSnapshot: dataSnapshot.child(trip.getTripID()).child("Chats").getChildren()) {
                            Log.d(TAG, "Value of chat is: " + postSnapshot);
                            AllChats.add(postSnapshot.getValue(Chat.class));
                        }
                        tripInterface.OnGetChatSuccess(AllChats);
                    }

                } else {
                    Log.e(TAG, "getMessageFromFirebaseTrip: no such room available");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                tripInterface.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
            }
        });
    }

    public void savePlaceInTheTripFirebase(final TripPlaceClass name, final Trip trip) {


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("UserTrips").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UUID place_id = UUID.randomUUID();
                databaseReference.child("UserTrips").child(trip.getTripID()).child("Places").child(String.valueOf(place_id)).setValue(name);

                tripInterface.OnSuccessSavePlaceMessage("Place "+name.getPlace()+" successfully saved in the trip "+trip.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                tripInterface.onSendMessageFailure("Unable to send message: " + databaseError.getMessage());
            }
        });



    }

    public void getAllTripPlaces(final String trip_id) {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("UserTrips").child(trip_id).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("UserTrips")
                        .child(trip_id).child("Places").addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                        Map<Integer,Object> map = new HashMap<Integer, Object>();

                        Log.d("nanan",""+dataSnapshot.getValue());

                        String place =  dataSnapshot.getValue(TripPlaceClass.class).getPlace().toString();

                        tripInterface.OnSuccessGetPlaceNameOfTrip(place);
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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                tripInterface.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
            }
        });

    }

    public void RemovePlaceFromTrip(final String trip_id, final String placeToDelete) {


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("UserTrips").child(trip_id).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("UserTrips")
                        .child(trip_id).child("Places").addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String place =  dataSnapshot.getValue(TripPlaceClass.class).getPlace().toString();
                    Log.d("papap",""+place+" obtained is: "+placeToDelete);
                        if(place.equals(placeToDelete)) {
                            Log.d("papa place found....","");
                            dataSnapshot.getRef().removeValue();
                            tripInterface.OnSuccessDeletePlaceFroTrip("Successfully deleted Place: "+dataSnapshot.getValue()+" from Trip");
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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                tripInterface.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
            }
        });





    }
}
