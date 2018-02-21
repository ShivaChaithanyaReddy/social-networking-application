package com.example.first.homework09;



import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
import com.squareup.picasso.Picasso;
*/

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chaithanya on 3/11/2017.
 */

public class RecViewAdapter extends  RecyclerView.Adapter<RecViewAdapter.RecViewHolder>{



    private List<FirebaseUserEntity> arrayList;
    private LayoutInflater layoutInflater;
    public Context context;
    static MediaPlayer mediaPlayer;
    public static int flag=0;
    public static String  OBJECT = "data";
    public static String BASEURL = "url";

public static String  FRNDUSERID = "FrndUser";
    private int fromwhere;

    private MainActivity mainActivity;

    public RecViewAdapter(List<FirebaseUserEntity> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context=context;
        this.mainActivity = mainActivity;
        this.layoutInflater = LayoutInflater.from(context);
        this.fromwhere = fromwhere;
    }

    @Override
    public RecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.rec_view_list, parent, false);
        return new RecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecViewHolder holder, final int position) {
        final FirebaseUserEntity users = arrayList.get(position);

        holder.text_view_user_alphabet.setText(users.getEmail().substring(0,1));
        holder.text_view_username.setText(users.getEmail());

        //add      compile 'com.squareup.picasso:picasso:2.5.2'          in gradle build
        //Picasso.with(context).load(news.getImage()).into(holder.forimagelogo);

        holder.For_rec_view_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FrndUserID = users.getuserID();
                Intent intent  = new Intent(context, SingleChatActivity.class);
                intent.putExtra(FRNDUSERID, FrndUserID);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class RecViewHolder extends RecyclerView.ViewHolder {

        private TextView text_view_user_alphabet;
        private TextView text_view_username;
        private LinearLayout For_rec_view_layout;


        public RecViewHolder(View itemView) {
            super(itemView);

            text_view_user_alphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            text_view_username = (TextView) itemView.findViewById(R.id.text_view_username);

            For_rec_view_layout = (LinearLayout) itemView.findViewById(R.id.For_rec_view_layout);
        }
    }
}