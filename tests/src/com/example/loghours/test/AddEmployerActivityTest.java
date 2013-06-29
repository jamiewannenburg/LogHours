package com.example.loghours.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.loghours.AddEmployerActivity;

public class AddEmployerActivityTest extends
		ActivityInstrumentationTestCase2<AddEmployerActivity> {
	
	private AddEmployerActivity mActivity;
	private Spinner mSpinner;
	private SpinnerAdapter mPlanetData;
	  
	public AddEmployerActivityTest() {
	    super("com.example.loghours", AddEmployerActivity.class);
	  } // end of SpinnerActivityTest constructor definition
	@Override
	  protected void setUp() throws Exception {
	    super.setUp();

	    setActivityInitialTouchMode(false);

	    mActivity = getActivity();

	    mSpinner = (Spinner) mActivity.findViewById(
	    		  com.example.loghours.R.id.remove_employers_spinner
	      );

	      mPlanetData = mSpinner.getAdapter();

	  } // end of setUp() method definition
}
