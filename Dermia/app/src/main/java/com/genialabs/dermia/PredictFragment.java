package com.genialabs.dermia;


import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
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
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.genialabs.dermia.Controllers.ApiClient;
import com.genialabs.dermia.Controllers.ApiInterface;
import com.genialabs.dermia.Controllers.DbHandler;
import com.genialabs.dermia.Models.Prediction;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PredictFragment extends Fragment {

    private PredictViewModel viewModel;
    //Layout objects
    private ImageView iv_image;
    private Button btn_predict,btn_photo,btn_chart;
    private TextView tv_res,tv_pred,tv_total,tv_resume,tv_policy;
    private static final int CAM_REQUEST = 1313;
    private static final int CAM_NEW_IMAGE = 1202;
    private Uri imageUri;
    private ContentValues cv;
    private Bitmap bitmap;
    private View rootView;

    private LinearLayout l_layout;
    private AlphaAnimation anim;
    private int fails= 0;
    private ApiInterface uploadAPIs;
    private Retrofit retrofit=null;
    private DbHandler dbPreds;
    private ProgressBar myProgressBar;
    private String bodyPart;
    private long idalbum;
    private Boolean newAlbum;


    public static PredictFragment newInstance() {
        return new PredictFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.predict_fragment, container, false);
        //FIt PHOTO
        iv_image = rootView.findViewById(R.id.iv_image);
        imageUri = getArguments().getParcelable("uri");
        bodyPart = getArguments().getString("bodypart");
        newAlbum = getArguments().getBoolean("newAlbum");
        if(!newAlbum)
            idalbum = Long.valueOf(getArguments().getString("albumid"));
        bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            iv_image.setImageBitmap(bitmap);
            //ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //bitmap.compress(Bitmap.CompressFormat.JPEG,75,stream);.......
        } catch (IOException e) {
            e.printStackTrace();
        }


        retrofit = ApiClient.getRetrofitClient(getContext());
        uploadAPIs = retrofit.create(ApiInterface.class);

        l_layout = rootView.findViewById(R.id.linearLayoutCompat);
        btn_predict = rootView.findViewById(R.id.b_predict);
        btn_photo = rootView.findViewById(R.id.b_rephoto);
        btn_chart = rootView.findViewById(R.id.b_chart);
        tv_res = rootView.findViewById(R.id.tv_res);
        tv_pred = rootView.findViewById(R.id.tv_pred);
        tv_total = rootView.findViewById(R.id.tv_total);
        tv_resume = rootView.findViewById(R.id.tv_resume);
        tv_policy = rootView.findViewById(R.id.tv_policy);
        myProgressBar = rootView.findViewById(R.id.progressBar);

        btn_predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (have_permissions()) {
                    int pred = predict_melanoma();
                } else {
                    Toast.makeText(getActivity(), R.string.needallow, Toast.LENGTH_LONG).show();

                }
            }
        });

        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("humanbody", "yes");
                PhotoFragment fragment = new PhotoFragment();
                fragment.setArguments(args);
                getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

            }
        });

        btn_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("albumid", String.valueOf(idalbum));
                args.putString("albumname", "");
                args.putString("bodypart",  bodyPart);
                PhotosOfAlbum fragment = new PhotosOfAlbum();
                fragment.setArguments(args);
                ((MainActivity)getContext()).getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(R.id.fragment_container,fragment).addToBackStack(null).commit();

            }
        });

        myProgressBar.setIndeterminate(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().findViewById(R.id.my_toolbar).setElevation(-1.5f);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(PredictViewModel.class);
        dbPreds = ((MainActivity)getActivity()).getDB();
        //SHaredView
        viewModel.setName("HOLA VIEWMODEL");
        // TODO: Use the ViewModel
    }


    private int predict_melanoma() {

            uploadToServer(getRealPathFromURI(getContext(), imageUri));
            btn_predict.setVisibility(View.INVISIBLE);
            btn_photo.setVisibility(View.INVISIBLE);
            tv_res.setText(R.string.analisis);
            tv_policy.setText(getString(R.string.bepatient));

        return 1;
    }

    private void uploadToServer(String filePath) {
        //Create a file object using file path
        myProgressBar.setVisibility(View.VISIBLE);
        File file = new File(filePath);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        RequestBody requestFile = RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(imageUri)), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Call<Prediction> responseBodyCall = uploadAPIs.uploadImage("127.0.0.1", 12, Build.SERIAL, filename,multipartBody);

        responseBodyCall.enqueue(new Callback<Prediction>() {
            @Override
            public void onResponse(Call<Prediction> call, Response<Prediction> response) {
                myProgressBar.setVisibility(View.GONE);
                btn_predict.setVisibility(View.VISIBLE);
                btn_photo.setVisibility(View.VISIBLE);
                if(tv_res.getAnimation()!=null)
                tv_res.getAnimation().cancel();
                float pred = Float.parseFloat(response.body().getPred()) * 100;
                float positive = 100-pred;
                String pos = "0";
                String resume="";
                if(pred<50.0){
                    tv_res.setTextColor(Color.GREEN);
                    tv_res.setText(getString(R.string.allwell));
                    tv_resume.setText(getString(R.string.nothingbad));
                    resume=getString(R.string.resume_allwell);
                }else{
                    pos = "1";
                    tv_res.setTextColor(Color.RED);
                    tv_res.setText(getString(R.string.result_cancer));
                    tv_resume.setText(getString(R.string.cancerdetected));
                    resume=getString(R.string.resume_allbad);
                }
                tv_pred.setText(rootView.getResources().getString(R.string.cancerdetection) + " "+ pred + "%");
                tv_total.setText(rootView.getResources().getString(R.string.negativecancer) + " "+ positive + "%");
                tv_policy.setText(getString(R.string.policy));
                btn_chart.setVisibility(View.VISIBLE);
                String timeStamp =
                        new SimpleDateFormat("yyyy MM dd - HH:mm:ss",
                                Locale.getDefault()).format(new Date());
                if(newAlbum)
                 idalbum = dbPreds.insertAlbum(getString(R.string.newalbum),bodyPart,getRealPathFromURI(getContext(),imageUri));
                else
                    dbPreds.UpdateAlbumImage(String.valueOf(idalbum),getRealPathFromURI(getContext(),imageUri));

                dbPreds.insertPrediction(pos,String.valueOf(pred),getRealPathFromURI(getContext(),imageUri),timeStamp,resume,String.valueOf(idalbum));



            }

            @Override
            public void onFailure(Call<Prediction> call, Throwable t) {
                btn_predict.setVisibility(View.VISIBLE);
                btn_photo.setVisibility(View.VISIBLE);
                Log.d("failure", "message = " + t.getMessage());
                Log.d("failure", "cause = " + t.getCause());

                fails++;
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                tv_res.setText(t.getMessage());

                tv_res.setText(getString(R.string.analyzing) + fails + "...");


                if(fails<4)
                    uploadToServer(getRealPathFromURI(getContext(), imageUri));
                else {
                    if(tv_res.getAnimation()!=null)
                    tv_res.getAnimation().cancel();
                    tv_res.setText(getString(R.string.connection));
                    tv_policy.setText(getString(R.string.conn_error_server));
                    myProgressBar.setVisibility(View.GONE);
                    Snackbar.make(rootView, getString(R.string.connerror), Snackbar.LENGTH_LONG).show();
                }
            }
        });
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
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bundle args = new Bundle();
                args.putParcelable("uri", imageUri);
                PredictFragment fragment = new PredictFragment();
                fragment.setArguments(args);
                getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

            }
            if (requestCode == CAM_NEW_IMAGE) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                iv_image.setImageBitmap(bitmap);

                tv_res.setText("");
                tv_pred.setText("Analisis");
                tv_resume.setText("-");
                tv_total.setText("-");
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

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }




}
