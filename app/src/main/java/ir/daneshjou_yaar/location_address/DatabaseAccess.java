package ir.daneshjou_yaar.location_address;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iqfarhad on 1/1/2018.
 */

public class DatabaseAccess {
    private static final String TAG = "DatabaseAccess";


    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    /**
     * Private constructor to avoid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DatabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    public List<Object_model> getAllinfo(String choosed ,String searchkey) {
        List<Object_model> list = new ArrayList<Object_model>();
        Cursor cursor;
       if (choosed.equalsIgnoreCase("*")){
               cursor = database.rawQuery("SELECT * FROM " +
                      "( SELECT * FROM hospital" +
                      " UNION " +
                      "SELECT * FROM banks  " +
                      "UNION " +
                      "SELECT * FROM restroom " +
                      "UNION " +
                      "SELECT * FROM shops " +
                       "UNION " +
                       "SELECT * FROM etc " +
                       "UNION " +
                       "SELECT * FROM house )" +
                      " UNION SELECT * FROM" +
                      "( SELECT * FROM offical " +
                      "UNION" +
                      " SELECT * FROM water " +
                      "UNION" +
                      " SELECT * FROM resturant " +
                      "UNION " +
                      "SELECT * FROM bookshop " +
                       "UNION " +
                       "SELECT * FROM taxi )" , null);

        }else if (choosed.equalsIgnoreCase("search")){
           cursor = database.rawQuery( "SELECT * FROM " +
                   "( SELECT * FROM " +
                   "( SELECT * FROM hospital" +
                   " UNION " +
                   "SELECT * FROM banks  " +
                   "UNION " +
                   "SELECT * FROM restroom " +
                   "UNION " +
                   "SELECT * FROM shops " +
                   "UNION " +
                   "SELECT * FROM etc " +
                   "UNION " +
                   "SELECT * FROM house )" +
                   " UNION SELECT * FROM" +
                   "( SELECT * FROM offical " +
                   "UNION" +
                   " SELECT * FROM water " +
                   "UNION" +
                   " SELECT * FROM resturant " +
                   "UNION " +
                   "SELECT * FROM bookshop " +
                   "UNION " +
                   "SELECT * FROM taxi ) ) WHERE name = '"+ searchkey +"'" ,null);
       }
        else{
        cursor = database.rawQuery("SELECT * FROM "+ choosed  , null);
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            Object_model model = new Object_model();
            model.setId(cursor.getString(0));
            model.setName(cursor.getString(1));
            Log.d("getall", "getAllinfo: "+cursor.getString(1).toString());
            model.setAddress(cursor.getString(2));
            model.setInfo(cursor.getString(3));
            model.setCategory(cursor.getString(4));
            model.setImg(cursor.getString(5));
            model.setLongitude(cursor.getString(6));
            model.setLatitude(cursor.getString(7));

            list.add(model);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }


    public void setNewJob(List<Object_model> new_jobs ) {

        Cursor cursor;
        for (Object_model a: new_jobs) {
            String row_one = "INSERT INTO "+ a.getCategory() +" (id , name , address , info , category , img , longitude , latitude ) VALUES ('"+ a.getId() +"' , '" + a.getName() +"' , '" + a.getAddress() +"' , '" + a.getInfo() +"' , '" + a.getCategory() +"' , '" + a.getImg() +"' , '" + a.getLongitude() +"' , '" + a.getLatitude() +"' )" ;
            Log.d(TAG, "setNewJob: "+ row_one);
            database.execSQL(row_one);
            //cursor = database.rawQuery("INSERT INTO "+ a.getCategory() +" (id , name , address , info , category , img , longitude , latitude ) VALUES ('"+ a.getId() +"' , '" + a.getName() +"' , '" + a.getAddress() +"' , '" + a.getInfo() +"' , '" + a.getCategory() +"' , '" + a.getImg() +"' , '" + a.getLongitude() +"' , '" + a.getLatitude() +"'" , null);
        }


    }
}
