package com.example.first.homework09.FirebaseStorage;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.first.homework09.FirebaseUserEntity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class FirebaseStorageHelper {

    private static final String TAG = FirebaseStorageHelper.class.getCanonicalName();

    private FirebaseStorage firebaseStorage;

    private StorageReference rootRef;

    private Context context;

    public FirebaseStorageHelper(Context context){
        this.context = context;
        init();
    }

    private void init(){
        this.firebaseStorage = FirebaseStorage.getInstance();
        rootRef = firebaseStorage.getReferenceFromUrl("gs://hw09-1749e.appspot.com");
    }

    public void saveProfileImageToCloud(final FirebaseUserEntity currentUserDetails, String userId, Uri selectedImageUri, final ImageView profilePhoto, final ImageView imageView) {

        StorageReference photoParentRef = rootRef.child(userId);
        StorageReference photoRef = photoParentRef.child(selectedImageUri.getLastPathSegment());
        UploadTask uploadTask = photoRef.putFile(selectedImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "OnFailure " + e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.d(TAG,"InSuccessUpload"+downloadUrl);

                new ImageFirebaseDatabaseManager().saveImagePathToDatabase(currentUserDetails,downloadUrl);

                Glide.with(context).load(downloadUrl).into(profilePhoto);

            }
        });

    }

    public void displayImageInProfieOnLoad(String userId, ImageView profilePhoto) {

        Log.d("In display snap:","");


        StorageReference photoParentRef = rootRef.child(userId);
        StorageReference photoRef = photoParentRef.getRoot();

        final long ONE_MEGABYTE = 1024 * 1024;
        photoParentRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }
}
