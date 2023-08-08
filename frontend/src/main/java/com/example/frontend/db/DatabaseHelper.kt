package com.example.myexample.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
//출처: https://jw0652.tistory.com/590
// 변경 사항은, 자바 -> 코틀린 형식으로 변경.
// 뷰 객체 선택 방법이 findViewById -> 뷰 바인딩 형식으로 변경.
// 이 외에는 다 동일
class DatabaseHelper(context: Context?) : SQLiteOpenHelper
    (context, DATABASE_NAME, null, 1) {
    // 1번째 매개변수 context -> this 현재 호출이 된 액티비티를 의미.
    // 2번째 매개변수 데이터베이스 파일명 ->
    // 예) DATABASE_NAME, 클래스 변수로 선언해서 상수로 사용중
    // 4번째 매개변수 , 현재 사용하는 버전을 의미하고,
    // 이버전이 변경이 된다면, 아래에 있는 onUpgrade 함수 재 호출됨.
    override fun onCreate(db: SQLiteDatabase) {
        // 앱이 설치후, 해당 클래스가 최초에 실행이 되면,
        // onCreate 함수가 최초 1회 호출.
        db.execSQL("create table " + TABLE_NAME +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, memid TEXT, mempw TEXT, memname TEXT, mememail TEXT)")
    }
    // 이 클래스 사용하면, 지정된 데이터베이스 파일에, 테이블이 생성이 됩니다.
    // 생성된 물리 경로 주소.
    // 에뮬레이터 탐색기 -> data->data -> 패키지명:com.example.test17_crud
    // -> databases , 파일명으로 있음.

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // 위에 SQLiteOpenHelper
        //    (context, DATABASE_NAME, null, 1)
        // 4번째 매개변수가 변경이 될때마다, 실행이 되는 함수.
        // 예를 들어) 버전 ->2 로 변경시 아래에 함수가 실행됨.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    // 데이터베이스 추가하기 insert
    fun     insertData(memid: String?, mempw: String?, memname: String?, mememail: String?): Boolean {
        // 디비 사용시 쓰기, 수정, 삭제 ->writableDatabase 사용함.
        val db = this.writableDatabase
        // execSQL -> 대신에 ContentValues() 를 이용하면
        // SQL  문장 없이, 바로 메서드에 값만 인자로 넣어서
        // 이용하면, 쉽게 insert를 구현가능.
        val contentValues = ContentValues()
        contentValues.put(COL_2, memid)
        contentValues.put(COL_3, mempw)
        contentValues.put(COL_4, memname)
        contentValues.put(COL_5, mememail)
        val result = db.insert(TABLE_NAME, null, contentValues)
        return if (result == -1L) false else true
    }
    // 데이터베이스 추가하기 insert
    fun     selectData(memid: String?): Boolean {
        // 디비 사용시 쓰기, 수정, 삭제 ->writableDatabase 사용함.
        val db = this.writableDatabase
        // execSQL -> 대신에 ContentValues() 를 이용하면
        // SQL  문장 없이, 바로 메서드에 값만 인자로 넣어서
        // 이용하면, 쉽게 insert를 구현가능.
        val contentValues = ContentValues()
        contentValues.put(COL_2, memid)
        val result = db.insert(TABLE_NAME, null, contentValues)
        return if (result == -1L) false else true
    }
    //데이터베이스 항목 읽어오기 Read
    val allData: Cursor
        get() {
            val db = this.writableDatabase
            return db.rawQuery("select * from $TABLE_NAME", null)
        }

    // 데이터베이스 삭제하기
    fun deleteData(id: String): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "ID = ? ", arrayOf(id))
    }

    //데이터베이스 수정하기
    fun updateData(id: String, memid: String?, mempw: String?, memname: String?, mememail: String?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_1, id)
        contentValues.put(COL_2, memid)
        contentValues.put(COL_3, mempw)
        contentValues.put(COL_4, memname)
        contentValues.put(COL_5, mememail)
        db.update(TABLE_NAME, contentValues, "ID = ?", arrayOf(id))
        return true
    }
    //저장
    // 자바(static) , 클래스 변수 -> 클래스명.  접근.
    companion object {
        // DatabaseHelper 클래스명 -> mydb
        // mydb.DATABASE_NAME -> 사용가능.
        const val DATABASE_NAME = "Members.db" // 데이터베이스 명
        const val TABLE_NAME = "Members" // 테이블 명

        // 테이블 항목
        const val COL_1 = "id"
        const val COL_2 = "memid"
        const val COL_3 = "mempw"
        const val COL_4 = "memname"
        const val COL_5 = "mememail"
    }
}