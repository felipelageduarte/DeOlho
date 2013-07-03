package br.usp.icmc.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class Encomenda {
	private static final String TAG = "Encomenda";
	private static String tableName = "Encomenda";
	private String codigo;
	private String hash;

	public Encomenda(String codigo) {
		this.codigo = codigo;
		this.hash = "";
	}

	public Encomenda(String codigo, String hash) {
		this.codigo = codigo;
		this.hash = hash;
	}

	/*
	 * Getters and Setters
	 */

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	/*
	 * SQL
	 */

	public void insert() {
		SQLiteDatabase db = DBManager.getInstance().getConnection();
		String sql = "insert into " + tableName + "(codigo, hash) values (?,?)";
		SQLiteStatement insertStmt = db.compileStatement(sql);
		insertStmt.bindString(1, this.codigo);
		insertStmt.bindString(2, this.hash);
		insertStmt.executeInsert();
	}

	public void update() {
		SQLiteDatabase db = DBManager.getInstance().getConnection();

		ContentValues values = new ContentValues();
		values.put("hash", this.hash);

		db.update(tableName, values, "codigo='" + this.codigo + "'", null);
	}

	public static Encomenda selectByPrimaryKey(String codigo) {
		SQLiteDatabase db = DBManager.getInstance().getConnection();
		return next(db.rawQuery("select codigo, hash from " + tableName
				+ " where codigo = ?", new String[] { codigo }));
	}

	public static Cursor findAll() {
		SQLiteDatabase db = DBManager.getInstance().getConnection();
		return db.rawQuery("select codigo, hash from " + tableName, null);
	}

	public static Encomenda next(Cursor cursor) {
		if (cursor.moveToNext()) {
			Encomenda encomenda = new Encomenda(cursor.getString(0),
					cursor.getString(1));
			return encomenda;
		}
		return null;
	}

	public static void onCreate(SQLiteDatabase db) {
		//db.execSQL("DROP TABLE IF EXISTS " + tableName);
		db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName
				+ " (codigo TEXT PRIMARY KEY, hash TEXT)");
	}

}
