package com.genialabs.dermia;


import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.IOException;

public class PhotoFragment extends Fragment {

    private PhotoViewModel mViewModel;
    private View rootView;
    private Uri imageUri;
    private static final int CAM_REQUEST = 1313;
    private Bitmap bitmap;
    private ImageButton btn_photo,btn_charts;
    private ContentValues cv;
    private ConstraintLayout l_photo,l_humnabody;
    private Button b_head,b_chest,b_hipzone,b_r_shoulder,b_l_shoulder,b_r_arm,b_l_arm,b_r_hand,b_l_hand,b_r_leg,b_l_leg,b_r_foot,b_l_foot;
    private String bodypart,l_humanbody;

    public static PhotoFragment newInstance() {

        return new PhotoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.photo_fragment, container, false);

        //Gradient Change
        ConstraintLayout constraintLayout =  rootView.findViewById(R.id.root_layout_photo);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();


        btn_photo = rootView.findViewById(R.id.btn_photo);
        btn_charts = rootView.findViewById(R.id.btn_charts);
        l_humnabody = rootView.findViewById(R.id.l_human_body);
        l_photo = rootView.findViewById(R.id.l_photo);
        //Por si saco otra foto desde prediction
        try{
            l_humanbody = getArguments().getString("humanbody");
        }catch (NullPointerException e){

        }
        if(l_humanbody=="yes"){
            l_photo.setVisibility(View.INVISIBLE);
            l_humnabody.setVisibility(View.VISIBLE);
        }

        b_head = rootView.findViewById(R.id.b_head);
        b_chest = rootView.findViewById(R.id.b_chest);
        b_hipzone = rootView.findViewById(R.id.b_zonex);
        b_r_shoulder = rootView.findViewById(R.id.b_r_shoulder);
        b_l_shoulder = rootView.findViewById(R.id.b_l_shoulder);
        b_r_arm = rootView.findViewById(R.id.b_r_arm);
        b_l_arm = rootView.findViewById(R.id.b_l_arm);
        b_r_hand = rootView.findViewById(R.id.b_r_hand);
        b_l_hand = rootView.findViewById(R.id.b_left_hand);
        b_r_leg = rootView.findViewById(R.id.b_r_leg);
        b_l_leg = rootView.findViewById(R.id.b_l_leg);
        b_r_foot = rootView.findViewById(R.id.b_r_foot);
        b_l_foot = rootView.findViewById(R.id.b_l_foot);

        b_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodypart=getString(R.string.head);
                startCamera();
            }
        });
        b_chest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodypart=getString(R.string.chest);
                startCamera();
            }
        });
        b_hipzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodypart=getString(R.string.hip);
                startCamera();
            }
        });
        b_r_shoulder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodypart=getString(R.string.r_shoulder);
                startCamera();
            }
        });
        b_l_shoulder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodypart=getString(R.string.l_shoulder);
                startCamera();
            }
        });
        b_r_arm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodypart=getString(R.string.r_arm);
                startCamera();
            }
        });
        b_l_arm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodypart=getString(R.string.l_arm);
                startCamera();
            }
        });
        b_r_hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodypart=getString(R.string.r_hand);
                startCamera();
            }
        });
        b_l_hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodypart=getString(R.string.l_hand);
                startCamera();
            }
        });
        b_r_leg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodypart=getString(R.string.r_leg);
                startCamera();
            }
        });
        b_l_leg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodypart=getString(R.string.l_leg);
                startCamera();
            }
        });
        b_r_foot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodypart=getString(R.string.r_foot);
                startCamera();
            }
        });
        b_l_foot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodypart=getString(R.string.l_foot);
                startCamera();
            }
        });

        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            l_photo.setVisibility(View.INVISIBLE);
            l_humnabody.setVisibility(View.VISIBLE);
            }
        });
        btn_charts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putBoolean("movetoalbum", true);
                TabFragment fragment = new TabFragment();
                fragment.setArguments(args);
                getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(R.id.fragment_container,fragment).addToBackStack(null).commit();

            }
        });

        return rootView;
    }
    public void startCamera(){
        if (have_permissions()) {
            cv = new ContentValues();
            String my_username = "PEPE";
            cv.put(MediaStore.Images.Media.TITLE, "mela_" + my_username + "_" + System.currentTimeMillis());
            cv.put(MediaStore.Images.Media.DESCRIPTION, "Skin Photo");
            cv.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            imageUri = getActivity().getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, CAM_REQUEST);


        }

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
        // TODO: Use the ViewModel
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAM_REQUEST) {
                //SEND TO PREDICT FRAGMENT
                // Adding captured image in bitmap.
                //bitmap = (Bitmap) data.getExtras().get("data");
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

                   /* String downloadsDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                    String filename = String.format("%d_%s", Calendar.getInstance().getTimeInMillis(), imageUri.getLastPathSegment());

                    File saveFile = new File(downloadsDirectoryPath, filename);

                    FileInputStream inStream = new FileInputStream(new File(imageUri.getPath()));
                    FileOutputStream outStream = new FileOutputStream(saveFile);
                    FileChannel inChannel = inStream.getChannel();
                    FileChannel outChannel = outStream.getChannel();
                    inChannel.transferTo(0, inChannel.size(), outChannel);
                    inStream.close();
                    outStream.close();*/

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bundle args = new Bundle();
                args.putParcelable("uri", imageUri);
                args.putString("bodypart", bodypart);
                args.putBoolean("newAlbum", true);
                PredictFragment fragment = new PredictFragment();
                fragment.setArguments(args);
                getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(R.id.fragment_container,fragment).addToBackStack(null).commit();

            }
        }
    }


    public boolean have_permissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("checkPErm", "Permission is granted2");
                return true;
            } else {

                Log.v("checkPErm", "Permission is revoked2");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("CAMERAPERMISION", "Perm GRANTED");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.d("CAMERAPERMISION", "Perm DENIED");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
