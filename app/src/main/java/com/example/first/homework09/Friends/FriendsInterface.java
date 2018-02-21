package com.example.first.homework09.Friends;

import com.example.first.homework09.FirebaseUserEntity;

import java.util.List;

/**
 * Created by Chaithanya on 4/21/2017.
 */

public interface FriendsInterface {
    void allUsersForFriends(List<FirebaseUserEntity> allUsers);

    void CurrentUser(FirebaseUserEntity fbaseEntity);

    void send_Frnd_request(FirebaseUserEntity news);

    void OnSuccessSendFriendRequest(String s);
}
