package com.ahmeddonkl.nbeticket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
public class Splash extends Activity {

    //play music when start app
    MediaPlayer songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Start music
        songs = MediaPlayer.create(this, R.raw.show);
        songs.start();

        //check if this is not first time to use app
        SharedPreferences sharedpreferences = this.getSharedPreferences("User_Data", Context.MODE_PRIVATE);

        //get data of user
        String phone =  sharedpreferences.getString("Phone","");


        //my intent
        final Intent myintent;

        //first time to use it so redirect it to login
        if(phone.equals(""))
        {
            myintent = new Intent(this,SelectBranch.class);
        }
        else
        {
            myintent = new Intent(this,SelectBranch.class);
        }

        Thread mythread = new Thread() {

            public void run() {
                try {
                    sleep(3000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(myintent);
                }
            }

        };

        mythread.start();
    }

    //To stop song
    @Override
    protected void onPause() {
        super.onPause();
        songs.release();
        finish();
    }


}
