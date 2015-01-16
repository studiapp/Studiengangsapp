package de.hfu.mos.startanimation;


import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import de.hfu.mos.MainActivity;
import de.hfu.mos.R;

public class StartActivity extends Activity implements
        AnimationListener {

    private ImageView img1;
    private ImageView img2;
    private RelativeLayout layout;
    
    // Animation
    private Animation animFadein;
    private Animation animFadein2;
    
    //used to cancel startanimation if you click on it
    private android.view.View.OnClickListener clickListener;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        timer = new Timer(); 
        timer.schedule(new TimerTask(){
            public void run() {
                StartActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        startActivity(new Intent(StartActivity.this, MainActivity.class));
                        finish();
                    }
                });
            }
        }, 5000);


        img1 = (ImageView) findViewById(R.id.imgHFU);
        img2 = (ImageView) findViewById(R.id.imgMOS);
        layout = (RelativeLayout) findViewById(R.id.startAnimLayout);

        // load the animation
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        animFadein2 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in_mos);

        // set animation listener
        animFadein.setAnimationListener(this);
        animFadein2.setAnimationListener(this);


        img1.setVisibility(View.VISIBLE);
        img2.setVisibility(View.VISIBLE);

        // start the animation
        img1.startAnimation(animFadein);
        img2.startAnimation(animFadein2);
        
        
        // end animation if clicked somewhere
        clickListener = new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				timer.cancel();
				timer.purge();
				startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();				
			}
		};
		
		img1.setOnClickListener(clickListener);
		img2.setOnClickListener(clickListener);
		layout.setOnClickListener(clickListener);

    }



    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
