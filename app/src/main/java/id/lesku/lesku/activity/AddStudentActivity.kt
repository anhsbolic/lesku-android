package id.lesku.lesku.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import id.lesku.lesku.R
import id.lesku.lesku.fragment.StudentsFragment
import id.lesku.lesku.helper.SqliteDbHelper
import id.lesku.lesku.model.Student
import id.lesku.lesku.utils.SchoolLevel
import id.lesku.lesku.utils.Sex
import kotlinx.android.synthetic.main.activity_add_student.*
import java.util.*

class AddStudentActivity : AppCompatActivity() {
    private lateinit var strStudentName: String
    private lateinit var strStudentSex: String
    private lateinit var strStudentPhone: String
    private lateinit var strStudentWhatsapp: String
    private var strStudentAddress: String? = null
    private lateinit var strStudentSchool: String
    private lateinit var strStudentSchoolLevel: String
    private lateinit var strStudentGradeLevel: String
    private lateinit var strStudentSubject: String
    private lateinit var strStudentParentName: String
    private lateinit var strStudentParentPhone: String
    private lateinit var strStudentParentWhatsapp: String
    private lateinit var strStudentParentAddress: String

    private var isInEditMode: Boolean = false
    private var student: Student? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)
        setTitle(R.string.add_student_activity_title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //Spinner Sex
        val listSex = arrayOf(Sex.L.toString(), Sex.P.toString())
        val spinnerSexAdapter = ArrayAdapter<String>(this@AddStudentActivity,
                R.layout.activity_add_student_spinner_sex_item, listSex)
        addStudentSpinnerSex.adapter = spinnerSexAdapter
        addStudentSpinnerSex.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                strStudentSex = spinnerSexAdapter.getItem(position)
            }

        }

        //Spinner School Level
        val listSchoolLevel = arrayOf(SchoolLevel.SD.toString(), SchoolLevel.SMP.toString(),
                SchoolLevel.SMA.toString(), SchoolLevel.SMK.toString(),
                SchoolLevel.PRAKULIAH.toString(), SchoolLevel.KULIAH.toString())
        val spinnerSchoolLevelAdapter = ArrayAdapter<String>(this@AddStudentActivity,
                R.layout.activity_add_student_spinner_sex_item, listSchoolLevel)
        addStudentSpinnerSchoolLevel.adapter = spinnerSchoolLevelAdapter
        addStudentSpinnerSchoolLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                strStudentSchoolLevel = spinnerSchoolLevelAdapter.getItem(position)
            }

        }

        //Get Data From Intent, if in Edit Profile Condition
        if(intent.hasExtra(IS_EDIT_MODE)){
            isInEditMode = intent.getBooleanExtra(IS_EDIT_MODE, false)

            if(isInEditMode){
                //update title
                setTitle(R.string.add_student_activity_title_update_profile)

                //get data student
                student = intent.getParcelableExtra(DATA_STUDENT)

                //update UI
                addStudentBtnAdd.visibility = View.GONE
                addStudentBtnSave.visibility = View.VISIBLE
                addStudentEtName.setText(student!!.name!!)
                addStudentEtPhone.setText(student!!.phone!!)
                addStudentEtWhatsapp.setText(student!!.whatsapp!!)
                addStudentEtAddress.setText(student!!.address!!)
                addStudentEtSchool.setText(student!!.school!!)
                addStudentEtGradeLevel.setText(student!!.grade_level!!)
                addStudentEtSubject.setText(student!!.subject!!)
                addStudentEtParentName.setText(student!!.parent_name!!)
                addStudentEtParentPhone.setText(student!!.parent_phone!!)
                addStudentEtParentWhatsapp.setText(student!!.parent_whatsapp!!)
                addStudentEtParentAdress.setText(student!!.parent_address!!)
                val studentSex = student!!.sex!!
                for(i in 0 until listSex.size){
                    if(studentSex == listSex[i]){
                        addStudentSpinnerSex.setSelection(i)
                        break
                    }
                }
                val studentSchoolLevel = student!!.school_level!!
                for(i in 0 until listSchoolLevel.size){
                    if(studentSchoolLevel == listSchoolLevel[i]){
                        addStudentSpinnerSchoolLevel.setSelection(i)
                        break
                    }
                }
            }
        }


        //Btn Listener
        addStudentBtnAdd.setOnClickListener {
            showKeyboard(false)
            if(validateStudentData()){
                val student = Student(
                        null,
                        Date(),
                        Date(),
                        strStudentName,
                        strStudentSex,
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home ->{
                showKeyboard(false)
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
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
        if(addStudentEtParentAdress.text.toString().isEmpty()){
            if(strStudentAddress != null){
                strStudentParentAddress = strStudentAddress!!
            }
        }else{
            strStudentParentAddress = addStudentEtParentAdress.text.toString()
        }

        //Return
        return valid
    }

    private fun addStudentToSQLiteDb(student: Student) {
        val dbStudent = SqliteDbHelper(this@AddStudentActivity)
        val rowInserted = dbStudent.addStudent(student)
        if(rowInserted == -1) {
            Toast.makeText(this@AddStudentActivity, "Gagal Menambahkan Siswa",
                    Toast.LENGTH_SHORT).show()
        }else{
            sendResultToStudentsFragment()
        }
    }

    private fun sendResultToStudentsFragment() {
        val intent = Intent()
        setResult(StudentsFragment.ADD_STUDENT_SUCCESS, intent)
        finish()
    }

    private fun showKeyboard(showKeyboard: Boolean){
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(currentFocus!!.windowToken,
                        if (showKeyboard) InputMethodManager.SHOW_FORCED
                        else InputMethodManager.HIDE_NOT_ALWAYS )
    }

    companion object {
        val IS_EDIT_MODE = "IsEditMode"
        val DATA_STUDENT = "DataStudent"
    }
}
