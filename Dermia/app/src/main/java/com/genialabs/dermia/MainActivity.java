package com.genialabs.dermia;




import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.genialabs.dermia.Controllers.DbHandler;
import com.genialabs.dermia.Controllers.PrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Toolbar mTopToolbar;
    private AboutFragment fragment1;
    private PhotoFragment fragment2;
    private AlbumFragment fragment3;
    private TabFragment fragment4;
    public DbHandler dbPreds;
    public PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Navigation bar
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //Toolbar
        mTopToolbar = findViewById(R.id.my_toolbar);
        mTopToolbar.setTitle("");
        setSupportActionBar(mTopToolbar);
        //Fragments
        fragment1 = new AboutFragment();
        fragment2 = new PhotoFragment();
        fragment3 = new AlbumFragment();
        fragment4 = new TabFragment();

        getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).add(R.id.fragment_container, fragment2,"fragment2").addToBackStack(null).commit();


        dbPreds = new DbHandler(this);

        prefManager = new PrefManager(getApplicationContext());

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mTopToolbar.setElevation(0.1f);
                    }
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(R.id.fragment_container, fragment1, "fragment1").addToBackStack(null).commit();

                    return true;
                case R.id.navigation_dashboard:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mTopToolbar.setElevation(0.1f);
                    }
                    if(!fragment2.isVisible()) {//si fragment2 es el fragment que estoy viendo
                        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(R.id.fragment_container, fragment2, "fragment2").addToBackStack(null).commit();
                    }
                    return true;
                case R.id.navigation_notifications:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mTopToolbar.setElevation(-1.5f);
                    }
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(R.id.fragment_container, fragment4,"fragment4").commit();
                    return true;
            }
            return false;
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            Toast.makeText(this, "Rate App", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.genialabs.medicaible")));
            return true;
        }
        if (id == R.id.action_refresh) {
            Toast.makeText(getApplicationContext(),"Refreshing MAIN",Toast.LENGTH_LONG);
        }


        return false;
    }


public DbHandler getDB(){
    return dbPreds;
}

public PrefManager getPrefs(){
        return prefManager;
}
}
