package com.example.loghours;

import com.example.loghours.EmployersContract.Employers;
import com.example.loghours.LogsContract.Logs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LogsDbHelper extends SQLiteOpenHelper {
	private static final String TEXT_TYPE = " TEXT";
	//private static final String INTEGER_TYPE = " INTEGER";
	private static final String COMMA_SEP = ", ";
	private static final String SQL_CREATE_LOGS_ENTRIES =
			"CREATE TABLE " + Logs.TABLE_NAME + " (" +
				    Logs._ID + " INTEGER PRIMARY KEY, " +
				    Logs.COLUMN_NAME_START_TIME + TEXT_TYPE + COMMA_SEP +
				    Logs.COLUMN_NAME_STOP_TIME + TEXT_TYPE + COMMA_SEP +
				    Logs.COLUMN_NAME_EMPLOYER + TEXT_TYPE +
				    ")";
	    
	private static final String SQL_CREATE_EMPLOYERS_ENTRIES =
			"CREATE TABLE " + Employers.TABLE_NAME + " (" +
				    Employers._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				    Employers.COLUMN_NAME_TITLE + TEXT_TYPE + " NOT NULL UNIQUE" +
				    ")";
	    

	private static final String SQL_DELETE_LOGS_ENTRIES =
		    "DROP TABLE IF EXISTS " + Logs.TABLE_NAME;
	
	private static final String SQL_DELETE_EMPLOYERS_ENTRIES =
		    " DROP TABLE IF EXISTS " + Employers.TABLE_NAME;

	// If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 9;
    public static final String DATABASE_NAME = "Logs.db";
    
    public LogsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_LOGS_ENTRIES);
		db.execSQL(SQL_CREATE_EMPLOYERS_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_LOGS_ENTRIES);
        db.execSQL(SQL_DELETE_EMPLOYERS_ENTRIES);
        onCreate(db);

	}
	
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
