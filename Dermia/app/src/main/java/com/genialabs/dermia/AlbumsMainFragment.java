package com.genialabs.dermia;


import androidx.lifecycle.ViewModelProviders;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genialabs.dermia.Adapters.AlbumAdapter;
import com.genialabs.dermia.Adapters.AlbumsMainAdapter;
import com.genialabs.dermia.Controllers.DbHandler;
import com.genialabs.dermia.Models.AlbumMain;
import com.genialabs.dermia.Models.Prediction;

import java.util.ArrayList;

public class AlbumsMainFragment extends Fragment {

    private AlbumsMainViewModel mViewModel;
    private View rootView;
    private ImageView iv_bodypart;
    private TextView tv_bodypart,tv_id;
    private DbHandler dbPreds;
    private RecyclerView recyclerView;
    private LinearLayout album_layout;
    private AlbumsMainAdapter mAdapter;

    public static AlbumsMainFragment newInstance() {
        return new AlbumsMainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.albums_main_fragment, container, false);


        dbPreds = ((MainActivity)getActivity()).getDB();

        album_layout = rootView.findViewById(R.id.l_albumMain);

        //RecyclerView
        recyclerView = rootView.findViewById(R.id.rv_albums_main);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<AlbumMain> myDataset = dbPreds.getAllAlbumsMain();

        if(myDataset.size()==0){
            album_layout.setVisibility(View.VISIBLE);

        }
        else {
            album_layout.setVisibility(View.INVISIBLE);
            mAdapter = new AlbumsMainAdapter(myDataset, getContext());
            //mAdapter.setHasStableIds(true);
            recyclerView.setAdapter(mAdapter);
        }



        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AlbumsMainViewModel.class);
        // TODO: Use the ViewModel
    }

}
