package com.certis.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by GreenUser on 2015-11-07.
 * SQLiteOpenHelper 클래스는 데이터베이스를 만들거나 열기 위해 필요한 일들을 도와주는 역할을 함.
 * 이미배포된 앱의 DB를 업그레이드 할시 유용함.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String dbName = "certis.db";
    private static final int dbVersion = 1;
    private static final String tbName = "LoginToken";
    SQLiteDatabase db;

    /**
     * @param context : DB를 생성하는 Context, 보통 Main Activity를 전달
     * name : DB 파일의 이름
     * factory : SQLiteDatabase.CursorFactory를 구현한 객체이어야 함
     * 사용자가 직접 커서 객체 생성
     * 표준 커서 이용시 null로 지정
     * version : DB 파일의 버전
     */
    public DBHelper(Context context) {
        super(context, dbName, null, dbVersion);
    }

    /**
     * 데이터베이스가 처음 생성될때 호출됨, 보통 테이블을 생성 및 초기 레코드 삽입 작업을 구현함.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table" + " " + tbName + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," + " " +
                "token TEXT" + ");";
        db.execSQL(sql);
    }

    /**
     *
     * DB의 버전이 변경될 때 호출됨.
     * DB 업그레이드 시 호출됨. 기존 테이블 삭제 및 생성, ALTER TABLE로 스키마 수정
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists" + " " + tbName;
        db.execSQL(sql);
        onCreate(db);
    }

    public String dbSelect() {
        db = this.getReadableDatabase();

        Cursor cursor;
        cursor = db.query("LoginToken", new String[] {"_id", "token"}, "_id = '1'", null, null, null, null);
        String str;

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            str = cursor.getString(1);
        } else {
            str = null;
        }

        if (cursor != null) {
            cursor.close();
        }

        return str;
    }

    public void dbInsert(String token) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("token", token);
        db.insert("LoginToken", null, values);
        db.close();
    }

    public void dbUpdate(String token) {
        db = this.getWritableDatabase();

        if (dbSelect() != null) {
            ContentValues values = new ContentValues();
            values.put("token", token);
            db.update("LoginToken", values, "_id =1", null);
        } else {
            Log.v("dbUpdate", "저장된 토큰을 찾을 수 없습니다.");
        }
        db.close();
    }
}
