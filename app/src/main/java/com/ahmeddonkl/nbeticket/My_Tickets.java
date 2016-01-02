package com.ahmeddonkl.nbeticket;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class My_Tickets extends ActionBarActivity {

    String current_date,branch_name,selected_date,ticket_number;

    //list view of Tickets
    ListView list;
    Ticket_adapter ticket_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__tickets);

        //declare  SharedPreferences to get tickets
        SharedPreferences prefs = this.getSharedPreferences("Tickets", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();

        branch_name = prefs.getString("Branch_name","");
        selected_date = prefs.getString("date_selected", "");
        ticket_number =  prefs.getString("ticket_number", "");
        current_date = prefs.getString("Current_Date","");

        //split
        List<String> names = Arrays.asList(branch_name.split("\\s*,\\s*"));
        List<String> expire_date = Arrays.asList(selected_date.split("\\s*,\\s*"));
        List<String> date = Arrays.asList(current_date.split("\\s*,\\s*"));
        List<String> number = Arrays.asList(ticket_number.split("\\s*,\\s*"));

        final List<Ticket> ticket_items = new ArrayList<Ticket>();

        for (int i = 1 ; i < names.size() ; i++)
        {

            ticket_items.add(new Ticket(names.get(i),expire_date.get(i),date.get(i),number.get(i)));
        }

       //publish data on list
        list=(ListView) findViewById(R.id.tickets_list);
        ticket_adapter =new Ticket_adapter(this,R.layout.ticket_list_item,ticket_items);
        list.setAdapter(ticket_adapter);

        //Swipe Listner
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        list,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions)
                            {
                                for (int position : reverseSortedPositions)
                                {
                                    //get selected
                                    Ticket obj = ticket_items.get(position);
                                    //remove from screen
                                    ticket_items.remove(position);
                                    //remove from shared preference
                                    branch_name   = branch_name.replace(","+obj.branch_name,"");
                                    selected_date =  selected_date.replace(","+obj.expire_date,"");
                                    ticket_number =   ticket_number.replace(","+obj.number,"");
                                    current_date  =  current_date.replace(","+obj.date,"");

                                    editor.putString("Branch_name",  branch_name);
                                    editor.putString("date_selected",  selected_date);
                                    editor.putString("ticket_number",  ticket_number);
                                    editor.putString("Current_Date", current_date);
                                    editor.commit();
                                }
                                 ticket_adapter.notifyDataSetChanged();
                            }
                        });
        list.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        list.setOnScrollListener(touchListener.makeScrollListener());

}



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my__tickets, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
