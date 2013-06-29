package com.example.loghours;

import java.util.ArrayList;
import java.util.List;

import com.example.loghours.EmployersContract.Employers;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
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
		
		
        loadSpinner(this);
		
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
    	
    	DbFunctions entry = new DbFunctions(this);//AddEmployerActivity.
    	entry.open();
    	
		long row = entry.insertEmployer(employer);
		
		if (row == -1)
    	{
    		Toast.makeText(this, "Please enter unique value", Toast.LENGTH_LONG).show();
    	}
		
		entry.close();
    	loadSpinner(this);
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
    	DbFunctions entry = new DbFunctions(this);//AddEmployerActivity.
    	entry.open();
    	entry.removeEmployerFromDb(employer);
    	entry.close();
    	loadSpinner(this);
    }
    
    private void loadSpinner(Context c) {
    	// get employers from database
    	DbFunctions entry = new DbFunctions(c);//AddEmployerActivity.
    	entry.open();
    	List<String> employers;
    	
    	employers = entry.getAllEmployers();
    	entry.close();
    	
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
