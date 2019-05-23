package com.example.fincachat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.fincachat.Models.Message;

import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class MessageBroadcastReceiver extends BroadcastReceiver {
    SQLiteDatabase db;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Message message =new Message(intent.getLongExtra("message.mId",0),intent.getStringExtra("message.message"), intent.getStringExtra("message.dateTime"),intent.getStringExtra("message.User"));
        /*NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("FincaChat")
                        .setContentText(message.getMessage());
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(101, mBuilder.build());*/

    }
}
