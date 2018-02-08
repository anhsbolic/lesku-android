package id.lesku.lesku.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import id.lesku.lesku.R
import id.lesku.lesku.helper.SqliteDbHelper
import id.lesku.lesku.model.Student
import kotlinx.android.synthetic.main.activity_add_student.*
import java.util.*

class AddStudentActivity : AppCompatActivity() {
    private lateinit var strStudentName: String
    private lateinit var strStudentPhone: String
    private lateinit var strStudentWhatsapp: String
    private lateinit var strStudentAddress: String
    private lateinit var strStudentSchool: String
    private lateinit var strStudentSchoolLevel: String
    private lateinit var strStudentGradeLevel: String
    private lateinit var strStudentSubject: String
    private lateinit var strStudentParentName: String
    private lateinit var strStudentParentPhone: String
    private lateinit var strStudentParentWhatsapp: String
    private lateinit var strStudentParentAddress: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)
        setTitle(R.string.add_student_activity_title)

        addStudentBtnSave.setOnClickListener {
            if(validateStudentData()){
                val student = Student(
                        null,
                        Date(),
                        Date(),
                        strStudentName,
                        strStudentPhone,
                        strStudentWhatsapp,
                        strStudentAddress,
                        strStudentSchool,
                        strStudentSchoolLevel,
                        strStudentGradeLevel,
                        strStudentSubject,
                        strStudentParentName,
                        strStudentParentPhone,
                        strStudentParentWhatsapp,
                        strStudentParentAddress
                )
                addStudentToSQLiteDb(student)
            }
        }
    }

    private fun validateStudentData(): Boolean {
        var valid = true

        //Name
        if(addStudentEtName.text.toString().isEmpty()){
            addStudentEtName.error = "isi nama lengkap siswa"
            valid = false
        }else{
            strStudentName = addStudentEtName.text.toString()
            addStudentEtName.error = null
        }

        //Phone
        if(addStudentEtPhone.text.toString().isEmpty()){
            addStudentEtPhone.error = "isi no telp/HP siswa"
            valid = false
        }else{
            strStudentPhone = addStudentEtPhone.text.toString()
            addStudentEtPhone.error = null
        }

        //Whatsapp
        if(addStudentEtWhatsapp.text.toString().isEmpty()){
            addStudentEtWhatsapp.error = "isi No whatsapp siswa"
            valid = false
        }else{
            strStudentWhatsapp = addStudentEtWhatsapp.text.toString()
            addStudentEtWhatsapp.error = null
        }

        //Address
        if(addStudentEtAddress.text.toString().isEmpty()){
            addStudentEtAddress.error = "isi alamat lengkap siswa"
            valid = false
        }else{
            strStudentAddress = addStudentEtAddress.text.toString()
            addStudentEtAddress.error = null
        }

        //School
        if(addStudentEtSchool.text.toString().isEmpty()){
            addStudentEtSchool.error = "isi nama sekolah/kampus"
            valid = false
        }else{
            strStudentSchool = addStudentEtSchool.text.toString()
            addStudentEtSchool.error = null
        }

        //School Level
        if(addStudentEtSchoolLevel.text.toString().isEmpty()){
            addStudentEtSchoolLevel.error = "isi SD/SMP/SMA/Kuliah"
            valid = false
        }else{
            strStudentSchoolLevel = addStudentEtSchoolLevel.text.toString()
            addStudentEtSchoolLevel.error = null
        }

        //Grade Level
        if(addStudentEtGradeLevel.text.toString().isEmpty()){
            addStudentEtGradeLevel.error = "isi kelas/tingkat berapa"
            valid = false
        }else{
            strStudentGradeLevel = addStudentEtGradeLevel.text.toString()
            addStudentEtGradeLevel.error = null
        }

        //Subject
        if(addStudentEtSubject.text.toString().isEmpty()){
            addStudentEtSubject.error = "isi nama Mata Pelajaran/Mata Kuliah"
            valid = false
        }else{
            strStudentSubject = addStudentEtSubject.text.toString()
            addStudentEtSubject.error = null
        }

        //Parent Name
        if(addStudentEtParentName.text.toString().isEmpty()){
            addStudentEtParentName.error = "isi nama lengkap orang tua siswa"
            valid = false
        }else{
            strStudentParentName = addStudentEtParentName.text.toString()
            addStudentEtParentName.error = null
        }

        //Parent Phone
        if(addStudentEtParentPhone.text.toString().isEmpty()){
            addStudentEtParentPhone.error = "isi no telp/HP orang tua siswa"
            valid = false
        }else{
            strStudentParentPhone = addStudentEtParentPhone.text.toString()
            addStudentEtParentPhone.error = null
        }

        //Parent Whatsapp
        if(addStudentEtParentWhatsapp.text.toString().isEmpty()){
            addStudentEtParentWhatsapp.error = "isi no whatsapp ortu siswa"
            valid = false
        }else{
            strStudentParentWhatsapp = addStudentEtParentWhatsapp.text.toString()
            addStudentEtParentWhatsapp.error = null
        }

        //ParentAddress
        strStudentParentAddress = if(addStudentEtParentAdress.text.toString().isEmpty()){
            strStudentAddress
        }else{
            addStudentEtParentAdress.text.toString()
        }

        //Return
        return valid
    }

    private fun addStudentToSQLiteDb(student: Student) {
        val dbStudent = SqliteDbHelper(this@AddStudentActivity)
        dbStudent.addStudent(student)

    }
}
