package com.genialabs.dermia;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class TabFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View rootView;
    private ViewPagerAdapter adapter;

    public TabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabFragment newInstance(String param1, String param2) {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tab, container, false);



        // find views by id
        ViewPager vp= rootView.findViewById(R.id.viewpager);
        TabLayout tl = rootView.findViewById(R.id.tablayout);

// attach tablayout with viewpager
        tl.setupWithViewPager(vp);

        adapter = new ViewPagerAdapter(getChildFragmentManager(),getContext());

// add your fragments
       // adapter.addFrag(new AlbumFragment(), getString(R.string.record));
        //adapter.addFrag(new PhotoFragment(), getString(R.string.tittle_album));

// set adapter on viewpager
        vp.setAdapter(adapter);
        try {
            boolean move = getArguments().getBoolean("movetoalbum");
            if (move)
                vp.setCurrentItem(1);
        }catch(NullPointerException e){

        };

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}

class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    Context context;
    public ViewPagerAdapter(FragmentManager manager,Context context) {
        super(manager);
        this.context=context;
        mFragmentList.add(AlbumFragment.newInstance());// a new instance se le pueden pasar argumentos
        mFragmentList.add(AlbumsMainFragment.newInstance());
        mFragmentTitleList.add(context.getString(R.string.record));
        mFragmentTitleList.add(context.getString(R.string.tittle_album));
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return  mFragmentList.get(0);
            case 1:
                return  mFragmentList.get(1);
        }

        return null;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public void addFrag(Fragment fragment) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add("");
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }


}