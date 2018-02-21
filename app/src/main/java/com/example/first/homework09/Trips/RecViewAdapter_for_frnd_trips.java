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
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import com.example.first.homework09.EditInterface;
        import com.example.first.homework09.R;
        import com.example.first.homework09.Trip;
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

public class RecViewAdapter_for_frnd_trips extends  RecyclerView.Adapter<RecViewAdapter_for_frnd_trips.RecViewHolder>{


    private List<Trip> arrayList;
    private LayoutInflater layoutInflater;
    public Context context;
    public static String  OBJECT = "data";
    public static String BASEURL = "url";

    TripsInterface friendsInterface;

    FirebaseUserEntity currentUser;


    public RecViewAdapter_for_frnd_trips(List<Trip> arrayList, Context context,TripsInterface friendsInterface) {
        this.arrayList = arrayList;
        this.context=context;
        this.layoutInflater = LayoutInflater.from(context);
        this.friendsInterface = friendsInterface;
    }

    @Override
    public RecViewAdapter_for_frnd_trips.RecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.rec_view_for_frnds_trips_list, parent, false);
        return new RecViewAdapter_for_frnd_trips.RecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecViewAdapter_for_frnd_trips.RecViewHolder holder, final int position) {
        final Trip news = arrayList.get(position);

        Log.e("qpqpqpq",""+news);

        holder.trip_name.setText(news.getName());



        holder.join_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    friendsInterface.OnClickFrndTripJoinRequest(news, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class RecViewHolder extends RecyclerView.ViewHolder {

        private ImageView trip_logo;
        private TextView trip_name;
        private Button join_trip;

        public RecViewHolder(View itemView) {
            super(itemView);

            trip_logo  =(ImageView) itemView.findViewById(R.id.Frnd_trip_image);
            trip_name = (TextView) itemView.findViewById(R.id.frnd_trip_name);
            join_trip = (Button) itemView.findViewById(R.id.join_frnd_trip_btn);

        }
    }
}
