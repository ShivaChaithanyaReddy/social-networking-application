package com.example.first.homework09;

import java.util.List;

/**
 * Created by Chaithanya on 4/20/2017.
 */

public interface EditInterface {


    void getEditProfile(FirebaseUserEntity firebaseUserEntity);

    void allUsersForChatting(List<FirebaseUserEntity> allUsers);

    void OnSuccessGetAllFriendRequestsString(String allPending);

    void OnGetAllReqsMainSuccess(List<FirebaseUserEntity> allReqs);

    void onClickApprove(FirebaseUserEntity news, int position);

    void getInitialProfile(FirebaseUserEntity fbaseEntity, FirebaseUserEntity EditedEntity);

    void onClickDisapprove(FirebaseUserEntity news);
}
