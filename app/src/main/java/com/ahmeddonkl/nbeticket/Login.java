package com.ahmeddonkl.nbeticket;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class Login extends ActionBarActivity {

    EditText phone;
    EditText password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone = (EditText) findViewById(R.id.phonebox);
        password = (EditText) findViewById(R.id.passwordbox);
        login = (Button) findViewById(R.id.submit_login);
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                ProgressDialog dialog = new ProgressDialog(Login.this);
                dialog.setMessage("Loading....");
                dialog.show();

                //validate user login
                //response will saved on response string
                FetchLoginData loginTask = new FetchLoginData(Login.this);
                String resondstr = " ";
                try {
                    resondstr = loginTask.execute(phone.getText().toString(), password.getText().toString()).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Login.this, Home.class);
                startActivity(intent);
                dialog.hide();
                if(resondstr!=null)
                {
                    if (resondstr.equals("User Found")) {
                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                        //if user name and password is correct
                         intent = new Intent(Login.this, Home.class);
                        startActivity(intent);

                        //save user on shared pref to make further login
                        SharedPreferences sharedpreferences = getSharedPreferences("User_Data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("Phone", phone.getText().toString());
                        editor.commit();
                    } else {
                        phone.setError("Wrong Phone Or Password");

                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No Connection", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

}