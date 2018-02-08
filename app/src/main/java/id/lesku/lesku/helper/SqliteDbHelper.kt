package id.lesku.lesku.helper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import id.lesku.lesku.model.Student
import id.lesku.lesku.utils.MyDateFormatter

class SqliteDbHelper(context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_STUDENTS
                + "("
                + KEY_STUDENT_ID + " INTEGER PRIMARY KEY,"
                + KEY_STUDENT_CREATED_DATE + " TEXT,"
                + KEY_STUDENT_UPDATED_DATE + " TEXT,"
                + KEY_STUDENT_NAME + " TEXT,"
                + KEY_STUDENT_PHONE + " TEXT,"
                + KEY_STUDENT_WHATSAPP + " TEXT,"
                + KEY_STUDENT_ADDRESS + " TEXT,"
                + KEY_STUDENT_SCHOOL + " TEXT,"
                + KEY_STUDENT_SCHOOL_LEVEL + " TEXT,"
                + KEY_STUDENT_GRADE_LEVEL + " TEXT,"
                + KEY_STUDENT_SUBJECT + " TEXT,"
                + KEY_STUDENT_PARENT_NAME + " TEXT,"
                + KEY_STUDENT_PARENT_PHONE + " TEXT,"
                + KEY_STUDENT_PARENT_WHATSAPP + " TEXT,"
                + KEY_STUDENT_PARENT_ADDRESS + " TEXT"
                + ")")
        db.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS)

        // Create tables again
        onCreate(db)
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    fun addStudent(student: Student): Int{
        val db: SQLiteDatabase = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_STUDENT_NAME, student.name!!)
        val createdDate = MyDateFormatter.getDateInFormat(student.created_date!!)
        values.put(KEY_STUDENT_CREATED_DATE, createdDate)
        val updatedDate = MyDateFormatter.getDateInFormat(student.updated_date!!)
        values.put(KEY_STUDENT_UPDATED_DATE, updatedDate)
        values.put(KEY_STUDENT_PHONE, student.phone!!)
        if(student.whatsapp != null){
            values.put(KEY_STUDENT_WHATSAPP, student.whatsapp!!)
        }
        values.put(KEY_STUDENT_ADDRESS, student.address!!)
        values.put(KEY_STUDENT_SCHOOL, student.school!!)
        values.put(KEY_STUDENT_SCHOOL_LEVEL, student.school_level!!)
        values.put(KEY_STUDENT_GRADE_LEVEL, student.grade_level!!)
        values.put(KEY_STUDENT_SUBJECT, student.subject!!)
        values.put(KEY_STUDENT_PARENT_NAME, student.parent_name!!)
        values.put(KEY_STUDENT_PARENT_PHONE, student.parent_phone!!)
        if(student.parent_whatsapp != null){
            values.put(KEY_STUDENT_PARENT_WHATSAPP, student.parent_whatsapp!!)
        }
        values.put(KEY_STUDENT_PARENT_ADDRESS, student.parent_address!!)

        //Inserting Row
        val rowInserted: Long = db.insert(TABLE_STUDENTS, null, values)
        db.close()

        return rowInserted.toInt()
    }

    @SuppressLint("Recycle")
    fun getStudent(idStudent: Int):Student{
        val db: SQLiteDatabase = this.readableDatabase
        val cursor: Cursor = db.query(
                TABLE_STUDENTS,
                arrayOf(KEY_STUDENT_ID,
                        KEY_STUDENT_NAME,
                        KEY_STUDENT_CREATED_DATE,
                        KEY_STUDENT_UPDATED_DATE,
                        KEY_STUDENT_PHONE,
                        KEY_STUDENT_WHATSAPP,
                        KEY_STUDENT_ADDRESS,
                        KEY_STUDENT_SCHOOL,
                        KEY_STUDENT_SCHOOL_LEVEL,
                        KEY_STUDENT_GRADE_LEVEL,
                        KEY_STUDENT_SUBJECT,
                        KEY_STUDENT_PARENT_NAME,
                        KEY_STUDENT_PARENT_PHONE,
                        KEY_STUDENT_PARENT_WHATSAPP,
                        KEY_STUDENT_PARENT_ADDRESS),
                KEY_STUDENT_ID + "=?",
                arrayOf(idStudent.toString()),
                null,
                null,
                null,
                null)
        cursor.moveToFirst()

        val createdDate = MyDateFormatter.getDateFromStringDate(cursor.getString(1))
        val updatedDate = MyDateFormatter.getDateFromStringDate(cursor.getString(2))

        return Student(
                cursor.getString(0).toInt(),
                createdDate,
                updatedDate,
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getString(9),
                cursor.getString(10),
                cursor.getString(11),
                cursor.getString(12),
                cursor.getString(13),
                cursor.getString(14)
        )
    }

    @SuppressLint("Recycle")
    fun getListStudents(): ArrayList<Student>{
        val studentList : ArrayList<Student> = ArrayList()

        val selectQuery = "SELECT * FROM " + TABLE_STUDENTS

        val db: SQLiteDatabase = this.writableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)
        if(cursor.moveToFirst()){
            do {
                val createdDate = MyDateFormatter.getDateFromStringDate(cursor.getString(1))
                val updatedDate = MyDateFormatter.getDateFromStringDate(cursor.getString(2))
                val student = Student(
                        cursor.getString(0).toInt(),
                        createdDate,
                        updatedDate,
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getString(14)
                )
                // Adding contact to list
                studentList.add(student)
            } while (cursor.moveToNext())
        }

        return studentList
    }

    fun updateStudent(student: Student): Int{
        val db: SQLiteDatabase = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_STUDENT_NAME, student.name!!)
        val createdDate = MyDateFormatter.getDateInFormat(student.created_date!!)
        values.put(KEY_STUDENT_CREATED_DATE, createdDate)
        val updatedDate = MyDateFormatter.getDateInFormat(student.updated_date!!)
        values.put(KEY_STUDENT_UPDATED_DATE, updatedDate)
        values.put(KEY_STUDENT_PHONE, student.phone!!)
        if(student.whatsapp != null){
            values.put(KEY_STUDENT_WHATSAPP, student.whatsapp!!)
        }
        values.put(KEY_STUDENT_ADDRESS, student.address!!)
        values.put(KEY_STUDENT_SCHOOL, student.school!!)
        values.put(KEY_STUDENT_SCHOOL_LEVEL, student.school_level!!)
        values.put(KEY_STUDENT_GRADE_LEVEL, student.grade_level!!)
        values.put(KEY_STUDENT_SUBJECT, student.subject!!)
        values.put(KEY_STUDENT_PARENT_NAME, student.parent_name!!)
        values.put(KEY_STUDENT_PARENT_PHONE, student.parent_phone!!)
        if(student.parent_whatsapp != null){
            values.put(KEY_STUDENT_PARENT_WHATSAPP, student.parent_whatsapp!!)
        }
        values.put(KEY_STUDENT_PARENT_ADDRESS, student.parent_address!!)

        //updating row
        return db.update(TABLE_STUDENTS, values, KEY_STUDENT_ID + " = ?",
                arrayOf(student.id_student!!.toString()))
    }

    fun deleteStudent(student: Student){
        val db: SQLiteDatabase = this.writableDatabase
        db.delete(TABLE_STUDENTS, KEY_STUDENT_ID + " = ?",
                arrayOf(student.id_student!!.toString()))
        db.close()
    }

    fun getStudentTotal(): Int{
        val countQuery = "SELECT * FROM " + TABLE_STUDENTS
        val db: SQLiteDatabase = this.readableDatabase
        val cursor: Cursor = db.rawQuery(countQuery, null)
        cursor.close()

        return cursor.count
    }

    /**
     * Companion object
     */

    companion object {
        val DATABASE_VERSION: Int = 1
        val DATABASE_NAME: String = "lesManager"

        //Table student
        val TABLE_STUDENTS: String = "students"
        val KEY_STUDENT_ID: String = "id_student"
        val KEY_STUDENT_CREATED_DATE: String = "created_date"
        val KEY_STUDENT_UPDATED_DATE: String = "updated_date"
        val KEY_STUDENT_NAME: String = "name"
        val KEY_STUDENT_PHONE: String = "phone"
        val KEY_STUDENT_WHATSAPP: String = "whatsapp"
        val KEY_STUDENT_ADDRESS: String = "address"
        val KEY_STUDENT_SCHOOL: String = "school"
        val KEY_STUDENT_SCHOOL_LEVEL: String = "school_level"
        val KEY_STUDENT_GRADE_LEVEL: String = "grade_level"
        val KEY_STUDENT_SUBJECT: String = "subject"
        val KEY_STUDENT_PARENT_NAME: String = "parent_name"
        val KEY_STUDENT_PARENT_PHONE: String = "parent_phone"
        val KEY_STUDENT_PARENT_WHATSAPP: String = "parent_whatsapp"
        val KEY_STUDENT_PARENT_ADDRESS: String = "parent_address"

    }

}