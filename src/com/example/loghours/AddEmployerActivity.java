package com.example.loghours;

import java.util.ArrayList;
import java.util.List;

import com.example.loghours.EmployersContract.Employers;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddEmployerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_employer);
		
		
        loadSpinner();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add_employer, menu);
		return true;
	}
	
	/** Called when the user clicks the Add button */
    public void addEmployer(View view) {
    	// get employer title
    	EditText editText = (EditText) findViewById(R.id.add_employer_text);
    	String employer = editText.getText().toString();
    	
    	// put entry in database
    	
    	// initiate helper
    	LogsDbHelper mDbHelper = new LogsDbHelper(getBaseContext());
    	
    	// Gets the data repository in write mode
    	SQLiteDatabase db = mDbHelper.getWritableDatabase();

    	// Create a new map of values, where column names are the keys
    	ContentValues values = new ContentValues();
    	values.put(Employers.COLUMN_NAME_TITLE, employer);

    	// Insert the new row, returning the primary key value of the new row
    	try
    	{
	    	db.insert(
	    	         Employers.TABLE_NAME,
	    	         null,
	    	         values);
	    	
    	}
    	catch(SQLiteException e)
    	{
    		Toast.makeText(getBaseContext(), "Must be unique", Toast.LENGTH_LONG).show();
    		e.printStackTrace();
    	}
    	db.close();
    	loadSpinner();
    }
    
    public void backToMain(View view) {
    	Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
    }
    
    /** Called when the user clicks the Remove button */
    public void removeEmployer(View view) {
    	// Get employer to remove
    	Spinner employers_spinner =  (Spinner) findViewById(R.id.remove_employers_spinner);
    	String employer = String.valueOf(employers_spinner.getSelectedItem());
    	
    	// remove entry in database
    	
    	// initiate helper
    	LogsDbHelper mDbHelper = new LogsDbHelper(getBaseContext());
    	
    	// Gets the data repository in write mode
    	SQLiteDatabase db = mDbHelper.getWritableDatabase();

    	// Define 'where' part of query.
    	String selection = Employers.COLUMN_NAME_TITLE + " LIKE ?";
    	
    	// Specify arguments in placeholder order.
    	String[] selectionArgs = { employer };
    	
    	// Issue SQL statement.
    	db.delete(Employers.TABLE_NAME, selection, selectionArgs);
    	db.close();
    	loadSpinner();
    }
    
    private void loadSpinner() {
    	// get employers from database
		List<String> employers = new ArrayList<String>();
		
		// initiate helper
    	LogsDbHelper mDbHelper = new LogsDbHelper(getBaseContext());
    	
    	// Gets the data repository in write mode
    	SQLiteDatabase db = mDbHelper.getReadableDatabase();
    	
    	// Select All Query
        String selectQuery = "SELECT  * FROM " + Employers.TABLE_NAME;
    	Cursor cursor = db.rawQuery(selectQuery, null);
        
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	employers.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
         
        // closing connection
        cursor.close();
        db.close();
    	        
    	Spinner spinner;
		spinner = (Spinner) findViewById(R.id.remove_employers_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, employers);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
    }

}
