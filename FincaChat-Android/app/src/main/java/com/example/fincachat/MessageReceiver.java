package com.example.fincachat;

import android.app.Notification;
import android.app.Service;
import android.app.VoiceInteractor;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.fincachat.Models.Message;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MessageReceiver extends Service {
    SQLiteDatabase db;
    int userId;
    public int counter=0;
    public MessageReceiver() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");

        stoptimertask();
        db.close();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userId=intent.getIntExtra("user.id",1);
        BroadcastReceiver br=new MessageBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("MessageBroadcaster");
        registerReceiver(br,filter);
        db=openOrCreateDatabase("FincaChat.db",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Messages(message text, sender text, date text)");
        startTimer();
        return START_STICKY;
    }

    private Timer timer;
    private TimerTask timerTask;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 10000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  "+ (counter++));
                checkNotifications();
            }
        };
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void checkNotifications() {
        final String requestUrl = getString(R.string.ApiHost)+"Messages/"+userId+"/GetUnreadMessages";
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JsonArrayRequest jsonArrayRequest;
        final String s = null;
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, requestUrl, s,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject jsonObject;
                        for (int index=0;index<response.length();index++){
                            try {
                                jsonObject=(JSONObject)response.get(index);
                                final com.example.fincachat.Models.Message message=new Message(jsonObject.getLong("MId"),jsonObject.getString("Message"), jsonObject.getString("DateTime"));

                                final String requestUrl=getString(R.string.ApiHost)+"Users/"+jsonObject.getString("UserId");
                                final RequestQueue requestQueue = Volley.newRequestQueue(MessageReceiver.this);
                                JsonObjectRequest request= new JsonObjectRequest(JsonObjectRequest.Method.GET, requestUrl, s, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            message.setUser(response.getString("Name"));
                                            ContentValues values= new ContentValues();
                                            values.put("message",message.getMessage());
                                            values.put("date",message.getDateTime());
                                            values.put("sender",message.getUser());
                                            db.insert("Messages",null,values);
                                            NotificationCompat.Builder notification=new NotificationCompat.Builder(getApplicationContext())
                                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                                    .setContentTitle("FincaChat->New Message")
                                                    .setContentText(message.getMessage())
                                                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message.getMessage()))
                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                                            notificationManagerCompat.notify(1,notification.build());
                                            //code to read message
                                            String request = getString(R.string.ApiHost)+"Messages/"+userId+"/"+message.getmId()+"/ReadMessage";
                                            JsonObjectRequest readRequest=new JsonObjectRequest(JsonObjectRequest.Method.GET, request, new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {

                                                    Log.i("status","OK");
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError e) {
                                                    e.printStackTrace();
                                                }
                                            });
                                            EventBus.getDefault().post(new MessageEvent("message"));
                                            requestQueue.add(readRequest);
                                            //Code to Update RecyclerView on Main Activity

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError e) {
                                        Log.e("Error",e.getMessage());
                                    }
                                });
                                requestQueue.add(request);

                            } catch (JSONException e) {
                                Log.e("Error",e.getMessage());
                            }
                        }
                        Log.i("counter",""+counter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String err = error.getMessage();
                        String s = err;
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }
}
