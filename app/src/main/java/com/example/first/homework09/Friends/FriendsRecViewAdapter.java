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

import com.example.first.homework09.FirebaseUserEntity;
import com.example.first.homework09.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chaithanya on 3/11/2017.
 */

public class FriendsRecViewAdapter extends  RecyclerView.Adapter<FriendsRecViewAdapter.RecViewHolder>{



    private List<FirebaseUserEntity> arrayList;
    private LayoutInflater layoutInflater;
    public Context context;
    public static String  OBJECT = "data";
    public static String BASEURL = "url";

    FriendsInterface friendsInterface;

    FirebaseUserEntity currentUser;


    public FriendsRecViewAdapter(FirebaseUserEntity firebaseUserEntity, List<FirebaseUserEntity> arrayList, Context context,FriendsInterface friendsInterface) {
        this.arrayList = arrayList;
        this.context=context;
        this.layoutInflater = LayoutInflater.from(context);
        this.currentUser = firebaseUserEntity;
        this.friendsInterface = friendsInterface;
    }

    @Override
    public RecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.friends_rec_view, parent, false);
        return new RecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecViewHolder holder, final int position) {
        final FirebaseUserEntity news = arrayList.get(position);

        holder.frnd_email.setText(news.getEmail());

        //add      compile 'com.squareup.picasso:picasso:2.5.2'          in gradle build
    //    Picasso.with(context).load(news.getImage()).into(holder.forimagelogo);

   /*     if(fromwhere == 0) {
            holder.forcloseimage.setImageResource(R.drawable.close_red);
        }else if(fromwhere == 1) {
            holder.forcloseimage.setImageResource(R.drawable.tick_green);
        }*/
        if(currentUser.getMyPendingRequest() != null ) {
            if(currentUser.getMyPendingRequest().contains(news.getuserID())) {
                holder.send_request.setText("Request Pending");
                holder.send_request.setEnabled(false);
            }else if(currentUser.getMyFriends() != null) {
                if(currentUser.getMyFriends().contains(news.getuserID())) {
                    holder.send_request.setVisibility(View.GONE);
                }
            }


        }





        holder.send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("kkk I am clicked: ",""+ news.toString());
                holder.send_request.setEnabled(false);
                holder.send_request.setText("Request Pending");
               friendsInterface.send_Frnd_request(news);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class RecViewHolder extends RecyclerView.ViewHolder {

        private ImageView frnd_image;
        private TextView frnd_email;
        private  Button send_request;

        public RecViewHolder(View itemView) {
            super(itemView);

            frnd_image  =(ImageView) itemView.findViewById(R.id.frnd_pic);
            frnd_email = (TextView) itemView.findViewById(R.id.frnd_id);
            send_request = (Button) itemView.findViewById(R.id.send_request);

        }
    }
}