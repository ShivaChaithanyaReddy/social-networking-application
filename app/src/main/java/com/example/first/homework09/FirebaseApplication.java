package com.example.first.homework09;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.PipedReader;
import java.util.ArrayList;
import java.util.List;

public class FirebaseApplication extends Application{

    private static final String TAG = FirebaseApplication.class.getSimpleName();

    private EditInterface editInterface;
    public static boolean status = false;
    public FirebaseAuth firebaseAuth;

    private  ChatInterface chatInterface;

    public FirebaseAuth.AuthStateListener mAuthListener;

    public FirebaseApplication(EditInterface editInterface) {
        this.editInterface = editInterface;
    }

    public FirebaseApplication(ChatInterface chatInterface) {
        this.chatInterface = chatInterface;
    }

    public FirebaseApplication(){}

    public FirebaseAuth getFirebaseAuth(){
        return firebaseAuth = FirebaseAuth.getInstance();
    }

    public String getFirebaseUserAuthenticateId() {
        String userId = null;
        if(firebaseAuth.getCurrentUser() != null){
            userId = firebaseAuth.getCurrentUser().getUid();
        }
        return userId;
    }

    public void checkUserLogin(final Context context){
        if(firebaseAuth.getCurrentUser() != null){
            Intent profileIntent = new Intent(context, ProfileActivity.class);
            context.startActivity(profileIntent);
        }
    }


    public void getAllUsersForChat(final String UserID) {

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
                        if(!postSnapshot.getKey().equals(UserID)) {
                            allUsers.add(postSnapshot.getValue(FirebaseUserEntity.class));
                        }
                }


                editInterface.allUsersForChatting(allUsers);
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

