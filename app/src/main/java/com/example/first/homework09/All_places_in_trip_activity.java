package com.example.first.homework09;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.WindowDecorActionBar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.first.homework09.Trips.TripsInterface;

import java.util.ArrayList;
import java.util.List;

public class All_places_in_trip_activity extends AppCompatActivity implements TripInterface {

    ListView listView;
    ArrayAdapter<String> adapter;
    String trip_id;
    List<String> allPlaces;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_places_in_trip_activity);

        setTitle("AllPlacesIn Trip");
        allPlaces = new ArrayList<>();

        trip_id = getIntent().getStringExtra(TripFragment.TRIPID);

        new CreateTripFirebaseManager(All_places_in_trip_activity.this).getAllTripPlaces(trip_id);

         }


    @Override
    public void onCreateTripFailure(String s) {

    }

    @Override
    public void OnSuccessTripCreate(String s, Trip trip) {

    }

    @Override
    public void OnSuccessSendMessage(Chat chat) {

    }

    @Override
    public void onSendMessageFailure(String s) {

    }

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


        Log.d("jajaja",""+place);

        if(allPlaces == null || allPlaces.isEmpty()) {
            allPlaces.add(place);
            listView  = (ListView) findViewById(R.id.list_view_for_places_in_trip);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,allPlaces );
            adapter.setNotifyOnChange(true);
            listView.setAdapter(adapter);
        }
        else {
            allPlaces.add(place);
        }

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                new CreateTripFirebaseManager(All_places_in_trip_activity.this).RemovePlaceFromTrip(trip_id,allPlaces.get(position));
                allPlaces.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

    }

    @Override
    public void OnNoPlaceFound(String s) {
        Toast.makeText(this,s, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void OnSuccessDeletePlaceFroTrip(String s) {
        Toast.makeText(this,s, Toast.LENGTH_SHORT).show();

    }
}
