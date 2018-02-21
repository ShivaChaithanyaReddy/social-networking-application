package com.example.first.homework09.Trips;

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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.first.homework09.EditInterface;
import com.example.first.homework09.R;
import com.example.first.homework09.Trip;
import com.example.first.homework09.Trip_Profile_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;

import com.example.first.homework09.FirebaseUserEntity;

import static com.example.first.homework09.CreateTripActivity.TRIPFORINTENT;

/**
 * Created by Chaithanya on 3/11/2017.
 */

public class Rec_View_for_my_Trips extends  RecyclerView.Adapter<Rec_View_for_my_Trips.RecViewHolder>{


    private List<Trip> arrayList;
    private LayoutInflater layoutInflater;
    public Context context;
    public static String  OBJECT = "data";
    public static String BASEURL = "url";

    TripsInterface friendsInterface;

    FirebaseUserEntity currentUser;


    public Rec_View_for_my_Trips(List<Trip> arrayList, Context context,TripsInterface friendsInterface) {
        this.arrayList = arrayList;
        this.context=context;
        this.layoutInflater = LayoutInflater.from(context);
        this.friendsInterface = friendsInterface;
    }

    @Override
    public Rec_View_for_my_Trips.RecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.rec_view_for_frnds_trips_list, parent, false);
        return new Rec_View_for_my_Trips.RecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Rec_View_for_my_Trips.RecViewHolder holder, final int position) {
        final Trip news = arrayList.get(position);

        Log.e("qpqpqpq",""+news);

        holder.trip_name.setText(news.getName());

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
                    holder.send_request.setVisibilityfire(View.GONE);
                }
            }
        }
        */


        holder.recycler_each_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchFriends = new Intent(context, Trip_Profile_Activity.class);
                searchFriends.putExtra(TRIPFORINTENT, news);
                context.startActivity(searchFriends);
            }
        });


        holder.join_trip.setEnabled(false);
        holder.join_trip.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class RecViewHolder extends RecyclerView.ViewHolder {

        private ImageView trip_logo;
        private TextView trip_name;
        private Button join_trip;

        FrameLayout recycler_each_item_layout ;

        public RecViewHolder(View itemView) {
            super(itemView);

            trip_logo  =(ImageView) itemView.findViewById(R.id.Frnd_trip_image);
            trip_name = (TextView) itemView.findViewById(R.id.frnd_trip_name);
            join_trip = (Button) itemView.findViewById(R.id.join_frnd_trip_btn);
            recycler_each_item_layout = (FrameLayout) itemView.findViewById(R.id.recycler_each_item_layout);
        }
    }
}
