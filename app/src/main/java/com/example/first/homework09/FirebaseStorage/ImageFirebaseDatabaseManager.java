package com.example.first.homework09.FirebaseStorage;

import android.net.Uri;
import android.util.Log;

import com.example.first.homework09.FirebaseUserEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Chaithanya on 4/28/2017.
 */

public class ImageFirebaseDatabaseManager {

    public void saveImagePathToDatabase(final FirebaseUserEntity currentUserDetails, final Uri downloadUrl) {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = user.getUid();


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("UserProfiles").getRef().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Log.d("askdjbaskjdbsa",""+dataSnapshot);


                FirebaseUserEntity user = currentUserDetails;

                user.setImage(downloadUrl.toString());

                databaseReference.child("UserProfiles").child(userId).setValue(user);


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

                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("askdjbaskjdbsa",""+dataSnapshot.child("UserProfiles"));

               *//* Map<String, Object> userUpdates = new HashMap<String, Object>();

                userUpdates.put("image",downloadUrl);

                Log.d("njnjnj",""+downloadUrl);

                databaseReference.child("UserProfiles").child(userId).updateChildren(userUpdates);
*//*
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
              //  tripInterface.onSendMessageFailure("Unable to send message: " + databaseError.getMessage());
            }
        });*/

    }
}
