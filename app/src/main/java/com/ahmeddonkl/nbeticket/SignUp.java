package com.ahmeddonkl.nbeticket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;


public class SignUp extends ActionBarActivity  {

    String name, id ,email, phone,pass, conpass;
    EditText username,ID, myemail, myphone, password, confirm;


    private static final Pattern EMAIL_PATTERN = Pattern
            .compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    private static final Pattern PASWORD_PATTERN = Pattern
            .compile("[a-zA-Z0-9]{1,250}");
    private static final Pattern PHONE_PATTERN = Pattern
            .compile("[0-9]{1,250}");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //validate data when submit
        Button button_submit=(Button)findViewById(R.id.submit_signup);
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                username = (EditText)findViewById(R.id.usernamebox);
                name=username.getText().toString();

                ID =(EditText) findViewById(R.id.idbox);
                id=ID.getText().toString();

                myemail = (EditText)findViewById(R.id.emailbox);
                email=myemail.getText().toString();

                myphone = (EditText)findViewById(R.id.phonebox);
                phone=myphone.getText().toString();

                password = (EditText)findViewById(R.id.passwordbox);
                pass=password.getText().toString();

                confirm = (EditText)findViewById(R.id.confirmlbox);
                conpass=confirm.getText().toString();


                if ((!CheckID(id)))
                {
                    ID.setError("Invalid ID");
                }
                else if ((!CheckEmail(email)))
                {
                    myemail.setError("Invalid Email");
                }

                else if ((Checkphoneno(phone))==false)
                {
                    myphone.setError("Invalid Phone");
                }
                else if ((!Checkpassword(pass)))
                {
                    password.setError("Invalid password");
                }
                else if(confirmpassword(pass,conpass)==false)
                {
                    confirm.setError("password Don't Match");
                }

                else
                {
                    //validate user online on server
                    //response will saved on response string
                    FetchSignUpData signupTask = new FetchSignUpData(SignUp.this);

                    ProgressDialog dialog = new ProgressDialog(SignUp.this);
                    dialog.setMessage("Loading....");
                    dialog.show();

                    String resondstr = "";
                    try
                    {
                        resondstr = signupTask.execute(name, email, id, phone, pass).get();
                        Log.d("Respond", resondstr);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    dialog.hide();
                    if(resondstr!=null)
                    {


                        if (resondstr.equals("Valid User")) {
                            Toast.makeText(getApplicationContext(), "Sign Up Successful", Toast.LENGTH_LONG).show();
                            Intent myintent = new Intent(SignUp.this, Login.class);
                            startActivity(myintent);
                        } else {
                            switch (resondstr) {
                                case "Email Already Exist":
                                    myemail.setError("Email Already Exist");
                                    break;
                                case "National ID Already Exist":
                                    ID.setError("National ID Already Exist");
                                    break;
                                case "Phone Already Exist":
                                    myphone.setError("Phone Already Exist");
                                    break;

                            }

                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "No Connection", Toast.LENGTH_LONG).show();
                    }



                }


            }


        });
    }


    //mail check pattern contain @ex.com
    private boolean CheckEmail(String Email)
    {
        return EMAIL_PATTERN.matcher(Email).matches();
    }


    private boolean CheckID(String id)
    {

        if(id.length()!=14)
        {
            return false;
        }
        else
        {
            return PHONE_PATTERN.matcher(id).matches();
        }
    }

    private boolean Checkphoneno(String PhoneNo)
    {

        if(PhoneNo.length()!=11)
        {
            return false;
        }
        else
        {
            return PHONE_PATTERN.matcher(PhoneNo).matches();
        }
    }

    private boolean Checkpassword(String password)
    {

        return PASWORD_PATTERN.matcher(password).matches();
    }

    private boolean confirmpassword(String password , String confirmation)
    {

        if(password.equals(confirmation))
            return true;
        else return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,Home.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
