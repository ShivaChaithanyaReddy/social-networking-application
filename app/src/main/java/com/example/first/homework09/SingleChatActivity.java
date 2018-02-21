package com.example.first.homework09;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class SingleChatActivity extends AppCompatActivity implements ChatInterface {

    private RecyclerView mRecyclerViewChat;
    private EditText mETxtMessage;

    private ProgressDialog mProgressDialog;

    private InsideChatAdapter mChatRecyclerAdapter;

    private ChatInterface chatInterface;


    public SingleChatActivity(){}
    public SingleChatActivity(ChatInterface chatInterface) {
        this.chatInterface = chatInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chat);

        mRecyclerViewChat = (RecyclerView) findViewById(R.id.recycler_view_chat);
        mETxtMessage = (EditText) findViewById(R.id.edit_text_message);


        final String ReciverID = getIntent().getStringExtra(RecViewAdapter.FRNDUSERID);
        final String sender = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final String senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Log.e("AllChats-SingleCht","Sender: "+ senderUid+" Receiver: "+ReciverID);

        new FirebaseApplication(SingleChatActivity.this).getAllMsgsOfChat(ReciverID, senderUid);

        findViewById(R.id.sendMsgBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mETxtMessage.getText().toString();
                String receiverUid = ReciverID;


                Chat chat = new Chat(sender, senderUid, receiverUid, message,System.currentTimeMillis());

                new FirebaseApplication(SingleChatActivity.this).SaveMessageinFirebase(chat);

            }
        });

    }


    @Override
    public void onGetMessagesSuccess(Chat chat) {
        if (mChatRecyclerAdapter == null) {
            mChatRecyclerAdapter = new InsideChatAdapter(new ArrayList<Chat>());
            mRecyclerViewChat.setAdapter(mChatRecyclerAdapter);
        }
        mChatRecyclerAdapter.add(chat);
        mRecyclerViewChat.smoothScrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);
    }

    @Override
    public void onGetMessagesFailure(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSendMessageFailure(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSendMessageSuccess() {
        mETxtMessage.setText("");
        Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
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
}
