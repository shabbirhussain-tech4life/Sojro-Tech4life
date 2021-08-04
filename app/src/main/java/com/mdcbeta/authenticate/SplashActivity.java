package com.mdcbeta.authenticate;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.mdcbeta.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.splash_logo)
    ImageView splashLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        Animation fadeIn = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.fade_in);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {

                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();


            }
        });
        splashLogo.startAnimation(fadeIn);

//        Log.d("TIME", "onCreate: "+Utils.localToUTC("01:00"));
//        Log.d("TIME", "onCreate: "+Utils.UTCTolocal("20:00"));


    }
}
