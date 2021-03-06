package com.gnorsilva.android.myfridge.provider;

import com.gnorsilva.android.myfridge.provider.MyFridgeContract.HistoryColumns;
import com.gnorsilva.android.myfridge.provider.MyFridgeContract.FridgeColumns;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class MyFridgeDatabase extends SQLiteOpenHelper{
	private static final String TAG = "MyFridgeDatabase";
    private static final String DATABASE_NAME = "myfridge.db";
    private static final int DATABASE_VERSION = 1;
    
    interface Tables{
    	String FRIDGE = "fridge";
    	String HISTORY = "history";
    }
    
    public MyFridgeDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "Creating database tables");
		
		db.execSQL("CREATE TABLE "+ Tables.FRIDGE +" ( " +
					BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					FridgeColumns.NAME + " TEXT NOT NULL, "+
					FridgeColumns.QUANTITY + " INTEGER NOT NULL, "+
					FridgeColumns.USE_BY_DATE + " TEXT NOT NULL, "+
					FridgeColumns.BARCODE + " TEXT, "+
					FridgeColumns.BARCODE_FORMAT + " TEXT );");
		
		db.execSQL("CREATE TABLE "+ Tables.HISTORY +" ( " +
					BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				 	HistoryColumns.NAME + " TEXT NOT NULL,"+
				 	HistoryColumns.TIMES_ADDED + " TEXT NOT NULL,"+
				 	HistoryColumns.TOTAL_QUANTITY + " INTEGER NOT NULL,"+
				 	HistoryColumns.BARCODE + " TEXT,"+
				 	HistoryColumns.BARCODE_FORMAT + " TEXT );");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "Dropping and updating database tables");
		
		// TODO find the correct procedure for database update
		db.execSQL("DROP TABLE IF EXISTS " + Tables.FRIDGE);
		db.execSQL("DROP TABLE IF EXISTS " + Tables.HISTORY);

		onCreate(db);
	}
	
}
