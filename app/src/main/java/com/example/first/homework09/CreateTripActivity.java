package com.example.first.homework09;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class CreateTripActivity extends AppCompatActivity implements TripInterface {


    public static final int IMAGE_GALLARY_REQUEST = 20;
    EditText title;
    EditText location;
    Button image;
    Button create;

    Bitmap image_photo;

    public static String TRIPFORINTENT = "trip";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {

            if(requestCode == IMAGE_GALLARY_REQUEST) {
                Uri ImageUri = data.getData();

                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(ImageUri);
                    image_photo = BitmapFactory.decodeStream(inputStream);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);



        title = (EditText) findViewById(R.id.trip_title);
        location = (EditText) findViewById(R.id.trip_location);


        findViewById(R.id.add_image_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photopickerIntent = new Intent(Intent.ACTION_PICK);

                File PicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

                String PicDirPath = PicDir.getPath();
                Uri data = Uri.parse(PicDirPath);
                
                photopickerIntent.setDataAndType(data, "image/*");
                
                startActivityForResult(photopickerIntent, IMAGE_GALLARY_REQUEST);
            }
        });


        findViewById(R.id.create_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = title.getText().toString();
                String place = location.getText().toString();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                Trip trip = new Trip(name,place,System.currentTimeMillis(),user.getUid(),String.valueOf(UUID.randomUUID()),null );

                if(!name.isEmpty() && name != null
                        && !place.isEmpty() && place != null
                        ) {
                    new CreateTripFirebaseManager(CreateTripActivity.this).CreateTripinFirebase(trip);

                }else {
                    Toast.makeText(CreateTripActivity.this, "PLease fill all the details . . ", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void OnSuccessTripCreate(String message, Trip trip) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CreateTripActivity.this, Trip_Profile_Activity.class);
        intent.putExtra(TRIPFORINTENT,trip);
        startActivity(intent);
    }

    @Override
    public void OnSuccessSendMessage(Chat chat) {

    }

    @Override
    public void onSendMessageFailure(String s) {

    }

    //called for the first time when activity starts.....
    @Override
    public void OnGetChatSuccess(List<Chat> allChats) {

    }

    @Override
    public void onGetMessagesFailure(String s) {

    }

    @Override
    public void OnSuccessSavePlaceMessage(String s) {

    }

    @Override
    public void OnSuccessGetPlaceNameOfTrip(String place) {

    }

    @Override
    public void OnNoPlaceFound(String s) {

    }

    @Override
    public void OnSuccessDeletePlaceFroTrip(String s) {

    }

    @Override
    public void onCreateTripFailure(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
