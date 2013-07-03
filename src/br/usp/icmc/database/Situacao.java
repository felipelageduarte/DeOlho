package br.usp.icmc.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class Situacao {
	private static String TAG = "Situacao";
	private static String tableName = "Situacao";
	private Encomenda encomenda;
	private Long data;
	private String local;
	private String situacao;
	
	public Situacao(Encomenda encomenda, String data, String local, String situacao) {
		super();
		this.encomenda = encomenda;
		this.setData(data);
		this.local = local;
		this.situacao = situacao;
		Log.d(TAG,"codigo: " + encomenda.getCodigo() + " / data: " + data + " / local: " + local + " / situacao: " + situacao);
	}
	
	public Situacao(Encomenda encomenda, Long data, String local, String situacao) {
		super();
		this.encomenda = encomenda;
		this.setData(data);
		this.local = local;
		this.situacao = situacao;
		Log.d(TAG,"codigo: " + encomenda.getCodigo() + " / data: " + data + " / local: " + local + " / situacao: " + situacao);
	}
	
	/*
	 * Getters and Setters
	 */
	public Encomenda getEncomenda() {
		return encomenda;
	}
	public void setEncomenda(Encomenda encomenda) {
		this.encomenda = encomenda;
	}
	public String getData() {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy kk:mm");
		return (new StringBuilder(format.format(data))).toString();
	}
	
	public void setData(Long data) {
		this.data = data;
	}
	
	public void setData(String data) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy kk:mm");  
			this.data = format.parse(data).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	
	
	/*
	 * SQL
	 */
	
	public long insert() {
		SQLiteDatabase db = DBManager.getInstance().getConnection();
		String sql = "insert into " + tableName + "(codigo, data, local, situacao) values (?,?,?,?)";
		SQLiteStatement insertStmt = db.compileStatement(sql);
		insertStmt.bindString(1, this.encomenda.getCodigo());
		insertStmt.bindLong(2, this.data);
		insertStmt.bindString(3, this.local);
		insertStmt.bindString(4, this.situacao);
		return insertStmt.executeInsert();
	}
	
	public static Cursor findAll() {
		SQLiteDatabase db = DBManager.getInstance().getConnection();
		return db.rawQuery("select codigo, data, local, situacao from " + tableName + " order by data desc", null);
	}
	
	public static Cursor findByCodigo(String codigo){
		SQLiteDatabase db = DBManager.getInstance().getConnection();
		return db.rawQuery("select codigo, data, local, situacao from " + tableName + " where codigo = ?  order by data desc", new String[]{codigo});
	}
	
	public static Situacao next(Cursor cursor){
		if (cursor.moveToNext()) {
			Situacao situacao = new Situacao(Encomenda.selectByPrimaryKey(cursor.getString(0)), 
					cursor.getLong(1),
					cursor.getString(2),
					cursor.getString(3));
			return situacao;			
		}
		return null;
	}
	
	public static void onCreate(SQLiteDatabase db) {
		//db.execSQL("DROP TABLE IF EXISTS " + tableName);
		db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + "(CODIGO VARCHAR(13) NOT NULL, DATA INTEGER NOT NULL, LOCAL TEXT NOT NULL, SITUACAO TEXT NOT NULL, PRIMARY KEY(CODIGO, DATA))");		
	}
}
