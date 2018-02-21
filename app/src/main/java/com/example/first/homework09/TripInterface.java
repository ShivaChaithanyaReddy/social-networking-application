package com.example.first.homework09;

import java.util.List;

/**
 * Created by Chaithanya on 4/21/2017.
 */

public interface TripInterface {
    void onCreateTripFailure(String s);

    void OnSuccessTripCreate(String s, Trip trip);

    void OnSuccessSendMessage(Chat chat);

    void onSendMessageFailure(String s);

    void OnGetChatSuccess(List<Chat> allChats);

    void onGetMessagesFailure(String s);

    void OnSuccessSavePlaceMessage(String s);

    void OnSuccessGetPlaceNameOfTrip(String place);

    void OnNoPlaceFound(String s);

    void OnSuccessDeletePlaceFroTrip(String s);
}
