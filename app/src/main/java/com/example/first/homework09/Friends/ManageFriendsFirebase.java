package com.example.first.homework09.Friends;

import android.util.Log;

import com.example.first.homework09.FirebaseUserEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chaithanya on 4/21/2017.
 */

public class ManageFriendsFirebase {

    FriendsInterface friendsInterface;
    private static final String TAG = ManageFriendsFirebase.class.getSimpleName();


    public ManageFriendsFirebase(FriendsInterface friendsInterface) {
        this.friendsInterface = friendsInterface;
    }

    public ManageFriendsFirebase() {
    }



    public void getUser(final String userID) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child("UserProfiles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                if (dataSnapshot.hasChild(userID)) {
                    Log.d(TAG,"datasnapshot is: "+dataSnapshot.child(userID).getValue(FirebaseUserEntity.class).toString());
                    FirebaseUserEntity fbaseEntity = dataSnapshot.child(userID).getValue(FirebaseUserEntity.class);
                    Log.d("my output:",""+fbaseEntity);
                    friendsInterface.CurrentUser(fbaseEntity);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }



    public void getAllUsers() {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = user.getUid();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        final List<FirebaseUserEntity> allUsers = new ArrayList<>();

        myRef.child("UserProfiles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.d(TAG, "Value is: " + postSnapshot);
                    if(!postSnapshot.getKey().equals(userID)) {
                        if(postSnapshot.getValue(FirebaseUserEntity.class).getMyFriends() != null) {
                            Log.d("myfrnds:",""+postSnapshot.getValue(FirebaseUserEntity.class).getMyFriends());
                            if(!postSnapshot.getValue(FirebaseUserEntity.class).getMyFriends().contains(postSnapshot.getKey())) {
                                allUsers.add(postSnapshot.getValue(FirebaseUserEntity.class));
                            }
                        }else {
                            Log.d("myfrnds:",""+postSnapshot.getValue(FirebaseUserEntity.class).getMyFriends());
                            allUsers.add(postSnapshot.getValue(FirebaseUserEntity.class));

                        }
                    }
                }


                friendsInterface.allUsersForFriends(allUsers);
                //      editInterface.allUsersToChat();
                /*if (dataSnapshot.hasChild(userID)) {
                    Log.d(TAG,"datasnapshot is: "+dataSnapshot.child(userID).getValue(FirebaseUserEntity.class).toString());
                    FirebaseUserEntity fbaseEntity = dataSnapshot.child(userID).getValue(FirebaseUserEntity.class);
                    Log.d("my output:",""+fbaseEntity);
                    editInterface.getEditProfile(fbaseEntity);
                }*/

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }

    public void sendFrndRequest(final FirebaseUserEntity news) {


        Log.e("kkk In firebase",""+news);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = user.getUid();



        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        myRef.child("UserProfiles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                if(dataSnapshot.child(userID).getValue(FirebaseUserEntity.class).getMyPendingRequest() == null ) {

                    FirebaseUserEntity fbaseEntity =  dataSnapshot.child(userID).getValue(FirebaseUserEntity.class);
                    fbaseEntity.setMyPendingRequest(news.getuserID());
                    Log.e("TESTING","the value is null"+ dataSnapshot.child(userID).getValue(FirebaseUserEntity.class));

                    databaseReference.child("UserProfiles").child(userID).setValue(fbaseEntity);
                    Log.e("TESTING2","the value is null"+ dataSnapshot.child(userID).getValue(FirebaseUserEntity.class));

                }else {
                    FirebaseUserEntity fbaseEntity =  dataSnapshot.child(userID).getValue(FirebaseUserEntity.class);
                    Log.e("TESTING3","the value is null"+ fbaseEntity);

                    String allPending  = fbaseEntity.getMyPendingRequest();
                    allPending = allPending+" | "+news.getuserID();
                    fbaseEntity.setMyPendingRequest(allPending);
                    databaseReference.child("UserProfiles").child(userID).setValue(fbaseEntity);
                }

                if(dataSnapshot.child(news.getuserID()).getValue(FirebaseUserEntity.class).getReceivedFriendRequests() == null ) {
                    FirebaseUserEntity fbaseEntity =  dataSnapshot.child(news.getuserID()).getValue(FirebaseUserEntity.class);
                    fbaseEntity.setReceivedFriendRequests(userID);
                    Log.e("TESTING","the value is null"+ dataSnapshot.child(userID).getValue(FirebaseUserEntity.class));

                    databaseReference.child("UserProfiles").child(news.getuserID()).setValue(fbaseEntity);
                    Log.e("TESTING2","the value is null"+ dataSnapshot.child(userID).getValue(FirebaseUserEntity.class));

                }else {
                    FirebaseUserEntity fbaseEntity =  dataSnapshot.child(news.getuserID()).getValue(FirebaseUserEntity.class);
                    Log.e("TESTING3","the value is null"+ fbaseEntity);

                    String allPending  = fbaseEntity.getReceivedFriendRequests();
                    allPending = allPending+" | "+userID;
                    fbaseEntity.setReceivedFriendRequests(allPending);
                    databaseReference.child("UserProfiles").child(news.getuserID()).setValue(fbaseEntity);
                }

                friendsInterface.OnSuccessSendFriendRequest("Friend Request to + "+news.getEmail()+" sent");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });




    }
}
