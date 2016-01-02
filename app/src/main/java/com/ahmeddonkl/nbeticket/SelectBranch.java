package com.ahmeddonkl.nbeticket;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class SelectBranch extends ActionBarActivity implements LocationListener  {

    //views on screen
    android.widget.Spinner gov_spin,branch_spin,near_spin;
    Button submit,get_location;

    //To get user location
    LocationManager mLocationManager;


    //make instant from DB Helper and open data base
    final DataBaseHelper myDbHelper = new DataBaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_branch);

        //link views
        gov_spin=(android.widget.Spinner)findViewById(R.id.gov_spin);
        branch_spin=(android.widget.Spinner)findViewById(R.id.branch_spin);
        near_spin=(android.widget.Spinner)findViewById(R.id.near_spin);
        submit = (Button)findViewById(R.id.branch_submit);
        get_location = (Button)findViewById(R.id.get_location);

        //set on click on Buttons
        submit.setOnClickListener(ButtonClick);
        get_location.setOnClickListener(ButtonClick);


        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            myDbHelper.openDataBase();

        }catch(SQLException sqle){

            throw sqle;

        }

        //GET DATA OF governments and publish on Cov Spinner
        myDbHelper.getAllGov();

        //publish data in Gov Spin
        ArrayAdapter<String> gov_adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,DataBaseHelper.Gov_List);
        gov_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gov_spin.setAdapter(gov_adapter);



        //inflate branch spin depend on cov spin
        gov_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String gov_selected = gov_spin.getSelectedItem().toString();
                myDbHelper.getBranchByCov(DataBaseHelper.Governments.get(gov_selected));
                //initialize Branch Adapter
                ArrayAdapter<String> branch_adapter=new ArrayAdapter<String>(SelectBranch.this,android.R.layout.simple_spinner_item,DataBaseHelper.Branch_List);
                branch_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                branch_spin.setAdapter(branch_adapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //Button listener
    private View.OnClickListener ButtonClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            switch (view.getId())
            {
                case R.id.branch_submit:
                    final String branch_selected;
                    final String branch_id;
                    String near = " ";

                    try
                    {
                        near = near_spin.getSelectedItem().toString();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                        //take selected
                        if(!near.equals(" "))
                        {
                            branch_selected = near_spin.getSelectedItem().toString();
                            branch_id = DataBaseHelper.Nearest.get(branch_selected);
                        }
                    else
                        {
                            branch_selected = branch_spin.getSelectedItem().toString();
                            branch_id = DataBaseHelper.Branches.get(branch_selected);
                        }

                    new AlertDialog.Builder(SelectBranch.this)
                            .setTitle("Branch Selected")
                            .setMessage("Are you sure you Want to go '"+ branch_selected +"' Branch?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    //go to date page
                                    Intent intent = new Intent(SelectBranch.this,Select_Date.class).putExtra("Branch_id",branch_id);
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
                    break;
                case R.id.get_location:
                    //get nearest Branches to user
                    Get_Near_Places();
                    break;
            }
        }
    };

    private void Get_Near_Places()
    {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000)
        {
            //get location
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            List<String> near_places_list = myDbHelper.getNearest_Branches(lat, lng);

            ArrayAdapter<String> branch_adapter=new ArrayAdapter<String>(SelectBranch.this,android.R.layout.simple_spinner_item,near_places_list);
            branch_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            near_spin.setAdapter(branch_adapter);

        }
        else
        {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        }

    }


        @Override
        public void onLocationChanged(Location loc)
        {
            if (loc != null)
            {
                mLocationManager.removeUpdates(this);
            }

        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Enable Gps First", Toast.LENGTH_LONG ).show();
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_branch, menu);
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
            Intent intent = new Intent(this,Select_Date.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
