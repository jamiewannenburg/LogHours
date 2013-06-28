package com.example.loghours;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.loghours.LogsContract.Logs;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;

public class StopLoggerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stop_logger);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_stop_logger, menu);
		return true;
	}
	
	/** Called when the user clicks the Stop button */
    public void stopHourLogger(View view) {
    	String dateTimeStamp = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.US).format(new Date());
    	Intent get_intent = getIntent();
		String row_id = get_intent.getStringExtra(MainActivity.ROW_ID);
		
    	// update database
    	
    	// initiate helper
    	LogsDbHelper mDbHelper = new LogsDbHelper(getBaseContext());
    	
    	// Gets the data repository in read mode
    	SQLiteDatabase db = mDbHelper.getReadableDatabase();
    	
    	// New value for one column
    	ContentValues values = new ContentValues();
    	values.put(Logs.COLUMN_NAME_STOP_TIME, dateTimeStamp);
    	
    	// Which row to update, based on the ID
    	String selection = Logs._ID + " = ?";
    	String[] selectionArgs = { String.valueOf(row_id) };
    	try
    	{
	    	Integer count = db.update(
	    		Logs.TABLE_NAME,
	    	    values,
	    	    selection,
	    	    selectionArgs);
	    	if (count!=1){
	    		throw new NoSuchFieldException();
	    	}
    	}
    	catch(NoSuchFieldException e)
        {
            e.printStackTrace();
        } 
    	
    	db.close();
    	// start start activity
    	Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
    }
    

}
