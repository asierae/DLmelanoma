package com.genialabs.dermia.Controllers;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.genialabs.dermia.MainActivity;
import com.genialabs.dermia.R;

public class SplashScreen extends AppCompatActivity {

    ImageView logo;
    ImageView labsl;
    Animation frombotton,fromtop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logo = findViewById(R.id.ss_iv1);
        labsl = findViewById(R.id.ss_iv2);

        frombotton = AnimationUtils.loadAnimation(this,R.anim.frombotton);
        fromtop = AnimationUtils.loadAnimation(this,R.anim.fromtop);
        labsl.setAnimation(frombotton);
        logo.setAnimation(fromtop);
        new Handler().postDelayed(new Runnable(){
            public void run(){
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            };
        }, 2000);
    }
}
