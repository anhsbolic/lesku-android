package id.lesku.lesku.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import id.lesku.lesku.R
import id.lesku.lesku.adapter.DetailStudentFragmentAdapter
import id.lesku.lesku.fragment.StudentsFragment
import id.lesku.lesku.model.Student
import id.lesku.lesku.utils.Sex
import kotlinx.android.synthetic.main.activity_detail_student.*

class DetailStudentActivity : AppCompatActivity() {

    private lateinit var student: Student
    private var isDataUpdated: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_student)
        setSupportActionBar(detailStudentToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        detailStudentToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        title = null

        //Get Data
        if(intent.hasExtra(DATA_STUDENT)){
            student = intent.getParcelableExtra(DATA_STUDENT)
            setDataToUI(student)
        }

        //Set Tab Layout
        val fragmentAdapter = DetailStudentFragmentAdapter(this@DetailStudentActivity,
                supportFragmentManager)
        detailStudentViewPager.adapter = fragmentAdapter
        detailStudentTabLayout.setupWithViewPager(detailStudentViewPager)

    }

    private fun setDataToUI(student: Student) {
        val sex = student.sex!!
        if(sex == Sex.L.toString()){
            detailStudentImg.setImageResource(R.drawable.student_boy)
        }else{
            detailStudentImg.setImageResource(R.drawable.student_girl)
        }
        title = student.name!!
    }

    fun getStudentData(): Student{
        return student
    }

    fun updateStudentDataAndUI(studentData: Student){
        student = studentData
        setDataToUI(student)
        detailStudentCollapsingToolbarLayout.title = student.name!!
        isDataUpdated = true
    }

    override fun onBackPressed() {
        if(isDataUpdated){
            val intent = Intent()
            setResult(StudentsFragment.DETAIL_STUDENT_UPDATED, intent)
            finish()
        }else{
            super.onBackPressed()
        }
    }

    fun deletedStudentResult(){
        val intent = Intent()
        setResult(StudentsFragment.DETAIL_STUDENT_UPDATED, intent)
        finish()
    }

    companion object {
        val DATA_STUDENT = "DataStudent"
    }
}
