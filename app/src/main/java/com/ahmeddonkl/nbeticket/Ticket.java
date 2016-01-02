package com.ahmeddonkl.nbeticket;

/**
 * Created by Ahmed Dongl on 9/1/2015.
 */
public class Ticket
{
    public String number;
    public String date;
    public String expire_date;
    public String branch_name ;

    public Ticket(String branch_name,String expire_date,String date,String number)
    {
        super();
        this.branch_name=branch_name;
        this.expire_date=expire_date;
        this.date=date;
        this.number=number;
    }

}
