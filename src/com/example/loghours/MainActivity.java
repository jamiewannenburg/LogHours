package com.example.loghours;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.loghours.EmployersContract.Employers;
import com.example.loghours.LogsContract.Logs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	public final static String FILE = "com.example.loghours.MESSAGE";
	public final static String ROW_ID = "com.example.loghours.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// first make the view
		setContentView(R.layout.activity_main);
		
		// Get previous value and position from shared_preferences
		SharedPreferences sharedPref = getBaseContext().getSharedPreferences(
				getString(R.string.preference_file_key),Context.MODE_PRIVATE);
		String previous_employer = sharedPref.getString(getString(R.string.empoyer_selector_value), "");
		
		// Load previous month
		String last_month = sharedPref.getString(getString(R.string.last_month), "");
		String this_month = new SimpleDateFormat("MM", Locale.US).format(new Date());
		if (!last_month.equals(this_month)){
			Toast.makeText(getBaseContext(), "The month has changed, make sure all data is sent.", Toast.LENGTH_LONG).show();
		}
		
		// Save current month
		SharedPreferences.Editor editor = sharedPref.edit();
    	editor.putString(getString(R.string.last_month),this_month);
    	editor.commit();
		
		// get employers from database
		List<String> employers = new ArrayList<String>();
		
		// initiate helper
    	LogsDbHelper mDbHelper = new LogsDbHelper(getBaseContext());
    	
    	// Gets the data repository in read mode
    	SQLiteDatabase db = mDbHelper.getReadableDatabase();
    	
    	// Select All Query
        String selectQuery = "SELECT  * FROM " + Employers.TABLE_NAME;
    	Cursor cursor = db.rawQuery(selectQuery, null);
        
        // looping through all rows and adding to list, also check if previous selector is still there
    	int count = 0;
    	int position = 0;
        if (cursor.moveToFirst()) {
            do {
            	String employer = cursor.getString(cursor.getColumnIndex(String.valueOf(Employers.COLUMN_NAME_TITLE)));
            	employers.add(employer);
            	if (employer.equals(previous_employer)) {
            		position = count;
            	}
            	count=count+1;
            } while (cursor.moveToNext());
        }
        else
        {
        	Toast.makeText(getBaseContext(), "No employers yet", Toast.LENGTH_LONG).show();
        	Intent intent = new Intent(this, AddEmployerActivity.class);
	    	startActivity(intent);
        }
        
         
        // closing connection
        cursor.close();
        db.close();
		
		Spinner spinner;
		spinner = (Spinner) findViewById(R.id.employers_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, employers);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		
		// set position
		spinner.setSelection(position);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		
		switch (item.getItemId())
		{
		case R.id.menu_add_employer:
			// Single menu item is selected do something
            // Ex: launching new activity/screen or show alert message
			Intent intent = new Intent(this, AddEmployerActivity.class);
	    	startActivity(intent);
	    	return true;
	    	
	    default:
	    	return super.onOptionsItemSelected(item);	
		}
	}
	
	/** Called when the user clicks the Start button */
    public void startHourLogger(View view) {
    	// Get employer and times
    	String dateTimeStamp = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.US).format(new Date());
    	Spinner employers_spinner =  (Spinner) findViewById(R.id.employers_spinner);
    	String employer = String.valueOf(employers_spinner.getSelectedItem());
    	
    	// save option
    	SharedPreferences sharedPref = getBaseContext().getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
    	SharedPreferences.Editor editor = sharedPref.edit();
    	editor.putString(getString(R.string.empoyer_selector_value), employer);
    	editor.commit();
    	
    	// put entry in database
    	
    	// initiate helper
    	LogsDbHelper mDbHelper = new LogsDbHelper(getBaseContext());
    	
    	// Gets the data repository in write mode
    	SQLiteDatabase db = mDbHelper.getWritableDatabase();

    	// Create a new map of values, where column names are the keys
    	ContentValues values = new ContentValues();
    	values.put(Logs.COLUMN_NAME_EMPLOYER, employer);
    	values.put(Logs.COLUMN_NAME_START_TIME, dateTimeStamp);

    	// Insert the new row, returning the primary key value of the new row
    	long newRowId;
    	newRowId = db.insert(
    	         Logs.TABLE_NAME,
    	         null,
    	         values);
    	db.close();
    	
    	// start stop logger activity
    	Intent intent = new Intent(this, StopLoggerActivity.class);
    	//intent.putExtra(EMPLOYER, employer);
    	intent.putExtra(ROW_ID, String.valueOf(newRowId));
    	startActivity(intent);
    }
    
    
    /** Called when the user clicks the Upload to Drive button */
    public void uploadToDrive(View view) {
    	// get month and employer
    	String yearMonth = new SimpleDateFormat("yyyy MMMM", Locale.US).format(new Date());
    	String thisMonth = new SimpleDateFormat("yyyy-MM", Locale.US).format(new Date());
    	Spinner employers_spinner =  (Spinner) findViewById(R.id.employers_spinner);
    	String employer = String.valueOf(employers_spinner.getSelectedItem());
    	
    	// get data from database
    	
    	// initiate helper
    	LogsDbHelper mDbHelper = new LogsDbHelper(getBaseContext());
    	
    	// Gets the data repository in write mode
    	SQLiteDatabase db = mDbHelper.getReadableDatabase();

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
    	
    	if (c.moveToFirst())
    	{
    		// write to csv file
        	File file = generateCsvFile("Hours " + employer + " "+ yearMonth +".csv",c);
        	c.close();
        	db.close();
        	// start drive activity
        	Intent intent = new Intent(this, DriveActivity.class);
        	intent.putExtra(FILE, String.valueOf(file));
        	startActivity(intent);
    	}
    	else
    	{
    		Toast.makeText(getBaseContext(), "No logs for this employer yet", Toast.LENGTH_LONG).show();
    		c.close();
        	db.close();
    	}
    	
    	
    }
    
    private File generateCsvFile(String sFileName, Cursor c)
    {
    	
    	try
    	{
	    	File root = getExternalFilesDir(null);
	        File gpxfile = new File(root, sFileName);
	        FileWriter writer = new FileWriter(gpxfile, true);
	        
	    	c.moveToFirst();
        	String file_content = "";
	    	do {
	    		String start_time = c.getString(c.getColumnIndexOrThrow(Logs.COLUMN_NAME_START_TIME));
	        	String stop_time = c.getString(c.getColumnIndexOrThrow(Logs.COLUMN_NAME_STOP_TIME));
	        	file_content += start_time + "," + stop_time + "\n";
	    	} while (c.move(1));
	    	writer.write(file_content);
	    	writer.flush();
            writer.close();
            return gpxfile;
    	}
        catch(IOException e)
        {
        	Toast.makeText(getBaseContext(), "Could not find a floder to write in", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;
        }
     }

}
