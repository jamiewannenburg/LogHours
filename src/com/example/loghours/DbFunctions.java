package com.example.loghours;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.loghours.EmployersContract.Employers;
import com.example.loghours.LogsContract.Logs;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class DbFunctions {
	private final Context ourContext;
	private LogsDbHelper mDbHelper;
	private SQLiteDatabase db;
	
	public DbFunctions open(){
		// initiate helper
		mDbHelper = new LogsDbHelper(ourContext);
		// Gets the data repository in write mode
		db = mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		try
		{
			mDbHelper.close();
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
	}
	
	public int insertStopTime(String dateTimeStamp,String row_id){
		// New value for one column
    	ContentValues values = new ContentValues();
    	values.put(Logs.COLUMN_NAME_STOP_TIME, dateTimeStamp);
    	
    	// Which row to update, based on the ID
    	String selection = Logs._ID + " = ?";
    	String[] selectionArgs = { String.valueOf(row_id) };
    	
    	Integer count = db.update(
    		Logs.TABLE_NAME,
    	    values,
    	    selection,
    	    selectionArgs);
    	if (count!=1){
    		Toast.makeText(ourContext, "Could not find start time", Toast.LENGTH_LONG).show();
    	}
    	return count;
	}
	
	public long insertStartTime(String dateTimeStamp, String employer) {
		
    	// Create a new map of values, where column names are the keys
    	ContentValues values = new ContentValues();
    	values.put(Logs.COLUMN_NAME_EMPLOYER, employer);
    	values.put(Logs.COLUMN_NAME_START_TIME, dateTimeStamp);

    	// Insert the new row, returning the primary key value of the new row
    	long newRowId;
    	try
    	{
	    	newRowId = db.insert(
	    	         Logs.TABLE_NAME,
	    	         null,
	    	         values);
    	}
    	catch(NullPointerException e)
    	{
    		e.printStackTrace();
    		newRowId = -1;
    	}
    	return newRowId;
	}
	public long insertEmployer(String employer){

    	// Create a new map of values, where column names are the keys
    	ContentValues values = new ContentValues();
    	values.put(Employers.COLUMN_NAME_TITLE, employer);

    	// Insert the new row, returning the primary key value of the new row
    	long row = db.insert(
   	         Employers.TABLE_NAME,
   	         null,
   	         values);
    	return row;
	}
	
	public long removeEmployerFromDb(String employer){
		// Define 'where' part of query.
    	String selection = Employers.COLUMN_NAME_TITLE + " LIKE ?";
    	
    	// Specify arguments in placeholder order.
    	String[] selectionArgs = { employer };
    	
    	// Issue SQL statement.
    	return db.delete(Employers.TABLE_NAME, selection, selectionArgs);
	}
	
	public String getEmployerLogs(String employer, String thisMonth){
		
		// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    	    Logs._ID,
    	    Logs.COLUMN_NAME_START_TIME,
    	    Logs.COLUMN_NAME_STOP_TIME
    	    };
    	
    	String selection = Logs.COLUMN_NAME_EMPLOYER + " LIKE ? AND " +
    						"strftime('%Y-%m'," + Logs.COLUMN_NAME_START_TIME + ") LIKE ?";
    	String[] selectionArgs = { String.valueOf(employer), String.valueOf(thisMonth) };
    	
    	// How you want the results sorted in the resulting Cursor
    	String sortOrder =
    		Logs.COLUMN_NAME_START_TIME + " DESC";

    	Cursor c = db.query(
    		Logs.TABLE_NAME,  // The table to query
    	    projection,                               // The columns to return
    	    selection,                                // The columns for the WHERE clause
    	    selectionArgs,                            // The values for the WHERE clause
    	    null,                                     // don't group the rows
    	    null,                                     // don't filter by row groups
    	    sortOrder                                 // The sort order
    	    );
    	
    	String file_content = "";
    	if (c.moveToFirst())
    	{
    		c.moveToFirst();
	    	do {
	    		String start_time = c.getString(c.getColumnIndexOrThrow(Logs.COLUMN_NAME_START_TIME));
	        	String stop_time = c.getString(c.getColumnIndexOrThrow(Logs.COLUMN_NAME_STOP_TIME));
	        	file_content += start_time + "," + stop_time + "\n";
	    	} while (c.move(1));
    	}
    	else
    	{
    		Toast.makeText(ourContext, "No logs for this employer yet", Toast.LENGTH_LONG).show();
    	}
    	return file_content;
	}
	
	public List<String> getAllEmployers(){
		List<String> employers = new ArrayList<String>();
		    	
    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    		Employers._ID,
    	    Employers.COLUMN_NAME_TITLE
    	    };
    	
    	Cursor cursor;
    	try
    	{
    		cursor = db.query(
	    		Employers.TABLE_NAME,  // The table to query
	    	    projection,                               // The columns to return
	    	    null,                                // The columns for the WHERE clause
	    	    null,                            // The values for the WHERE clause
	    	    null,                                     // don't group the rows
	    	    null,                                     // don't filter by row groups
	    	    null                                 // The sort order
	    	    );
    	}
    	catch(NullPointerException e)
    	{
    		cursor = null;
    	}
        
        // looping through all rows and adding to list
    	if (cursor != null){
	        if (cursor.moveToFirst()) {
	            do {
	            	employers.add(cursor.getString(cursor.getColumnIndex(Employers.COLUMN_NAME_TITLE)));
	            } while (cursor.moveToNext());
	        }
	        else {
	        	//employers.add("Default");
	        }
	        // closing connection
	        cursor.close();
    	}
    	else {
    		//employers.add("Default");
    	}
    	if (employers.isEmpty()) {
    		Toast.makeText(ourContext, "Employers is empty", Toast.LENGTH_LONG).show();
    	}
    	return employers;
	}
	
	public DbFunctions(Context c){
		ourContext = c;
	}

}
