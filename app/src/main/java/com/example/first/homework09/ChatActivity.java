package com.example.first.homework09;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ChatActivity extends AppCompatActivity implements EditInterface {


    //for Recview
    private RecyclerView recyclerView;
    private RecViewAdapter recViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    // for Recview




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent firebaseUserIntent = new Intent(ChatActivity.this, LoginActivity.class);
            startActivity(firebaseUserIntent);
            finish();
        }else  {

            new FirebaseApplication(this).getAllUsersForChat(user.getUid());

        }

/*

*/

    }

    @Override
    public void getEditProfile(FirebaseUserEntity firebaseUserEntity) {

    }

    @Override
    public void allUsersForChatting(List<FirebaseUserEntity> allUsers) {



        //RecyclerView Code starts:
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recViewAdapter = new RecViewAdapter(allUsers, this);
        recyclerView.setAdapter(recViewAdapter);
        //RecyclerView Code ends:


    }

    @Override
    public void OnSuccessGetAllFriendRequestsString(String allPending) {

    }

    @Override
    public void OnGetAllReqsMainSuccess(List<FirebaseUserEntity> allReqs) {

    }

    @Override
    public void onClickApprove(FirebaseUserEntity news, int position) {

    }

    @Override
    public void getInitialProfile(FirebaseUserEntity fbaseEntity, FirebaseUserEntity EditedEntity) {

    }

    @Override
    public void onClickDisapprove(FirebaseUserEntity news) {

    }
}
