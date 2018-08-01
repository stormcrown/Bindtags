//package com.blozi.blindtags.sqlite
//
//import android.content.Context
//import android.database.DatabaseErrorHandler
//import android.database.sqlite.SQLiteDatabase
//import android.database.sqlite.SQLiteOpenHelper
//
/** 感觉很麻烦，暂时先不做 */
//class BloziDatabaseHelper :SQLiteOpenHelper {
//    private val TAG = "BloziDatabaseHelper"
//    private constructor(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : super(context, name, factory, version)
//    private constructor(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int, errorHandler: DatabaseErrorHandler?) : super(context, name, factory, version, errorHandler)
//
//    companion object {
//        private val DefaultDatabaseName = "BloziDatabase"
//        private val version = 1
//        fun getInstance(context: Context?):BloziDatabaseHelper{
//            return BloziDatabaseHelper(context,DefaultDatabaseName,null,version);
//        }
//    }
//
//
//
//    override fun onCreate(db: SQLiteDatabase?) {
//        createFileCacheTable(db)
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//      //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//
//    private fun createFileCacheTable(db: SQLiteDatabase?){
//        db!!.execSQL(" create table image_cach (id varchar(200) primary key ,type varchar(40) ,path varchar(400) ) ")
//    }
//}