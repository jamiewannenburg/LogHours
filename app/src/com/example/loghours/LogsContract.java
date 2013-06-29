package com.example.loghours;

import android.provider.BaseColumns;
import com.jamiewannenburg.loghours.R;

public final class LogsContract {
	// To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public LogsContract() {}

    /* Inner class that defines the table contents */
    public static abstract class Logs implements BaseColumns {
        public static final String TABLE_NAME = "logs";
        public static final String COLUMN_NAME_START_TIME = "start_time";
        public static final String COLUMN_NAME_STOP_TIME = "stop_time";
        public static final String COLUMN_NAME_EMPLOYER = "employer";
    }
}
