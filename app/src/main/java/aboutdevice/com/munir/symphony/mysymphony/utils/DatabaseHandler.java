package aboutdevice.com.munir.symphony.mysymphony.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import aboutdevice.com.munir.symphony.mysymphony.model.NotificationStore;

/**
 * Created by admin on 1/31/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "AboutDeviceManager";

    // Contacts table name
    private static final String TABLE_NOTIFICATIONS = "notifications";

    // Contacts Table Columns names
    public static final String key_id = "key_id";
    private static final String notification_title = "notification_title";
    private static  final String notification_content = "notification_content";
    public static final  String activityToBeOpened = "activitytobeopened";
    public static final String model_sw_version = "model_sw_version";
    private static  final String t = "t";
    private static  final String b = "b";
    private static  final String link = "link";
    private static  final String image_url = "image_url";
    private static  final String insertion_date = "insertion_date";
    public static  final String notification_id = "notification_id";
    public static  final String notification_type = "notification_type";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTIFICATION_TABLE = "CREATE TABLE " + TABLE_NOTIFICATIONS + " ( "
                +key_id+ " INTEGER PRIMARY KEY, " + notification_title +" TEXT, "
                + notification_content +" TEXT, " + activityToBeOpened +" TEXT, "
                + model_sw_version +" TEXT, " + t +" TEXT, " + b +" TEXT, "
                + link +" TEXT, " + image_url +" TEXT, " + insertion_date +" TEXT, "  + notification_id +" TEXT, " + notification_type +" TEXT " + " )";
        db.execSQL(CREATE_NOTIFICATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);

        // Create tables again
        onCreate(db);
    }


    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    //addNotifications
    //Adding Notifications
    public void addNotification(NotificationStore notificationStore){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(notification_title,notificationStore.getNotification_title());
        contentValues.put(notification_content,notificationStore.getNotification_content());
        contentValues.put(activityToBeOpened,notificationStore.getActivityToBeOpened());
        contentValues.put(model_sw_version,notificationStore.getModel_sw_version());
        contentValues.put(t,notificationStore.getT());
        contentValues.put(b,notificationStore.getB());
        contentValues.put(link,notificationStore.getLink());
        contentValues.put(image_url,notificationStore.getImage_url());
        contentValues.put(insertion_date,notificationStore.getInsertion_date());
        contentValues.put(notification_id,notificationStore.getNotification_id());
        contentValues.put(notification_type,notificationStore.getNotification_type());

        db.insert(TABLE_NOTIFICATIONS, null, contentValues);
        db.close();
    }

    public List<NotificationStore> getAllNotifications(){
        List<NotificationStore> notificationList = new ArrayList<NotificationStore>();
        String selectQuery = "SELECT * FROM " +TABLE_NOTIFICATIONS;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if(cursor.moveToFirst()){
            do{
                NotificationStore notificationStore = new NotificationStore();
                notificationStore.setId(Integer.parseInt(cursor.getString(0)));
                notificationStore.setNotification_title(cursor.getString(1));
                notificationStore.setNotification_content(cursor.getString(2));
                notificationStore.setActivityToBeOpened(cursor.getString(3));
                notificationStore.setModel_sw_version(cursor.getString(4));
                notificationStore.setT(cursor.getString(5));
                notificationStore.setB(cursor.getString(6));
                notificationStore.setLink(cursor.getString(7));
                notificationStore.setImage_url(cursor.getString(8));
                notificationStore.setInsertion_date(cursor.getString(9));
                notificationStore.setNotification_id(cursor.getString(10));
                notificationStore.setNotification_type(cursor.getString(11));

                notificationList.add(notificationStore);
            }while(cursor.moveToNext());
        }

        return notificationList;
    }

    // Getting contacts Count
    public long getNotificationCount() {
        /*String countQuery = "SELECT  * FROM " + TABLE_NOTIFICATIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();*/
        SQLiteDatabase db = this.getReadableDatabase();
        long numRows = DatabaseUtils.queryNumEntries(db, TABLE_NOTIFICATIONS);

        return numRows;
    }

    public void deleteAll(NotificationStore notificationStore){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NOTIFICATIONS);
    }

    public boolean CheckIsDataAlreadyInDBorNot(String dbfield, String fieldValue) {
        SQLiteDatabase db = getReadableDatabase();
        String Query = "Select * from  " + TABLE_NOTIFICATIONS + " where " + dbfield + " = " + "'"+ fieldValue + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }

    public List<NotificationStore> getRecord(String dbfield, int fieldValue) {
        List<NotificationStore> notificationList = new ArrayList<NotificationStore>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "Select * from  " + TABLE_NOTIFICATIONS + " where " + dbfield + " = " + fieldValue;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if(cursor.moveToFirst()){
            do{
                NotificationStore notificationStore = new NotificationStore();
                notificationStore.setId(Integer.parseInt(cursor.getString(0)));
                notificationStore.setNotification_title(cursor.getString(1));
                notificationStore.setNotification_content(cursor.getString(2));
                notificationStore.setActivityToBeOpened(cursor.getString(3));
                notificationStore.setModel_sw_version(cursor.getString(4));
                notificationStore.setT(cursor.getString(5));
                notificationStore.setB(cursor.getString(6));
                notificationStore.setLink(cursor.getString(7));
                notificationStore.setImage_url(cursor.getString(8));
                notificationStore.setInsertion_date(cursor.getString(9));
                notificationStore.setNotification_id(cursor.getString(10));
                notificationStore.setNotification_type(cursor.getString(11));

                notificationList.add(notificationStore);
            }while(cursor.moveToNext());
        }

        return notificationList;
    }

    public int getMinRowId(){
        int rowid = 1;
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT MIN(" + key_id +") FROM " + TABLE_NOTIFICATIONS ;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null){
            cursor.moveToFirst();
            rowid = Integer.parseInt(cursor.getString(0));
        }

        return rowid;
    }

    public void deleteRows(List rewsToDeleteArray){
       // String idCSV = TextUtils.join("," , rewsToDeleteArray);
        String idCSV = TextUtils.join("," , rewsToDeleteArray);
        SQLiteDatabase db = getWritableDatabase();
        if(db != null){
            db.delete(TABLE_NOTIFICATIONS, " key_id " + " IN ( " +idCSV +" )" , null);
            db.close();
        }
    }

    public List<NotificationStore> getAllNotificationKeys(){
        List<NotificationStore> notificationList = new ArrayList<NotificationStore>();
        String selectQuery = "SELECT " + key_id + " FROM " +TABLE_NOTIFICATIONS;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if(cursor.moveToFirst()){
            do{
                NotificationStore notificationStore = new NotificationStore();
                notificationStore.setId(Integer.parseInt(cursor.getString(0)));

                notificationList.add(notificationStore);
            }while(cursor.moveToNext());
        }

        return notificationList;
    }
}
