package com.example.first.homework09;

import java.util.List;

/**
 * Created by Chaithanya on 4/21/2017.
 */

public interface ChatInterface {


    void onGetMessagesSuccess(Chat chat);

    void onGetMessagesFailure(String s);

    void onSendMessageFailure(String s);

    void onSendMessageSuccess();

    void OnGetChatSuccess(List<Chat> allChats);
}
