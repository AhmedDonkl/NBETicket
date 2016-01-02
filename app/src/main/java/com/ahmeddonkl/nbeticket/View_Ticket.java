package com.ahmeddonkl.nbeticket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class View_Ticket extends ActionBarActivity
{

    //Ticket Data
    String branch_id,branch_name,selected_date,ticket_number;

    //Views of Ticket
    TextView Branch_Name,Ticket_Number,Expiry_Date;
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__ticket);

        //link Views
        Branch_Name = (TextView)findViewById(R.id.Branch_Name);
        Ticket_Number = (TextView)findViewById(R.id.Ticket_Number);
        Expiry_Date = (TextView)findViewById(R.id.Expiry_Date);
        done = (Button)findViewById(R.id.done);

        //get data from intent
        Intent intent = getIntent();
        if(intent.hasExtra("Branch_id")&&intent.hasExtra("date_selected")&&intent.hasExtra("Ticket_Number"))
        {
            branch_id = intent.getStringExtra("Branch_id");
            selected_date = intent.getStringExtra("date_selected");
            branch_name = DataBaseHelper.All.get(branch_id);
            ticket_number = intent.getStringExtra("Ticket_Number");
        }

        //set data on text views
        Branch_Name.setText(branch_name);
        Ticket_Number.setText(ticket_number);
        Expiry_Date.setText(selected_date);

        //get current date
        Date now = new Date();
        Date alsoNow = Calendar.getInstance().getTime();
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

        //save ticket on shared pref
        SharedPreferences prefs = this.getSharedPreferences("Tickets", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Branch_name", prefs.getString("Branch_name","") + "," + branch_name);
        editor.putString("date_selected", prefs.getString("date_selected", "") + "," + selected_date);
        editor.putString("ticket_number", prefs.getString("ticket_number", "") + "," + ticket_number);
        editor.putString("Current_Date", prefs.getString("Current_Date","") + "," + Current_Date);
        editor.putString("Tickets_Count", prefs.getString("Tickets_Count","") + "," + Current_Date);
        editor.commit();

        //when press done go to home
        done.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(View_Ticket.this,Home.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view__ticket, menu);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
