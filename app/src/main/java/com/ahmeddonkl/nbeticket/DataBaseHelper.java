package com.ahmeddonkl.nbeticket;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ahmed Dongl on 8/31/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.ahmeddonkl.nbeticket/databases/";

    private static String DB_NAME = "nbe.sqlite";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    //to take list of Government
    public static List<String> Gov_List;

    //to take list of Branches Based on Gov
    public static List<String> Branch_List ;

    //saved Gov and it's id
    public static Map<String,String> Governments;

    //saved Branch and it's id
    public static Map<String,String> Branches;

    //saved Nearest and it's id
    public static Map<String,String> Nearest;

    //Append All Branches that selected
    public static Map<String,String> All = new HashMap<>();

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            File file = new File(myPath);
            if (file.exists() && !file.isDirectory())
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    //
    public void getAllGov()
    {
        //initialize Gov map
        Governments = new HashMap();

        //initialize list
        Gov_List=new ArrayList<>();

        //Select Query
        String selectQuery = "SELECT _id,EngProvince FROM BranchesID" ;

        Cursor cursor = myDataBase.rawQuery(selectQuery,null);

        int id_index=cursor.getColumnIndex("_id");
        int provider_index=cursor.getColumnIndex("EngProvince");

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {

            Gov_List.add(cursor.getString(provider_index));
            Governments.put(cursor.getString(provider_index), cursor.getString(id_index));
            All.put(cursor.getString(id_index),cursor.getString(provider_index));
            cursor.moveToNext();
        }

        // make sure to close the cursor
        cursor.close();
    }

    public void getBranchByCov(String cov)
    {
        //initialize branch map
        Branches = new HashMap();

        //initialize list
        Branch_List=new ArrayList<>();

        //Select Query
        String selectQuery = "SELECT _id,BranchName FROM Branches WHERE Branch_Cov_ID="+cov ;
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);

        int id_index=cursor.getColumnIndex("_id");
        int provider_index=cursor.getColumnIndex("BranchName");

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            All.put(cursor.getString(id_index),cursor.getString(provider_index));
            Branch_List.add(cursor.getString(provider_index));
            Branches.put(cursor.getString(provider_index),cursor.getString(id_index));
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
    }

    public List<String> getNearest_Branches(double current_lat,double current_lng)
    {
        //initialize branch map
        Nearest = new HashMap();

        //initialize list
        List<String> nearest_list=new ArrayList<>();

        //Select Query
        String selectQuery = "SELECT _id,BranchName,lat,Lng FROM Branches " ;
        Cursor cursor = myDataBase.rawQuery(selectQuery,null);

        int id_index=cursor.getColumnIndex("_id");
        int provider_index=cursor.getColumnIndex("BranchName");
        int lat_index=cursor.getColumnIndex("lat");
        int lng_index=cursor.getColumnIndex("Lng");

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            double lat = cursor.getDouble(lat_index);
            double lng = cursor.getDouble(lng_index);
            if(distance(lat,current_lat,lng,current_lng,0,0) <= 1000.0)
            {
                nearest_list.add(cursor.getString(provider_index));
                Nearest.put(cursor.getString(provider_index), cursor.getString(id_index));
            }

            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return nearest_list;
    }

    /*
 * Calculate distance between two points in latitude and longitude taking
 * into account height difference. If you are not interested in height
 * difference pass 0.0. Uses Haversine method as its base.
 *
 * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
 * el2 End altitude in meters
 * @returns Distance in Meters
 */
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

}