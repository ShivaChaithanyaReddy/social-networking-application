package com.example.first.homework09;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.first.homework09.Friends.Friend_req_rec_view_adapter;

import java.util.ArrayList;
import java.util.List;

public class View_Frnd_req_activity extends AppCompatActivity implements EditInterface {



    //for Recview
    private RecyclerView recyclerView;
    private Friend_req_rec_view_adapter recViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    // for Recview


    List<FirebaseUserEntity> alll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__frnd_req_activity);

        alll = new ArrayList<>();

        new FirebaseApplication(this).getAllRecievedFrndReq();

    }

    @Override
    public void getEditProfile(FirebaseUserEntity firebaseUserEntity) {

    }

    @Override
    public void allUsersForChatting(List<FirebaseUserEntity> allUsers) {

    }

    @Override
    public void OnSuccessGetAllFriendRequestsString(String allPending) {
        Log.d("demo : ","All reqs "+allPending);
        new FirebaseApplication(this).getAllRecievedFrndReqMain(allPending);

    }

    @Override
    public void OnGetAllReqsMainSuccess(List<FirebaseUserEntity> allReqs) {

alll = allReqs;
        //RecyclerView Code starts:
        recyclerView = (RecyclerView) findViewById(R.id.rec_view_frn_requests_recieved);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recViewAdapter = new Friend_req_rec_view_adapter(allReqs, this,this);
        recyclerView.setAdapter(recViewAdapter);
        //RecyclerView Code ends:

    }

    @Override
    public void onClickApprove(FirebaseUserEntity news, int position) {
        alll.remove(position);
        recViewAdapter.notifyDataSetChanged();
        new FirebaseApplication(this).ApproveFrndsRequest(news);
    }

    @Override
    public void getInitialProfile(FirebaseUserEntity fbaseEntity, FirebaseUserEntity EditedEntity) {

    }

    @Override
    public void onClickDisapprove(FirebaseUserEntity news) {
        new FirebaseApplication(this).DisapproveFrndRequest(news);
    }
}
