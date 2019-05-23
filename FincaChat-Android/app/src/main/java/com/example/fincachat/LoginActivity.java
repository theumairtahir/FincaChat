package com.example.fincachat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fincachat.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Button btnSignIn;
    Button btnRegister;
    EditText edtPassword;
    EditText edtUsername;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnSignIn=findViewById(R.id.btnLogin);
        edtPassword=findViewById(R.id.edtPass);
        edtUsername=findViewById(R.id.edtUsername);
        user=null;
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=edtUsername.getText().toString();
                String password=edtPassword.getText().toString();
                String url=getString(R.string.ApiHost)+"login"+"/"+username+"/"+password;
                String nul=null;
                JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, url, nul, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        user=new User();
                        try {
                            user.setId(response.getInt("Id"));
                            user.setName(response.getString("Name"));
                            user.setPhone(response.getString("Phone"));
                            user.setUsername(response.getString("UserName"));
                            user.setPassword(response.getString("Password"));
                            Intent i=new Intent(LoginActivity.this,MainActivity.class);
                            i.putExtra("user.id",user.getId());
                            i.putExtra("user.name",user.getName());
                            i.putExtra("user.phone",user.getPhone());
                            i.putExtra("user.username",user.getUsername());
                            i.putExtra("user.password",user.getPassword());
                            startActivity(i);
                        } catch (JSONException e) {
                            Log.e("Error",e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,"Sign-In Failed!",Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue= Volley.newRequestQueue(LoginActivity.this);
                requestQueue.add(request);
                requestQueue.start();
            }
        });
        btnRegister=findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }
}
