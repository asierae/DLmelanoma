package com.genialabs.dermia.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.genialabs.dermia.MainActivity;
import com.genialabs.dermia.Models.Prediction;
import com.genialabs.dermia.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {
    private ArrayList<Prediction> mDataset;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tv_round,tv_tittle,tv_id,tv_date;
        public ImageView iv_photo;
        public FloatingActionButton fab_delete,fab_share;
        public MyViewHolder(View v) {
            super(v);
            tv_round = v.findViewById(R.id.cv_tv_pred);
            tv_tittle = v.findViewById(R.id.cv_tv_resume);
            iv_photo = v.findViewById(R.id.cv_iv_photo);
            fab_delete = v.findViewById(R.id.cv_fabInfo);
            fab_share = v.findViewById(R.id.cv_fab_share);
            tv_id = v.findViewById(R.id.cv_tv_id);
            tv_date = v.findViewById(R.id.cv_tv_date);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AlbumAdapter(ArrayList<Prediction> myDataset,Context myContext) {
        mDataset = myDataset;
        mContext = myContext;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AlbumAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cv_prediction2, parent, false);

        return new MyViewHolder(itemView);

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        float pred = Float.valueOf(mDataset.get(position).getPred());
        if(pred>49.9)
            holder.tv_round.setBackgroundColor(Color.RED);
        holder.tv_round.setTextSize(16);
        holder.tv_round.setText(mDataset.get(position).getPred()+"%");
        holder.tv_tittle.setText(mDataset.get(position).getResume());
        holder.tv_id.setText(String.valueOf(mDataset.get(position).getId()));
        holder.tv_date.setText(mDataset.get(position).getDate());

        BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inPreferredConfig= Bitmap.Config.RGB_565;
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeFile(mDataset.get(position).getImage(),options);
        holder.iv_photo.setImageBitmap(bitmap);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = ((MainActivity)mContext).getLayoutInflater();

        holder.fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setView(inflater.inflate(R.layout.dialog_yes_no, null))
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                int id_del = 0;
                                for(Prediction p:mDataset){
                                    id_del = mDataset.get(position).getId();
                                    if (p.getId() == id_del) {
                                        mDataset.remove(p);
                                        break;
                                    }
                                }
                                ((MainActivity)mContext).getDB().DeletePred(id_del);
                                notifyItemRemoved(position);

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).show();
            }
        });

        holder.fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save image
                View cv= (View) holder.fab_share.getParent();
                Bitmap b = ViewShot(cv);
                String imagePath = MediaStore.Images.Media.insertImage(
                        mContext.getContentResolver(),
                        b,
                        "tmp_image",
                        "tmp_image"
                );

                Uri uri = Uri.parse(imagePath);
                //share saved image
                Intent iShare = new Intent(Intent.ACTION_SEND);
                iShare.setType("image/*");
                iShare.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.share_info));
                iShare.putExtra(Intent.EXTRA_STREAM, uri);
                mContext.startActivity(Intent.createChooser(iShare, "Share via"));
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Bitmap ViewShot(View v) {
        int height = v.getHeight();
        int width = v.getWidth();
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas (b);
        v.layout(0, 0 , v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }
}
