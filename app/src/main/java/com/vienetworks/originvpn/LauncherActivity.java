package com.vienetworks.originvpn;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;
import com.vienetworks.originvpn.R;
import android.app.*;

public class LauncherActivity extends Activity {
//      private static int time = 5000;
    CircularFillableLoaders circularFillableLoaders;
    private Handler handler = new Handler();
    int sleep = 0;
    private ProgressBar text;
    private TextView text2;
    private TextView text3;     
    Handler handlerz = new Handler();    
    int status = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        text = ((ProgressBar)findViewById(R.id.progress_horizontal));
        text2 = ((TextView) findViewById(R.id.value123));
        text3 = ((TextView) findViewById(R.id.event_update));

        new Thread(new Runnable() {
                @Override
                public void run() {
                    while (status < 100) {

                        status += 1;

                        try {
                            Thread.sleep(40);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        handlerz.post(new Runnable() {
                                @Override
                                public void run() {

                                    text.setProgress(status);
                                    text2.setText(String.valueOf(status));
                                    if (status == 1) {
                                        
                                    }
                                    if (status == 5) {
                                        
                                    }
                                    if (status == 10) {
                                        
                                    }
                                    if (status == 15) {
                                        
                                    }
                                    if (status == 20) {
                                        
                                    }
                                    if (status == 25) {
                                        
                                    }
									if (status == 40) {
										
									}
                                    if (status == 50) {
                                        
                                    }
                                    if (status == 55) {
                                        
                                    }
                                    if (status == 60) {
                                        
                                    }
                                    if (status == 70) {
                                        
                                    }
                                    if (status == 75) {
                                      
                                    }
                                    if (status == 80) {
                                        
                                    }
                                    if (status == 85) {
                                        
                                    }
                                    if (status == 90) {
                                        
                                    }
                                    if (status == 95) {
                                        
                                    }
                                    if (status == 100) {
                                        startActivity(new Intent(LauncherActivity.this, SocksHttpMainActivity.class));
                                        finish();
                                    }
                                }
                            });
                    }
                }
            }).start();
        circularFillableLoaders = ((CircularFillableLoaders) findViewById(R.id.circularFillableLoaders));
        circularFillableLoaders.setProgress(100);
        circularFillableLoaders.setImageResource(R.drawable.ic_launcher);
        new Thread(new Runnable() {
                public void run() {
                    while (LauncherActivity.this.sleep < 100) {
                        LauncherActivity com_bytesbridge_learnAndroid_splash_screen = LauncherActivity.this;
                        com_bytesbridge_learnAndroid_splash_screen.sleep = com_bytesbridge_learnAndroid_splash_screen.sleep + 3;                                                
                        LauncherActivity.this.handler.post(new Runnable() {
                                public void run() {
                                    circularFillableLoaders.setProgress(LauncherActivity.this.sleep);
                                }
                            });
                        try {
                            Thread.sleep(110);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        /*      new Handler().postDelayed(new Runnable() {
         public void run() {
         startActivity(new Intent(JaSplash.this, MainActivity.class));
         finish();
         }
         }, (long) time);*/
    }
}
