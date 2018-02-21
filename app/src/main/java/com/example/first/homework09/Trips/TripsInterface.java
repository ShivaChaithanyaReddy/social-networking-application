package com.example.first.homework09.Trips;

import com.example.first.homework09.Trip;

import java.util.List;

/**
 * Created by Chaithanya on 4/22/2017.
 */

public interface TripsInterface {
    void OnSuccessAllFrndsTrips(List<Trip> allTrips);

    void OnClickFrndTripJoinRequest(Trip news, int position);

    void OnSuccessJoinFrndTrip(String s, Trip news, int position);

    void OnSuccessGetJoinedTrips(List<Trip> allTrips);

    void OnSuccessGetMyTrips(List<Trip> allTrips);

    void OnGetAllFriendsSuccess(String friends);
}
