package com.example.fincachat;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fincachat.Models.Message;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Intent serviceIntent;
    TextView txtWelcomeUser;
    RecyclerView recyclerView;
    ImageButton btnSend;
    EditText edtMessage;
    SQLiteDatabase db;
    List<Message> lstMessages;
    int userId;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userId =getIntent().getIntExtra("user.id",0);
        edtMessage=findViewById(R.id.edtMsg);
        btnSend=findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtMessage.getText().toString().isEmpty()) {
                    SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    String currentDate= format.format(Calendar.getInstance().getTime());
                    String requestUrl = getString(R.string.ApiHost) + "/Messages";
                    JSONObject requstObject = new JSONObject();

                    try {
                        requstObject.put("Message", edtMessage.getText());
                        requstObject.put("UserId", userId);
                        requstObject.put("DateTime", currentDate);
                        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, requestUrl, requstObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                edtMessage.setText("");
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Message Sending Error", error.getMessage());
                            }
                        });
                        queue.add(request);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        txtWelcomeUser=findViewById(R.id.txtWelcomeUser);
        txtWelcomeUser.setText(getText(R.string.WelcomeUser)+getIntent().getStringExtra("user.name"));
        //Setting the RecyclerView
        db=openOrCreateDatabase("FincaChat.db",MODE_PRIVATE,null);
        recyclerView=findViewById(R.id.recyclerView);
        setRecyclerView();
        serviceIntent=new Intent(getBaseContext(),MessageReceiver.class);
        serviceIntent.putExtra("user.id",getIntent().getIntExtra("user.id",1));
        startService(serviceIntent);
    }

    private void setRecyclerView() {
        try{
            lstMessages =  new ArrayList<>();
            Cursor c= db.rawQuery("SELECT * FROM Messages",null);
            c.moveToFirst();
            while (!c.isAfterLast()){
                lstMessages.add(new Message(c.getString(0),c.getString(1),c.getString(2)));
                c.moveToNext();
            }
            c.close();
            MessagesRecyclerViewAdapter adapter =new MessagesRecyclerViewAdapter(lstMessages);
            LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
            recyclerView.scrollToPosition(adapter.getItemCount()-1);
        }
        catch (Exception e){
            Log.e("DB_Error",e.getMessage());
        }
    }

    public void powerOff(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(getBaseContext(),MessageReceiver.class));
    }
    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecyclerViewUpdate(){

    }*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Cursor c=db.rawQuery("SELECT * FROM Messages",null);
        if(c.moveToLast()){
            lstMessages.add(new Message(c.getString(0),c.getString(1),c.getString(2)));
        }
        c.close();
        MessagesRecyclerViewAdapter adapter =new MessagesRecyclerViewAdapter(lstMessages);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(adapter.getItemCount()-1);
    }

    // This method will be called when a SomeOtherEvent is posted
    @Subscribe
    public void handleSomethingElse(SomeOtherEvent event) {
        Log.i("MessageEvent","Function called");
    }
}
