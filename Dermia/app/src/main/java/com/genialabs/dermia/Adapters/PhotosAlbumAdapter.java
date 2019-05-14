package com.genialabs.dermia.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.genialabs.dermia.Controllers.PasarelaFragment;
import com.genialabs.dermia.MainActivity;
import com.genialabs.dermia.Models.Prediction;
import com.genialabs.dermia.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PhotosAlbumAdapter extends RecyclerView.Adapter<PhotosAlbumAdapter.MyViewHolder> {
    private ArrayList<Prediction> mDataset;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tv_prediction;
        public ImageView cv_iv_photo;


        public MyViewHolder(View v) {
            super(v);
            tv_prediction = v.findViewById(R.id.cv_tv_pred);
            cv_iv_photo = v.findViewById(R.id.cv_iv_photo_each);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PhotosAlbumAdapter(ArrayList<Prediction> myDataset, Context myContext) {
        mDataset = myDataset;
        mContext = myContext;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PhotosAlbumAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cv_each_photo, parent, false);

        return new MyViewHolder(itemView);

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.tv_prediction.setText(mDataset.get(position).getPred());
        BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inPreferredConfig= Bitmap.Config.RGB_565;
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeFile(mDataset.get(position).getImage(),options);
        holder.cv_iv_photo.setImageBitmap(bitmap);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}

