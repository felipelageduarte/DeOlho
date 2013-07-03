package br.usp.icmc.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager {
	private static DBManager SINGLETON = null;
	private static final String DATABASE_NAME = "deolho.db3";
	private static final int DATABASE_VERSION = 3;
	private SQLiteDatabase db;
	private static Context context;

	/*
	 * Construtor
	 */
	private DBManager() {
		OpenHelper openHelper = new OpenHelper(this.context);
		this.db = openHelper.getWritableDatabase();
	}

	/*
	 * Singleton
	 */
	public static DBManager getInstance() {
		if (SINGLETON == null) {
			SINGLETON = new DBManager();
		}
		return SINGLETON;
	}

	public SQLiteDatabase getConnection() {
		return db;
	}

	public static void onCreate(Context _context) {
		context = _context;
		SINGLETON = new DBManager();
		Situacao.onCreate(SINGLETON.db);
		Encomenda.onCreate(SINGLETON.db);
	}

	private static class OpenHelper extends SQLiteOpenHelper {
		OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			Log.w("Example", "Upgrading database, this will drop tables and recreate.");
			// db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}

}
