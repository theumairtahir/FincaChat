package com.example.fincachat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegister;
    EditText edtName, edtPhone, edtUsername, edtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtName=findViewById(R.id.edtName);
        edtPhone=findViewById(R.id.edtPhone);
        edtUsername=findViewById(R.id.edtUsername);
        edtPassword=findViewById(R.id.edtPass);
        btnRegister=findViewById(R.id.btnLogin);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String requestUrl=getString(R.string.ApiHost)+"/Users";
                final JSONObject requestObejct=new JSONObject();
                if(!(edtPassword.getText().toString().isEmpty()||edtName.getText().toString().isEmpty()||edtPhone.getText().toString().isEmpty()||edtUsername.getText().toString().isEmpty())){
                    try {
                        requestObejct.put("Name", edtName.getText());
                        requestObejct.put("Phone", edtPhone.getText());
                        requestObejct.put("UserName", edtUsername.getText());
                        requestObejct.put("Password", edtPassword.getText());
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, requestUrl,requestObejct, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(RegisterActivity.this,"User Registered Successfully",Toast.LENGTH_SHORT).show();
                                Intent i= new Intent(RegisterActivity.this,MainActivity.class);
                                try {
                                    i.putExtra("user.name",requestObejct.getString("Name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(i);
                                finish();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(RegisterActivity.this,"Registration Failed!",Toast.LENGTH_SHORT).show();
                            }
                        });
                        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                        queue.add(request);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(RegisterActivity.this,"Please fill all required information",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
