package com.example.first.homework09.Friends;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.first.homework09.EditInterface;
import com.example.first.homework09.FirebaseUserEntity;
import com.example.first.homework09.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;

import com.example.first.homework09.FirebaseUserEntity;

/**
 * Created by Chaithanya on 3/11/2017.
 */

public class Friend_req_rec_view_adapter extends  RecyclerView.Adapter<Friend_req_rec_view_adapter.RecViewHolder>{


    private List<FirebaseUserEntity> arrayList;
    private LayoutInflater layoutInflater;
    public Context context;
    public static String  OBJECT = "data";
    public static String BASEURL = "url";

    EditInterface friendsInterface;

    FirebaseUserEntity currentUser;


    public Friend_req_rec_view_adapter(List<FirebaseUserEntity> arrayList, Context context,EditInterface friendsInterface) {
        this.arrayList = arrayList;
        this.context=context;
        this.layoutInflater = LayoutInflater.from(context);
        this.friendsInterface = friendsInterface;
    }

    @Override
    public RecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.rec_view_for_frnd_req_recieved, parent, false);
        return new RecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecViewHolder holder, final int position) {
        final FirebaseUserEntity news = arrayList.get(position);

        Log.e("qpqpqpq",""+news);

        holder.frnd_email_req.setText(news.getEmail());

      //  holder.frnd_email_req.setText(news.getEmail());

        //add      compile 'com.squareup.picasso:picasso:2.5.2'          in gradle build
        //    Picasso.with(context).load(news.getImage()).into(holder.forimagelogo);

   /*     if(fromwhere == 0) {
            holder.forcloseimage.setImageResource(R.drawable.close_red);
        }else if(fromwhere == 1) {
            holder.forcloseimage.setImageResource(R.drawable.tick_green);
        }*/
        /*if(currentUser.getMyPendingRequest() != null ) {
            if(currentUser.getMyPendingRequest().contains(news.getuserID())) {
                holder.send_request.setText("Request Pending");
                holder.send_request.setEnabled(false);
            }else if(currentUser.getMyFriends() != null) {
                if(currentUser.getMyFriends().contains(news.getuserID())) {
                    holder.send_request.setVisibility(View.GONE);
                }
            }


        }

        */

        holder.approve_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("qpqpqpq seg inadapter:",""+news);
                friendsInterface.onClickApprove(news, position);
            }
        });



        holder.dispprove_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendsInterface.onClickDisapprove(news);
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class RecViewHolder extends RecyclerView.ViewHolder {

        private ImageView frnd_image;
        private TextView frnd_email_req;
        private Button approve_request;
        private Button dispprove_request;

        public RecViewHolder(View itemView) {
            super(itemView);

            frnd_image  =(ImageView) itemView.findViewById(R.id.frnd_pic_from_req);
            frnd_email_req = (TextView) itemView.findViewById(R.id.frnd_id_from_req);
            approve_request = (Button) itemView.findViewById(R.id.approve_request_from_req);
            dispprove_request = (Button) itemView.findViewById(R.id.disapprove_request_from_req);

        }
    }
}