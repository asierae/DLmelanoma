package com.genialabs.dermia;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genialabs.dermia.Adapters.PhotosAlbumAdapter;
import com.genialabs.dermia.Controllers.DbHandler;
import com.genialabs.dermia.Controllers.PasarelaFragment;
import com.genialabs.dermia.Models.Prediction;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class PhotosOfAlbum extends Fragment {

    private PhotosOfAlbumViewModel mViewModel;
    private View rootView;
    private RecyclerView recyclerView;
    private DbHandler dbPreds;
    private String albumid,albumname,bodypart;
    private PhotosAlbumAdapter mAdapter;
    private TextView tv_albumname,tv_bodypart;
    private LinearLayout l_add_new;
    private BarChart chart;

    public static PhotosOfAlbum newInstance() {
        return new PhotosOfAlbum();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView=  inflater.inflate(R.layout.photos_of_album_fragment, container, false);
        recyclerView = rootView.findViewById(R.id.rv_photos_of_album);
        albumid = getArguments().getString("albumid");
        albumname = getArguments().getString("albumname");
        bodypart = getArguments().getString("bodypart");

        tv_bodypart = rootView.findViewById(R.id.tv_bodypart_photos);
        tv_albumname = rootView.findViewById(R.id.tv_albumname_photos);
        tv_bodypart.setText(bodypart);
        tv_albumname.setText(albumname);
        l_add_new=rootView.findViewById(R.id.l_add_photo);
        l_add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                args.putString("albumid", albumid);
                args.putString("bodypart", bodypart);
                PasarelaFragment fragment = new PasarelaFragment();
                fragment.setArguments(args);
                ((MainActivity)getContext()).getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(R.id.fragment_container,fragment).addToBackStack(null).commit();

            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));



        dbPreds = ((MainActivity)getActivity()).getDB();
        ArrayList<Prediction> photoList = dbPreds.getPredsFromAlbum(Integer.valueOf(albumid));

        mAdapter = new PhotosAlbumAdapter(photoList, getContext());
        //mAdapter.setHasStableIds(true);
        recyclerView.setAdapter(mAdapter);

        //CHART

         chart = rootView.findViewById(R.id.mychart1);

        ArrayList preds = new ArrayList();
        for(int i=0;i<photoList.size();i++){
            preds.add(new BarEntry(i, Float.valueOf(photoList.get(i).getPred())));
        }



        BarDataSet bardataset = new BarDataSet(preds, getString(R.string.skincancerporc));
        chart.animateY(1500);
        BarData data = new BarData(bardataset);
        bardataset.setBarBorderWidth(3);
        bardataset.setColors(ColorTemplate.PASTEL_COLORS);
        chart.getDescription().setEnabled(false);
        //Borrar cuadricula
        //chart.getAxisLeft().setDrawGridLines(false);
        //chart.getXAxis().setDrawGridLines(false);
        //chart.getAxisRight().setDrawGridLines(false);
        //fijar maximo de 100, se ven pequeños los negativos
        //chart.getAxisLeft().setAxisMaximum(100f);
        chart.getAxisLeft().addLimitLine(new LimitLine(50,getString(R.string.dangerlimit)));
        chart.getXAxis().setGranularityEnabled(true);
        chart.getXAxis().setGranularity(1.0f);
        chart.getXAxis().setLabelCount(photoList.size());
        chart.setData(data);



        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PhotosOfAlbumViewModel.class);
        // TODO: Use the ViewModel
    }

}
