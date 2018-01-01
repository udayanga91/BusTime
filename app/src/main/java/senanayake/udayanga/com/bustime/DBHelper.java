package senanayake.udayanga.com.bustime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Udayanga on 12/10/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int databaseVersion = 1;
    private static final String databaseName = "bus_time";
    private static final String tableName = "bus_time";

    private static final String keyId = "id";
    private static final String keyAddedPlace = "added_place";
    private static final String keyFrom = "from";
    private static final String keyTo = "to";
    private static final String keyRoute = "route";
    private static final String keyTime = "time";

    private static final String TAG = "DBHelper";

    public DBHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "Creating table...");
        String createTable = "CREATE TABLE " + tableName + "(" +
                keyId + " INTEGER PRIMARY KEY " +
                keyAddedPlace + " TEXT " +
                keyFrom + " TEXT " +
                keyTo + " TEXT " +
                keyRoute + " INTEGER)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(sqLiteDatabase);
    }

    public void addRoute(Route route) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(keyAddedPlace, route.getPlaceAdded());
        values.put(keyFrom, route.getFrom());
        values.put(keyTo, route.getTo());
        values.put(keyRoute, route.getRouteNo());
        values.put(keyTime, route.getTime());
        database.insert(tableName, null, values);
        database.close();
    }

    public Route getRoute(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(tableName, new String[]{keyAddedPlace,
                        keyFrom, keyTo, keyRoute, keyTime}, keyId + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Route route = new Route(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(3),
                cursor.getString(4),
                Integer.parseInt(cursor.getString(5)),
                cursor.getString(6));
        return route;
    }

    public ArrayList<HashMap<String, String>> getAllRoutes() {
        ArrayList<HashMap<String, String>> routeList;
        routeList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM " + tableName;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(keyId, cursor.getString(0));
                map.put(keyAddedPlace, cursor.getString(1));
                map.put(keyFrom, cursor.getString(2));
                map.put(keyTo, cursor.getString(3));
                map.put(keyRoute, cursor.getString(4));
                map.put(keyTime, cursor.getString(5));

                routeList.add(map);

            } while (cursor.moveToNext());

        }
        return routeList;
    }



    public int updateRoute(Route route) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(keyId, route.getId());
        values.put(keyAddedPlace, route.getPlaceAdded());
        values.put(keyFrom, route.getFrom());
        values.put(keyTo, route.getTo());
        values.put(keyRoute, route.getRouteNo());
        values.put(keyTime, route.getTime());

        // updating row
        return db.update(tableName, values, keyId + " = ?",
                new String[]{String.valueOf(route.getId())});
    }

    public void deleteContact(Route route) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, keyId + " = ?",
                new String[]{String.valueOf(route.getId())});
        db.close();
    }
}