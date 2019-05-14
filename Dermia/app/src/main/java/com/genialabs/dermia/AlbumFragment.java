package com.genialabs.dermia;


import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.genialabs.dermia.Adapters.AlbumAdapter;
import com.genialabs.dermia.Controllers.DbHandler;
import com.genialabs.dermia.Models.Prediction;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class AlbumFragment extends Fragment {

    private AlbumViewModel mViewModel;
    private View rootView;
    private RecyclerView recyclerView;
    private AlbumAdapter mAdapter;
    private ArrayList<Prediction> myDataset;
    private DbHandler dbPreds;
    private ConstraintLayout album_layout;

    public static AlbumFragment newInstance()
    {
        return new AlbumFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView=  inflater.inflate(R.layout.album_fragment, container, false);

        dbPreds = ((MainActivity)getActivity()).getDB();
        //Para onCrreatedoption y itemselected
        setHasOptionsMenu(true);

        album_layout = rootView.findViewById(R.id.album_layout);

        //RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerV_album);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<Prediction> myDataset = dbPreds.getAllPredictions();

        if(myDataset.size()==0){
            album_layout.setVisibility(View.VISIBLE);

        }
        else {
            album_layout.setVisibility(View.INVISIBLE);
            mAdapter = new AlbumAdapter(myDataset, getContext());
            //mAdapter.setHasStableIds(true);
            recyclerView.setAdapter(mAdapter);
        }


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AlbumViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.toolbar_menu_album, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_refresh:


                return true;
        }
        return false;
    }


}
