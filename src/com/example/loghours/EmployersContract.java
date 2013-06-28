package com.example.loghours;

import android.provider.BaseColumns;

public final class EmployersContract {
	// To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public EmployersContract() {}

    /* Inner class that defines the table contents */
    public static abstract class Employers implements BaseColumns {
        public static final String TABLE_NAME = "employers";
        public static final String COLUMN_NAME_TITLE = "title";
    }
}
