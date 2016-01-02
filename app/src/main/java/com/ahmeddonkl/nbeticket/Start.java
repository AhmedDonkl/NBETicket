package com.ahmeddonkl.nbeticket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Start extends Activity {

    Button signup,login;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        signup = (Button) findViewById(R.id.signup_button);
        login = (Button) findViewById(R.id.login_button);

        signup.setOnClickListener(ButtonClick);
        login.setOnClickListener(ButtonClick);
    }

    //Button listener
    private View.OnClickListener ButtonClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            switch (view.getId())
            {
                case R.id.signup_button:
                    Intent intent = new Intent(Start.this,SignUp.class);
                    startActivity(intent);
                    break;
                case R.id.login_button:
                    intent = new Intent(Start.this,Login.class);
                    startActivity(intent);
                    break;
            }
        }
    };


}
