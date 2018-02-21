package com.example.first.homework09;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.first.homework09.FirebaseStorage.FirebaseStorageHelper;
import com.example.first.homework09.Friends.FriendsSearchActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ProfileFragment extends Fragment implements EditInterface {

    private static final String TAG = ProfileFragment.class.getSimpleName();

    private ImageView profilePhoto;

    private TextView profileName;

    private TextView country;

    private TextView userStatus;

    Button create_trip_btn_From_profile;

    private LinearLayoutManager linearLayoutManager;
    public static final int SELECT_PICTURE = 2000;

    private String id;

    private static final int REQUEST_READ_PERMISSION = 120;

    private StorageReference rootRef;


    FirebaseUserEntity currentUserDetails;

    View view1;

    ImageView image;
    TextView name_profile_nav;
    TextView textView;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       view1 = inflater.inflate(R.layout.fragment_profile, container, false);

        getActivity().setTitle("My Profile");

        profileName = (TextView)view1.findViewById(R.id.profile_name);
        country = (TextView)view1.findViewById(R.id.country);

        profilePhoto = (ImageView)view1.findViewById(R.id.circleView);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        Log.d("demo : user logged in: ",""+userId);


        rootRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://hw09-1749e.appspot.com");

        StorageReference pathReference = rootRef.child(userId);


        Log.d("pathpathpath:",""+pathReference);


        ((FirebaseApplication)getActivity().getApplication()).getFirebaseAuth();
        id = ((FirebaseApplication)getActivity().getApplication()).getFirebaseUserAuthenticateId();


/*

        FirebaseStorageHelper storageHelper = new FirebaseStorageHelper(getActivity());
        storageHelper.displayImageInProfieOnLoad(id, profilePhoto);
*/


        new FirebaseApplication(this).getUser(userId,0,null);


        view1.findViewById(R.id.my_trip_btn_From_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), my_trips_activity.class);
                startActivity(intent);
            }
        });

        view1.findViewById(R.id.create_trip_btn_From_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateTripActivity.class);
                startActivity(intent);
            }
        });


        view1.findViewById(R.id.Joined_trip_btn_From_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchFriends = new Intent(getContext(), Joined_trips_activity.class);
                startActivity(searchFriends);

            }
        });


        view1.findViewById(R.id.frnd_trip_btn_From_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Friends_trips_activity.class);
                startActivity(intent);
            }
        });

/*
        Bitmap bm = BitmapFactory.decodeResource(getResources(),
                R.drawable.profile);
        Bitmap resized = Bitmap.createScaledBitmap(bm, 100, 100, true);
        Bitmap conv_bm = getRoundedRectBitmap(resized, 100);
*/

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, SELECT_PICTURE);
            }
        });

        linearLayoutManager = new LinearLayoutManager(getActivity());





        return view1;
    }




    public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            int color = 0xff424242;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, 200, 200);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(50, 50, 50, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

        } catch (NullPointerException e) {
        } catch (OutOfMemoryError o) {
        }
        return result;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_profile, menu);
    }
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_edit_profile){
            getActivity().finish();
            Intent editProfileIntent = new Intent(getContext(), EditProfileActivity.class);
            getContext().startActivity(editProfileIntent);
            return true;
        }else if(id == R.id.search_for_friends) {
            Intent searchFriends = new Intent(getContext(), FriendsSearchActivity.class);
            getContext().startActivity(searchFriends);
            return  true;
        }else if(id == R.id.notify_frnds) {
            Intent searchFriends = new Intent(getContext(), View_Frnd_req_activity.class);
            getContext().startActivity(searchFriends);
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("user id has entered onActivityResult ");
        if (requestCode == Helper.SELECT_PICTURE) {
            Uri selectedImageUri = data.getData();
            String imagePath = getPath(selectedImageUri);

            FirebaseStorageHelper storageHelper = new FirebaseStorageHelper(getActivity());

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
                return;
            }
            storageHelper.saveProfileImageToCloud(currentUserDetails , id, selectedImageUri, profilePhoto, image);
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(getActivity(), "Sorry!!!, you can't use this app without granting this permission", Toast.LENGTH_LONG).show();
            }
        }
    }



    @Override
    public void getEditProfile(FirebaseUserEntity firebaseUserEntity) {

        Log.d("In edit: Firebase enti",""+firebaseUserEntity);

        currentUserDetails = firebaseUserEntity;

        profileName.setText(firebaseUserEntity.getFname()+ " "+firebaseUserEntity.getLname());
        country.setText(firebaseUserEntity.getCountry());



if(firebaseUserEntity.getImage() == null) {
Toast.makeText(getContext(), "PLease tap on image to set your Display Picture . . . ",Toast.LENGTH_LONG).show();
}
else {
    Glide.with(getContext())
            .load(firebaseUserEntity.getImage())
            .into(profilePhoto);
    }
    }

    @Override
    public void allUsersForChatting(List<FirebaseUserEntity> allUsers) {

    }

    @Override
    public void OnSuccessGetAllFriendRequestsString(String allPending) {
    }

    @Override
    public void OnGetAllReqsMainSuccess(List<FirebaseUserEntity> allReqs) {


    }

    @Override
    public void onClickApprove(FirebaseUserEntity news, int position) {

    }

    @Override
    public void getInitialProfile(FirebaseUserEntity fbaseEntity, FirebaseUserEntity EditedEntity) {

    }

    @Override
    public void onClickDisapprove(FirebaseUserEntity news) {

    }

}
