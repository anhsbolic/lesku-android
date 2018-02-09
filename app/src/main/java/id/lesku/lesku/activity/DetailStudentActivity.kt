package id.lesku.lesku.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import id.lesku.lesku.R
import id.lesku.lesku.model.Student
import id.lesku.lesku.utils.Sex
import kotlinx.android.synthetic.main.activity_detail_student.*

class DetailStudentActivity : AppCompatActivity() {

    private lateinit var student: Student

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_student)
        setSupportActionBar(detailStudentToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        detailStudentToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        //Get Data
        if(intent.hasExtra(DATA_STUDENT)){
            student = intent.getParcelableExtra(DATA_STUDENT)
            val sex = "GIRL"
            if(sex == Sex.BOY.toString()){
                detailStudentImg.setImageResource(R.drawable.student_boy)
            }else{
                detailStudentImg.setImageResource(R.drawable.student_girl)
            }
            detailStudentTxtName.text = student.name!!
        }
    }

    companion object {
        val DATA_STUDENT = "DataStudent"
    }
}
