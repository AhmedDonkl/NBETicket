package com.ahmeddonkl.nbeticket;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ahmed Dongl on 3/6/2015.
 */

// our ViewHolder.
// caches our views
class ViewHolder
{
    TextView LBranch_Name ;
    TextView LTicket_Number ;
    TextView LReservation_Date ;
    TextView LExpiry_Date ;
}

public class Ticket_adapter extends ArrayAdapter<Ticket> {

    Activity activity;
    int layoutResourceId;
    List<Ticket> ticket_items = null;

    public Ticket_adapter(Activity activity,int layoutResourceId,List<Ticket> ticket_items)
    {
        super(activity,layoutResourceId,ticket_items);
        this.activity = activity;
        this.layoutResourceId = layoutResourceId;
        this.ticket_items=ticket_items;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder  holder = null;

        if (convertView == null) {

            // inflate the layout
            LayoutInflater inflater = ((Activity)activity).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);

            // well set up the ViewHolder
            holder = new ViewHolder();
            holder.LBranch_Name = (TextView) convertView.findViewById(R.id.LBranch_Name);
            holder.LTicket_Number = (TextView) convertView.findViewById(R.id.LTicket_Number);
            holder.LReservation_Date = (TextView) convertView.findViewById(R.id.LReservation_Date);
            holder.LExpiry_Date = (TextView) convertView.findViewById(R.id.LExpiry_Date);
            // store the holder with the view.
            convertView.setTag(holder);
        }
        else
        {
            // we've just avoided calling findViewById() on resource every time
            // just use the viewHolder
            holder = (ViewHolder) convertView.getTag();
        }



        Ticket obj = ticket_items.get(position);
        holder.LBranch_Name.setText(obj.branch_name);
        holder.LTicket_Number.setText(obj.number);
        holder.LExpiry_Date.setText(obj.expire_date);
        holder.LReservation_Date.setText(obj.date);

        return convertView;
    }
}

