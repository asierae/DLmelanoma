package com.genialabs.dermia.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.genialabs.dermia.Controllers.PasarelaFragment;
import com.genialabs.dermia.MainActivity;
import com.genialabs.dermia.Models.AlbumMain;
import com.genialabs.dermia.Models.Prediction;
import com.genialabs.dermia.PhotoFragment;
import com.genialabs.dermia.PhotosOfAlbum;
import com.genialabs.dermia.PredictFragment;
import com.genialabs.dermia.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class AlbumsMainAdapter extends RecyclerView.Adapter<AlbumsMainAdapter.MyViewHolder> {
    private ArrayList<AlbumMain> mDataset;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tv_id_album,tv_bodypart;
        public ImageView cv_iv_albums_main;
        public FloatingActionButton fab_add,fab_delete;
        public CardView cv_each;

        public MyViewHolder(View v) {
            super(v);
            tv_bodypart = v.findViewById(R.id.cv_tv_bodypart);
            cv_iv_albums_main = v.findViewById(R.id.cv_iv_albums_main);
            fab_add = v.findViewById(R.id.cv_fab_albums_main);
            fab_delete = v.findViewById(R.id.cv_fab_albums_delete);
            tv_id_album = v.findViewById(R.id.cv_tv_idalbum);
            cv_each = v.findViewById(R.id.cv_albums_main);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AlbumsMainAdapter(ArrayList<AlbumMain> myDataset, Context myContext) {
        mDataset = myDataset;
        mContext = myContext;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AlbumsMainAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cv_albums_main, parent, false);

        return new MyViewHolder(itemView);

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.tv_id_album.setText(mDataset.get(position).getId_album());
        holder.tv_bodypart.setText(mDataset.get(position).getBodypart());

        BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inPreferredConfig= Bitmap.Config.RGB_565;
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeFile(mDataset.get(position).getAlbumplus(),options);

        holder.cv_iv_albums_main.setImageBitmap(bitmap);

        holder.fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idalbum = mDataset.get(position).getId_album();
                Bundle args = new Bundle();
                args.putString("albumid", idalbum);
                args.putString("bodypart", mDataset.get(position).getBodypart());
                PasarelaFragment fragment = new PasarelaFragment();
                fragment.setArguments(args);
                ((MainActivity)mContext).getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(R.id.fragment_container,fragment).addToBackStack(null).commit();

            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = ((MainActivity)mContext).getLayoutInflater();
        holder.fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setView(inflater.inflate(R.layout.dialog_yes_no, null))
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                String id_del = "0";
                                for(AlbumMain p:mDataset){
                                    id_del = mDataset.get(position).getId_album();
                                    if (p.getId_album() == id_del) {
                                        mDataset.remove(p);
                                        break;
                                    }
                                }
                                ((MainActivity)mContext).getDB().DeleteAlbum(Integer.valueOf(id_del));
                                notifyItemRemoved(position);

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).show();
            }
        });

        holder.cv_each.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idalbum = mDataset.get(position).getId_album();
                Bundle args = new Bundle();
                args.putString("albumid", idalbum);
                args.putString("albumname",  mDataset.get(position).getAlbumname());
                args.putString("bodypart",  mDataset.get(position).getBodypart());
                PhotosOfAlbum fragment = new PhotosOfAlbum();
                fragment.setArguments(args);
                ((MainActivity)mContext).getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(R.id.fragment_container,fragment).addToBackStack(null).commit();

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
