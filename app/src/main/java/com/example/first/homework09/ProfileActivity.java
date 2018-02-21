package com.example.first.homework09;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;


public class ProfileActivity extends AppCompatActivity implements EditInterface{

    private FragmentManager fragmentManager;

    private Fragment fragment = null;

    ImageView image;

    TextView name_profile_nav;
TextView textView;


    //For login
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //For Login


    private StorageReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //for navigation header:
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);

        image = (ImageView) header.findViewById(R.id.circleView);
        name_profile_nav = (TextView) header.findViewById(R.id.name_profile_nav);
        textView = (TextView) header.findViewById(R.id.textView);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        rootRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://hw09-1749e.appspot.com");
        new FirebaseApplication(this).getUser(userId,0,null);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new ProfileFragment();
        fragmentTransaction.replace(R.id.main_container_wrapper, fragment);
        fragmentTransaction.commit();

        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);
        disableNavigationViewScrollbars(navigationView1);
        navigationView1.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_profile) {
                    fragment = new ProfileFragment();
                } else if (id == R.id.nav_chat) {
                    goToChat();
                }  else if (id == R.id.nav_logout) {
                    mAuth = ((FirebaseApplication)getApplication()).getFirebaseAuth();
                    mAuth.signOut();
                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    Toast.makeText(ProfileActivity.this,"User SIngned Out! ! !",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container_wrapper, fragment);
                transaction.commit();

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                assert drawer != null;
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }


    private void goToChat() {
        Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    @Override
    public void getEditProfile(FirebaseUserEntity firebaseUserEntity) {


        Log.d("InEdited Activity:","");
        name_profile_nav.setText(firebaseUserEntity.getFname()+" "+firebaseUserEntity.getLname());
        textView.setText(firebaseUserEntity.getCountry());

        Glide.with(this)
                .load(firebaseUserEntity.getImage())
                .into(image);
    }

    @Override
    public void allUsersForChatting(List<FirebaseUserEntity> allUsers) {

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
