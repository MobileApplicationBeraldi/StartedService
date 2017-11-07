package com.example.roberto.startedservice;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by roberto on 07.11.2015.
 */
public class MyService extends Service {


    Boolean running=false;
    MediaPlayer player=null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!running) //Service can be requested several times....
        {
            player = MediaPlayer.create(this, R.raw.braincandy);
            player.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
            player.start();
            running=true;


            Intent i=new Intent(this,MainActivity.class);

            //getActivity is a Factory method
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i, PendingIntent.FLAG_ONE_SHOT);


            //Define a new Notification from the compatibility builder
            NotificationCompat.Builder mBulider = new NotificationCompat.Builder(this);

            //Set the main parameters....
            mBulider
                    .setContentTitle("Stop the music")
                    .setContentText("Click to Stop the music")
                    .setSmallIcon(R.drawable.mail)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);


            //Build the notification
            Notification notification = mBulider.build();

            startForeground(10,notification);

            Toast.makeText(this,"Service PID: "+Integer.toString(android.os.Process.myPid()),Toast.LENGTH_SHORT).show();

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (running){
            player.stop();
            player.release();
            running=false;
        }
    }
}
