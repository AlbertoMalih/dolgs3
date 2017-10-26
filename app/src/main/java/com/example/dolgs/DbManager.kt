package com.example.dolgs


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.AsyncTask
import java.util.*

class DbManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
                "create table " + TABLE_NAME + " " +
                        "(" + COLUMN_ID + " integer primary key, " + COLUMN_DESCRIPTION +
                        " text," + COLUMN_TYPE + " integer," + COLUMN_AGE + " text,"  + COLUMN_NAME_PARTNER + " text," + COLUMN_DATE + " integer)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS contacts")
        onCreate(db)
    }

    fun insertDebt(note: Debt) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_DESCRIPTION, note.description)
        contentValues.put(COLUMN_AGE, note.age)
        contentValues.put(COLUMN_NAME_PARTNER, note.namePartner)
        contentValues.put(COLUMN_TYPE, note.typeDebt.ordinal)
        contentValues.put(COLUMN_DATE, note.date.getTime())
        note.id = db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }

    fun deleteDebt(position: Long) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME,
                COLUMN_ID + " = ?",
                arrayOf(position.toString()))
        db.close()
    }

    fun installAllNotesInListener(getterNotes:GetterDebts) {
        RequestAllUsers(getterNotes).execute()
    }


    private inner class RequestAllUsers internal constructor(val getterNotes: GetterDebts?) : AsyncTask<Void, Debt, Void?>() {
        override fun doInBackground(vararg voids: Void): Void? {
            val db = this@DbManager.readableDatabase
            val allData = db.rawQuery("select * from " + TABLE_NAME, null)
            if (!allData.moveToFirst()) {
                return null
            }
            val descriptionIndex = allData.getColumnIndex(COLUMN_DESCRIPTION)
            val dateIndex = allData.getColumnIndex(COLUMN_DATE)
            val namePartnerIndex = allData.getColumnIndex(COLUMN_NAME_PARTNER)
            val ageIndex = allData.getColumnIndex(COLUMN_AGE)
            val typeIndex = allData.getColumnIndex(COLUMN_TYPE)
            val idIndex = allData.getColumnIndex(COLUMN_ID)
            do {
                val currentNote = Debt(
                        allData.getString(descriptionIndex), allData.getString(namePartnerIndex), Debt.TypeDebt.values()[allData.getInt(typeIndex)],
                         allData.getString(ageIndex), Date(allData.getLong(dateIndex)), allData.getLong(idIndex))
                publishProgress(currentNote)
            } while (allData.moveToNext())
            allData.close()
            return null
        }

        override fun onProgressUpdate(vararg values: Debt) {
            super.onProgressUpdate(*values)
            getterNotes?.nextElement(values[0])
        }

        override fun onPostExecute(v: Void?) {
            super.onPostExecute(v)
            getterNotes?.onComplete()
        }
    }

    companion object {
        private val DATABASE_NAME = "DebtsDb.db"
        private val TABLE_NAME = "debts"
        private val COLUMN_ID = "_id"

        private val COLUMN_DESCRIPTION = "description"
        private val COLUMN_DATE = "date"
        private val COLUMN_NAME_PARTNER = "namePartner"
        private val COLUMN_AGE = "age"
        private val COLUMN_TYPE = "type"

        interface GetterDebts{
            fun nextElement(element: Debt)
            fun onComplete()
        }
    }
}