    public void getUser(final String userID, final int fromWhere, final FirebaseUserEntity userEntity) {

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
                    if(fromWhere == 1) {
                        editInterface.getInitialProfile(fbaseEntity, userEntity);
                    }else {
                        editInterface.getEditProfile(fbaseEntity);

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }


    public void isUserCurrentlyLogin(final Context context){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(null != user){
        //            Intent profileIntent = new Intent(context, ProfileActivity.class);
        //            context.startActivity(profileIntent);
                }else{
                    Intent loginIntent = new Intent(context, LoginActivity.class);
                    context.startActivity(loginIntent);
                }
            }
        };
    }

    public void createNewUser(final Context context, String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                if (!task.isSuccessful()) {
                    Toast.makeText(context, "Registration Failed!!", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(context, "Registration Successful!!", Toast.LENGTH_LONG).show();

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    FirebaseUserEntity userEntity = new FirebaseUserEntity(null, null, user.getUid(), null ,user.getEmail(),null, null, null,null,null,null);
                    FirebaseDatabaseHelper firebaseDatabaseHelper = new FirebaseDatabaseHelper();
                    firebaseDatabaseHelper.createUserInFirebaseDatabase(user.getUid(), userEntity);
                }
            }
        });
    }

    public void loginAUser(final Context context, String email, String password, final ProgressDialog progressDialog){

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity)context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                            Log.w(TAG, "signInWithEmail", task.getException());
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(context, "User has been logged in", Toast.LENGTH_SHORT).show();
                            Intent profileIntent = new Intent(context, ProfileActivity.class);
                           context.startActivity(profileIntent);
                        }
                    }
                });
    }

    public void SaveMessageinFirebase(final Chat chat) {

        final String room_type_1 = chat.senderUid + "_" + chat.receiverUid;
        final String room_type_2 = chat.receiverUid + "_" + chat.senderUid;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("UserChats").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room_type_1)) {
                    Log.e(TAG, "sendMessageToFirebaseUser: " + room_type_1 + " exists");
                    databaseReference.child("UserChats").child(room_type_1).child(String.valueOf(chat.timestamp)).setValue(chat);
                    chatInterface.onGetMessagesSuccess(chat);
                } else if (dataSnapshot.hasChild(room_type_2)) {
                    Log.e(TAG, "sendMessageToFirebaseUser: " + room_type_2 + " exists");
                    databaseReference.child("UserChats").child(room_type_2).child(String.valueOf(chat.timestamp)).setValue(chat);
                    chatInterface.onGetMessagesSuccess(chat);
                } else {
                    Log.e(TAG, "sendMessageToFirebaseUser: success");
                    databaseReference.child("UserChats").child(room_type_1).child(String.valueOf(chat.timestamp)).setValue(chat);
                    chatInterface.onGetMessagesSuccess(chat);
                }
                chatInterface.onSendMessageSuccess();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                chatInterface.onSendMessageFailure("Unable to send message: " + databaseError.getMessage());
            }
        });


    }



    public void getMessageFromFirebaseUser(String senderUid, String receiverUid) {
        final String room_type_1 = senderUid + "_" + receiverUid;
        final String room_type_2 = receiverUid + "_" + senderUid;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("UserChats").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room_type_1)) {
                    Log.e(TAG, "getMessageFromFirebaseUser: " + room_type_1 + " exists");
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("UserChats")
                            .child(room_type_1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Chat chat = dataSnapshot.getValue(Chat.class);
                            chatInterface.onGetMessagesSuccess(chat);
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
                            chatInterface.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
                        }
                    });
                } else if (dataSnapshot.hasChild(room_type_2)) {
                    Log.e(TAG, "getMessageFromFirebaseUser: " + room_type_2 + " exists");
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("UserChats")
                            .child(room_type_2).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Chat chat = dataSnapshot.getValue(Chat.class);
                            chatInterface.onGetMessagesSuccess(chat);
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
                            chatInterface.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
                        }
                    });
                } else {
                    Log.e(TAG, "getMessageFromFirebaseUser: no such room available");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                chatInterface.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
            }
        });
    }

    public void getAllMsgsOfChat(String receiverUid, String senderUid) {
        final String room_type_1 = senderUid + "_" + receiverUid;
        final String room_type_2 = receiverUid + "_" + senderUid;

        final List<Chat> AllChats = new ArrayList<>();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("UserChats").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room_type_1)) {
                    Log.e(TAG, "getMessageFromFirebaseUser: " + room_type_1 + " exists");

                    for (DataSnapshot postSnapshot: dataSnapshot.child(room_type_1).getChildren()) {
                        Log.d(TAG, "Value of chat is: " + postSnapshot);
                            AllChats.add(postSnapshot.getValue(Chat.class));
                    }

                    chatInterface.OnGetChatSuccess(AllChats);
                } else if (dataSnapshot.hasChild(room_type_2)) {
                    Log.e(TAG, "getMessageFromFirebaseUser: " + room_type_2 + " exists");

                    for (DataSnapshot postSnapshot: dataSnapshot.child(room_type_2).getChildren()) {
                        Log.d(TAG, "Value of chat is: " + postSnapshot);
                        AllChats.add(postSnapshot.getValue(Chat.class));
                    }


                    chatInterface.OnGetChatSuccess(AllChats);
                } else {
                    Log.e(TAG, "getMessageFromFirebaseUser: no such room available");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                chatInterface.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
            }
        });

    }

    public void getAllRecievedFrndReq() {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = user.getUid();


Log.d("demo : ","allreceived for: "+userID);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        myRef.child("UserProfiles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.e("TESTING4","the value is nullll"+dataSnapshot.child(userID).getValue(FirebaseUserEntity.class));

                if(dataSnapshot.child(userID).getValue(FirebaseUserEntity.class).getReceivedFriendRequests() == null ) {
                  /*  FirebaseUserEntity fbaseEntity =  dataSnapshot.child(news.getuserID()).getValue(FirebaseUserEntity.class);
                    fbaseEntity.setReceivedFriendRequests(news.getuserID());
                    Log.e("TESTING","the value is null"+ dataSnapshot.child(userID).getValue(FirebaseUserEntity.class));

                    databaseReference.child("UserProfiles").child(news.getuserID()).setValue(fbaseEntity);
                    Log.e("TESTING2","the value is null"+ dataSnapshot.child(userID).getValue(FirebaseUserEntity.class));
*/                    Log.e("TESTING4","the value is nullll"+userID);

                }else {
                    FirebaseUserEntity fbaseEntity =  dataSnapshot.child(userID).getValue(FirebaseUserEntity.class);
                    Log.e("TESTING4","the value is null"+ fbaseEntity);

                    String allPending  = fbaseEntity.getReceivedFriendRequests();

                    editInterface.OnSuccessGetAllFriendRequestsString(allPending);
              }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });





    }

    public void getAllRecievedFrndReqMain(final String allPending) {

        final List<FirebaseUserEntity> AllReqs = new ArrayList<>();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("UserProfiles").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Log.d(TAG+"demo ", "User is : Key is " + postSnapshot.getKey()+"value is : "+postSnapshot.getValue(FirebaseUserEntity.class));

                        if(allPending.contains(postSnapshot.getValue(FirebaseUserEntity.class).getuserID())) {
                            Log.d("demo :","allPending contain the user. . . ");
                            AllReqs.add(postSnapshot.getValue(FirebaseUserEntity.class));
                        }
                    }

                    editInterface.OnGetAllReqsMainSuccess(AllReqs);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               // chatInterface.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
            }
        });


    }

    public void ApproveFrndsRequest(final FirebaseUserEntity news) {


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = user.getUid();

        Log.d("qpqpqpq aslkn other :",""+news.getuserID()+" mine: "+userID);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("UserProfiles").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.d(TAG, "Value of chat is : Key is " + postSnapshot.getKey()+"value is : "+postSnapshot.getValue());

                    if(postSnapshot.getKey().equals(userID)) {

                       FirebaseUserEntity fib = postSnapshot.getValue(FirebaseUserEntity.class);
                        String ss = fib.getReceivedFriendRequests();
                        ss = ss.replace(news.getuserID()+" | ","");
                        ss = ss.replace(" | "+news.getuserID(),"");
                        ss = ss.replace(news.getuserID(),"");
                        fib.setReceivedFriendRequests(ss);
                        Log.e("kakaka",""+postSnapshot.getValue(FirebaseUserEntity.class));

                        String friends = fib.getMyFriends();

                        if(friends == null) {
                            friends = news.getuserID();
                        }else {
                            friends = " | "+news.getuserID();
                        }

                        fib.setMyFriends(friends);


                        databaseReference.child("UserProfiles").child(userID).setValue(fib);


                        // AllReqs.add(postSnapshot.getValue(FirebaseUserEntity.class));
                    }

                    if(postSnapshot.getKey().equals(news.getuserID())) {


                        Log.d("qpqpqpq", " present user::::"+news);
                        FirebaseUserEntity fib = postSnapshot.getValue(FirebaseUserEntity.class);
                        String ss = fib.getMyPendingRequest();
                        Log.d("qpqpqpq"," Pending requests are: "+ss);

                        ss = ss.replace(news.getMyPendingRequest()+" | ","");
                        ss = ss.replace(" | "+news.getMyPendingRequest(),"");
                        ss = ss.replace(news.getMyPendingRequest(),"");
                        fib.setMyPendingRequest(ss);
                        Log.d("llalla-two",""+ss);



                        String friends = fib.getMyFriends();

                        if(friends == null) {
                            friends =  userID;
                        }else {
                            friends = " | "+userID;
                        }

                        fib.setMyFriends(friends);

                        databaseReference.child("UserProfiles").child(news.getuserID()).setValue(fib);


                    }
                }

              //  editInterface.OnGetAllReqsMainSuccess(AllReqs);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // chatInterface.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
            }
        });




    }

    public void DisapproveFrndRequest(final FirebaseUserEntity news) {


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = user.getUid();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("UserProfiles").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.d(TAG, "Value of chat is : Key is " + postSnapshot.getKey()+"value is : "+postSnapshot.getValue());

                    if(postSnapshot.getKey().equals(userID)) {

                        FirebaseUserEntity fib = postSnapshot.getValue(FirebaseUserEntity.class);
                        String ss = fib.getReceivedFriendRequests();
                        ss = ss.replace(news.getuserID(),"");
                        fib.setReceivedFriendRequests(ss);
                        Log.e("kakaka",""+postSnapshot.getValue(FirebaseUserEntity.class));

                        databaseReference.child("UserProfiles").child(userID).setValue(fib);


                    }

                    if(postSnapshot.getKey().equals(news.getuserID())) {


                        Log.d("qpqpqpq", " present user::::"+news);
                        FirebaseUserEntity fib = postSnapshot.getValue(FirebaseUserEntity.class);
                        String ss = fib.getMyPendingRequest();
                        Log.d("qpqpqpq"," Pending requests are: "+ss);
                        ss = ss.replace(news.getuserID(),"");
                        ss = ss.replace(news.getuserID()+" | ","");
                        ss = ss.replace(" | "+news.getuserID(),"");
                        ss = ss.replace(news.getuserID(),"");
                        fib.setMyPendingRequest(ss);
                        Log.d("llalla-two",""+ss);

                        databaseReference.child("UserProfiles").child(news.getuserID()).setValue(fib);


                    }
                }

                //  editInterface.OnGetAllReqsMainSuccess(AllReqs);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // chatInterface.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
            }
        });



    }
}
