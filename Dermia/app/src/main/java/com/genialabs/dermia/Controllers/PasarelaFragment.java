package com.genialabs.dermia.Controllers;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.genialabs.dermia.PredictFragment;
import com.genialabs.dermia.R;

import java.io.IOException;


public class PasarelaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ContentValues cv;
    private Uri imageUri;
    private Bitmap bitmap;
    private String albumid;
    private String bodypart;
    private static final int CAM_NEW_IMAGE = 1202;
    private static final int CAM_REQUEST = 1313;
    private View rootView;

    public PasarelaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PasarelaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PasarelaFragment newInstance(String param1, String param2) {
        PasarelaFragment fragment = new PasarelaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_pasarela, container, false);;
        albumid = getArguments().getString("albumid");
        bodypart = getArguments().getString("bodypart");
        cv = new ContentValues();
        String my_username = "DermiaNew";
        cv.put(MediaStore.Images.Media.TITLE, "mela_" + my_username + "_" + System.currentTimeMillis());
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Skin Photo");
        cv.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        imageUri = getActivity().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAM_REQUEST);

        return rootView;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAM_REQUEST) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bundle args = new Bundle();
                args.putParcelable("uri", imageUri);
                args.putString("bodypart", bodypart);
                args.putString("albumid", albumid);
                args.putBoolean("newAlbum", false);
                PredictFragment fragment = new PredictFragment();
                fragment.setArguments(args);
                getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(R.id.fragment_container,fragment).addToBackStack(null).commit();

            }

        }
    }

}
