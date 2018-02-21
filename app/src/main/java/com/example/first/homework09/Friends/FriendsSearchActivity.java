package com.example.first.homework09.Friends;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.first.homework09.FirebaseUserEntity;
import com.example.first.homework09.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class FriendsSearchActivity extends AppCompatActivity implements FriendsInterface {


    RecyclerView recyclerView;
    private FriendsRecViewAdapter recViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    List<FirebaseUserEntity> allUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_search);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_friends);


        new ManageFriendsFirebase(this).getAllUsers();

    }

    @Override
    public void allUsersForFriends(List<FirebaseUserEntity> allUsers) {

        allUserData = new ArrayList<>();

        allUserData = allUsers;

        Log.e("TAG","AllUsers: Are"+allUsers);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        new ManageFriendsFirebase(this).getUser(user.getUid());

    }

    @Override
    public void CurrentUser(FirebaseUserEntity fbaseEntity) {

        if(allUserData != null) {

            //RecyclerView Code starts:
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView_friends);
            mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recViewAdapter = new FriendsRecViewAdapter(fbaseEntity, allUserData, this,this);
            recyclerView.setAdapter(recViewAdapter);


            //RecyclerView Code ends:

        }
    }

    @Override
    public void send_Frnd_request(FirebaseUserEntity news) {
        new ManageFriendsFirebase(this).sendFrndRequest(news);
        Log.e("kkk In main act","");
    }

    @Override
    public void OnSuccessSendFriendRequest(String s) {
        Toast.makeText(this, s,Toast.LENGTH_SHORT).show();
    }
}
