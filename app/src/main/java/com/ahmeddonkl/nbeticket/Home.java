package com.ahmeddonkl.nbeticket;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Home extends ActionBarActivity
{
    Button get_ticket,view_ticket;
    String saved_date = " " ;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        get_ticket = (Button) findViewById(R.id.getticket_bttn);
        view_ticket = (Button) findViewById(R.id.viewticket_bttn);

        get_ticket.setOnClickListener(ButtonClick);
        view_ticket.setOnClickListener(ButtonClick);

        //declare  SharedPreferences to get tickets
        SharedPreferences prefs = getSharedPreferences("Tickets", Context.MODE_PRIVATE);
        //get all tickets date
         saved_date = prefs.getString("Tickets_Count", "");

    }

    //Button listener
    private View.OnClickListener ButtonClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            switch (view.getId())
            {
                case R.id.getticket_bttn:

                    //get current date
                    Date now = new Date();
                    String dateStr = new SimpleDateFormat("dd/MM/yyyy").format(now);
                    SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
                    Date dateObj = null;
                    try
                    {
                        dateObj = curFormater.parse(dateStr);
                    }
                    catch (ParseException e)
                    {
                        e.printStackTrace();
                    }
                    SimpleDateFormat postFormater = new SimpleDateFormat("dd-MMMM-yyyy");
                    String Current_Date = postFormater.format(dateObj);

                    //check how many time user reserved in same date
                    //if ticket count > 2 restrict user from reserve again
                    int lastIndex = 0;
                    int count = 0;

                    while(lastIndex != -1){

                        lastIndex = saved_date.indexOf(Current_Date,lastIndex);

                        if(lastIndex != -1){
                            count ++;
                            lastIndex += Current_Date.length();
                        }
                    }

                    if(count < 2)
                    {
                        Intent intent = new Intent(Home.this, SelectBranch.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Sorry You Exceed Today's Limit", Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.viewticket_bttn:
                    Intent intent = new Intent(Home.this,My_Tickets.class);
                    startActivity(intent);
                    break;
            }
        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to Logout?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {

                            //remove user from shared pref
                            SharedPreferences sharedpreferences = getSharedPreferences("User_Data", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.remove("Phone");
                            editor.commit();
                            Intent intent = new Intent(Home.this,Start.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
