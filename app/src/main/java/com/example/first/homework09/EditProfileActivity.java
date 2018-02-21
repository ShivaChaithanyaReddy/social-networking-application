package com.example.first.homework09;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class EditProfileActivity extends AppCompatActivity  implements  EditInterface{

    private static final String TAG = EditProfileActivity.class.getSimpleName();

    private EditText editProfileFName;

    private EditText editProfileCountry;

    private  EditText editProfileLName;

    private EditText editProfileGender;

    private EditText editProfileHobby;

    private ImageView editProfileBitmap;

    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setTitle("Edit Profile Informatsion");

        editProfileFName = (EditText)findViewById(R.id.profile_name);
        editProfileLName = (EditText) findViewById(R.id.profile_lname);
        editProfileCountry = (EditText)findViewById(R.id.profile_country);
        editProfileGender = (EditText)findViewById(R.id.profile_gender);
        editProfileHobby = (EditText)findViewById(R.id.profile_hobby);
     //   editProfileBitmap = (ImageView) findViewById(R.id.profile_bitmap);

        mAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    String userId = user.getUid();
                    new FirebaseApplication(EditProfileActivity.this).getUser(userId,0,null);



                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent firebaseUserIntent = new Intent(EditProfileActivity.this, LoginActivity.class);
                    startActivity(firebaseUserIntent);
                    finish();
                }
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        };

        Button saveEditButton = (Button)findViewById(R.id.save_edit_button);
        saveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String profileFName = editProfileFName.getText().toString();
                String profileLName = editProfileLName.getText().toString();
                String profileCountry = editProfileCountry.getText().toString();
                String profileGender = editProfileGender.getText().toString();
                String profileHobby = editProfileHobby.getText().toString();
//                Bitmap profileBitmap = ((BitmapDrawable)editProfileBitmap.getDrawable()).getBitmap();

                // update the user profile information in Firebase database.
                if(TextUtils.isEmpty(profileFName) || TextUtils.isEmpty(profileCountry) || TextUtils.isEmpty(profileGender)
                        || TextUtils.isEmpty(profileHobby) || TextUtils.isEmpty(profileLName)){
                    Helper.displayMessageToast(EditProfileActivity.this, "All fields must be filled");
                }

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d("In EditProfileAcv-click",""+user.getEmail());


                if (user == null) {
                    Intent firebaseUserIntent = new Intent(EditProfileActivity.this, LoginActivity.class);
                    startActivity(firebaseUserIntent);
                    finish();
                } else {




                    String userId = user.getProviderId();
                    String id = user.getUid();
                    String profileEmail = user.getEmail();




                        FirebaseUserEntity userEntity = new FirebaseUserEntity(profileHobby, profileGender, id, profileLName,profileEmail,profileFName, profileCountry,null,null,null,null);



                    new FirebaseApplication(EditProfileActivity.this).getUser(id,1,userEntity);


                                  }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EditProfileActivity.this,ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void getEditProfile(FirebaseUserEntity firebaseUserEntity) {
        Log.d("l------------------",""+firebaseUserEntity);

                   if(firebaseUserEntity != null) {
                        editProfileFName.setText(firebaseUserEntity.getFname());
                        editProfileLName.setText(firebaseUserEntity.getLname());
                        editProfileCountry.setText(firebaseUserEntity.getCountry());
                        editProfileGender.setText(firebaseUserEntity.getGender());
                        editProfileHobby.setText(firebaseUserEntity.getHobby());
                    }
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
    public void getInitialProfile(FirebaseUserEntity fbaseEntity, FirebaseUserEntity editedentity) {



        editedentity.setImage(fbaseEntity.getImage());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();
        String profileEmail = user.getEmail();
        FirebaseDatabaseHelper firebaseDatabaseHelper = new FirebaseDatabaseHelper();
        firebaseDatabaseHelper.createUserInFirebaseDatabase(id, editedentity);
        Toast.makeText(EditProfileActivity.this,"Profile Saved Successfully. . . ",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClickDisapprove(FirebaseUserEntity news) {

    }
}
