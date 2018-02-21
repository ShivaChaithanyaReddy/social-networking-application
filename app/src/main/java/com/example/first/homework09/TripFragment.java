package com.example.first.homework09;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.first.homework09.FirebaseStorage.FirebaseStorageHelper;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static java.lang.String.valueOf;

public class TripFragment extends Fragment  implements TripInterface{

    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;


    private static final int SELECT_PICTURE = 29;
    private static final int REQUEST_READ_PERMISSION = 818 ;
    private static final String TAG = TripFragment.class.getName();
    public static final String TRIPID = "tripID";
    TextView name;
    TextView location;

    Trip trip;

    ImageView profilePhoto;

    private InsideChatAdapter mChatRecyclerAdapter;

    private RecyclerView mRecyclerViewChat;
    private EditText mETxtMessage;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_trip_fragment, container, false);
        getActivity().setTitle("My Trip");

        name = (TextView)view.findViewById(R.id.trip_title);
        location = (TextView)view.findViewById(R.id.trip_location);

        profilePhoto = (ImageView)view.findViewById(R.id.Trip_circleView);


      trip = (Trip) getActivity().getIntent().getSerializableExtra(CreateTripActivity.TRIPFORINTENT);

        mRecyclerViewChat = (RecyclerView) view.findViewById(R.id.recycler_view_chat_trip);
        mETxtMessage = (EditText) view.findViewById(R.id.edit_text_message_trip);


        final String sender = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final String senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        new CreateTripFirebaseManager(TripFragment.this).GetTripChats(trip);

        view.findViewById(R.id.sendMsgBtn_trip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mETxtMessage.getText().toString();
                String receiverUid = String.valueOf(trip.getTripID());

                Chat chat = new Chat(sender, senderUid, receiverUid, message,System.currentTimeMillis());
                new CreateTripFirebaseManager(TripFragment.this).SaveChatForTripInFirebase(chat) ;
            }
        });



        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, SELECT_PICTURE);
            }
        });



        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("user id has entered onActivityResult ");
        if (requestCode == Helper.SELECT_PICTURE) {
            Uri selectedImageUri = data.getData();
            String imagePath = getPath(selectedImageUri);

            FirebaseStorageHelper storageHelper = new FirebaseStorageHelper(getActivity());

            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
                return;
            }
            storageHelper.saveProfileImageToCloud(null, trip.getTripID(), selectedImageUri, profilePhoto, profilePhoto);
        }



        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);


                // Format the place's details and display them in the TextView.
              //  mPlaceDetailsText.setText(place.getName());

                String placename = ((String) place.getAddress()).replace(" ","+" );

 Log.i(TAG, "Place Selected: " + placename);


                TripPlaceClass Tripplace = new TripPlaceClass(placename,place.getLatLng().latitude,place.getLatLng().longitude);
                new CreateTripFirebaseManager(TripFragment.this).savePlaceInTheTripFirebase(Tripplace,trip);


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
    }


    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.trip_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.search_for_place_in_trip){
            openAutocompleteActivity();
            return true;
        }else if(id == R.id.all_places_in_trip)
        {
            Intent intent = new Intent(getContext(), All_places_in_trip_activity.class);
            intent.putExtra(TRIPID, trip.getTripID());
            startActivity(intent);
        }else if(id == R.id.map_places_in_trip) {
            Intent intent = new Intent(getContext(), map_for_places_in_trip.class);
            intent.putExtra(TRIPID, trip.getTripID());
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }


    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(getActivity());
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }





    @Override
    public void onCreateTripFailure(String s) {

    }

    @Override
    public void OnSuccessTripCreate(String s, Trip trip) {

    }

    @Override
    public void OnSuccessSendMessage(Chat chat) {
        mETxtMessage.setText("");
        Toast.makeText(getContext(), "Message sent", Toast.LENGTH_SHORT).show();

        if (mChatRecyclerAdapter == null) {
            mChatRecyclerAdapter = new InsideChatAdapter(new ArrayList<Chat>());
            mRecyclerViewChat.setAdapter(mChatRecyclerAdapter);
        }
        mChatRecyclerAdapter.add(chat);
        mRecyclerViewChat.smoothScrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);
    }

    @Override
    public void onSendMessageFailure(String s) {

    }

    @Override
    public void OnGetChatSuccess(List<Chat> allChats) {
        if(allChats != null) {
            mChatRecyclerAdapter = new InsideChatAdapter(allChats);
            mRecyclerViewChat.setAdapter(mChatRecyclerAdapter);
            mRecyclerViewChat.smoothScrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);
            mChatRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGetMessagesFailure(String s) {

    }

    @Override
    public void OnSuccessSavePlaceMessage(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
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
}
