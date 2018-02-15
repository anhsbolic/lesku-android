package id.lesku.lesku.activity

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import id.lesku.lesku.R
import id.lesku.lesku.helper.SqliteDbHelper
import id.lesku.lesku.model.Student
import id.lesku.lesku.utils.MyDateFormatter
import kotlinx.android.synthetic.main.activity_create_schedule.*
import kotlinx.android.synthetic.main.dialog_date_picker.*
import kotlinx.android.synthetic.main.dialog_time_picker.*
import java.util.*

class CreateScheduleActivity : AppCompatActivity() {

    private var dataStudents: ArrayList<Student> = ArrayList()
    private var student: Student? = null
    private lateinit var datePicked: Date
    private var startHourPicked: Int = 0
    private var startMinutePicked: Int = 0
    private var endHourPicked: Int = 0
    private var endMinutePicked: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_schedule)
        setTitle(R.string.create_schedule_activity_title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        loadThenSetStudentData()

        createScheduleSearchAutoComplete.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                if(createScheduleSearchAutoComplete.text.toString().isNotEmpty()){
                    createScheduleSearchAutoComplete.error = null
                    if(isStudentRegistered()){
                        Log.d("TES", student!!.id_student!!.toString())
                        createScheduleTxtAddress.isEnabled = false
                        createScheduleTxtAddress.setText(student!!.address!!)
                        createScheduleTxtAddressLayout.visibility = View.VISIBLE
                    }else{
                        createScheduleSearchAutoComplete.error = "Siswa Belum Terdaftar"
                        //TODO : create nav to add student
                    }
                }else{
                    createScheduleSearchAutoComplete.error = "Pilih Siswa"
                }
            }
        }

        datePicked = Date()
        createScheduleTxtDate.text = MyDateFormatter.dateBahasa(datePicked)

        createScheduleDateLayout.setOnClickListener {
            createScheduleCvTime.requestFocus()
            openDatePicker(datePicked)
        }

        startHourPicked = MyDateFormatter.getHourFromDate(Date())
        startMinutePicked = MyDateFormatter.getMinuteFromDate(Date())
        createScheduleTxtStartTime.text = MyDateFormatter.getTime(startHourPicked, startMinutePicked)

        createScheduleStartTimeLayout.setOnClickListener {
            createScheduleCvTime.requestFocus()
            openTimePicker("START_TIME", startHourPicked, startMinutePicked)
        }

        endHourPicked = MyDateFormatter.getHourFromDate(Date())
        endMinutePicked = MyDateFormatter.getMinuteFromDate(Date())
        createScheduleTxtEndTime.text = MyDateFormatter.getTime(endHourPicked, endMinutePicked)

        createScheduleEndTimeLayout.setOnClickListener {
            createScheduleCvTime.requestFocus()
            openTimePicker("END_TIME", endHourPicked, endMinutePicked)
        }

        createScheduleReminderAlarmLayout.setOnClickListener {
            createScheduleCvTime.requestFocus()
            setReminderAlarm()
        }

        createScheduleRepetitionLayout.setOnClickListener {
            createScheduleCvTime.requestFocus()
            setRepetition()
        }

        createScheduleBtnSave.setOnClickListener {
            saveSchedule()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home ->{
                //showKeyboard(false)
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun loadThenSetStudentData() {
        dataStudents = getStudentListFromSQLiteDb()
        if(dataStudents.isNotEmpty()){
            val students = arrayOfNulls<String>(dataStudents.size)
            for(i in 0 until dataStudents.size){
                students[i] = dataStudents[i].name!!
            }
            val studentsAdapter = ArrayAdapter<String>(this@CreateScheduleActivity,
                    R.layout.activity_add_student_spinner_sex_item, students)

            if(createScheduleSearchAutoComplete != null){
                createScheduleSearchAutoComplete.setAdapter(studentsAdapter)
            }
        }
    }

    private fun getStudentListFromSQLiteDb(): ArrayList<Student> {
        val dbStudent = SqliteDbHelper(this@CreateScheduleActivity)
        return dbStudent.getListStudents()
    }

    private fun isStudentRegistered(): Boolean {
        val studentName = createScheduleSearchAutoComplete.text.toString()
        //check the category is new or not
        var isRegistered = false
        for(i in 0 until dataStudents.size){
            if(dataStudents[i].name!!.toLowerCase().trim() == studentName.toLowerCase().trim() ){
                isRegistered = true
                student = dataStudents[i]
                break
            }
        }
        return isRegistered
    }

    private fun openDatePicker(date: Date){
        //set dialog datePicker
        val dialogDatePicker = Dialog(this@CreateScheduleActivity)
        dialogDatePicker.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogDatePicker.setCanceledOnTouchOutside(true)
        dialogDatePicker.setCancelable(true)
        dialogDatePicker.setContentView(R.layout.dialog_date_picker)
        val window = dialogDatePicker.window
        val param = window.attributes
        param.gravity = Gravity.CENTER
        param.width = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = param
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        //show dialog datepicker
        dialogDatePicker.show()

        //get layout component
        val myDatePicker: DatePicker = dialogDatePicker.datePicker
        val btnSetDate: Button = dialogDatePicker.datePickerBtnSetDate

        //set DatePicker date with pickedDate
        val currentYear = MyDateFormatter.getYearFromDate(date)
        val currentMonth = MyDateFormatter.getMonthFromDate(date)
        val currentDay = MyDateFormatter.getDayOfMonthFromDate(date)
        myDatePicker.updateDate(currentYear, currentMonth, currentDay)

        //dialog handling
        btnSetDate.setOnClickListener {
            val intDay = myDatePicker.dayOfMonth
            val intMonth = myDatePicker.month
            val intYear = myDatePicker.year

            //update UI
            datePicked = MyDateFormatter.getDate(intDay, intMonth, intYear)
            createScheduleTxtDate.text = MyDateFormatter.dateBahasa(datePicked)
            dialogDatePicker.dismiss()
        }
    }

    @Suppress("DEPRECATION")
    private fun openTimePicker(whatTime: String ,hour: Int, minute: Int) {
        //set dialog datePicker
        val dialogTimePicker = Dialog(this@CreateScheduleActivity)
        dialogTimePicker.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogTimePicker.setCanceledOnTouchOutside(true)
        dialogTimePicker.setCancelable(true)
        dialogTimePicker.setContentView(R.layout.dialog_time_picker)
        val window = dialogTimePicker.window
        val param = window.attributes
        param.gravity = Gravity.CENTER
        param.width = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = param
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        //show dialog datepicker
        dialogTimePicker.show()

        //get layout component
        val myTimePicker: TimePicker = dialogTimePicker.timePicker
        val btnSetTime: Button = dialogTimePicker.timePickerBtnSetTime

        //set timePicker with 24 hours format
        myTimePicker.setIs24HourView(true)

        //set TimePicker time with pickedTime
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            myTimePicker.hour = hour
            myTimePicker.minute = minute
        }else{
            myTimePicker.currentHour = hour
            myTimePicker.currentMinute = minute
        }

        //dialog handling
        btnSetTime.setOnClickListener {
            val hourPicked = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                myTimePicker.hour
            } else {
                myTimePicker.currentHour
            }

            val minutePicked = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                myTimePicker.minute
            } else {
                myTimePicker.currentMinute
            }

            //update UI
            when(whatTime){
                "START_TIME" ->{
                    startHourPicked = hourPicked
                    startMinutePicked = minutePicked
                    if(startHourPicked <= endHourPicked ){
                        if(startMinutePicked <= endMinutePicked ){
                            createScheduleTxtStartTime.text = MyDateFormatter.getTime(startHourPicked, startMinutePicked)
                            dialogTimePicker.dismiss()
                        }else{
                            createScheduleTxtStartTime.text = MyDateFormatter.getTime(endHourPicked, endMinutePicked)
                            dialogTimePicker.dismiss()
                            val endTime = MyDateFormatter.getTime(endHourPicked,endMinutePicked)
                            val toastMsg = "Pilih kurang dari pukul $endTime"
                            Toast.makeText(this@CreateScheduleActivity,toastMsg,
                                    Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        createScheduleTxtStartTime.text = MyDateFormatter.getTime(endHourPicked, endMinutePicked)
                        dialogTimePicker.dismiss()
                        val startTime = MyDateFormatter.getTime(startHourPicked,startMinutePicked)
                        val toastMsg = "Pilih lebih dari pukul $startTime"
                        Toast.makeText(this@CreateScheduleActivity,toastMsg,
                                Toast.LENGTH_SHORT).show()
                    }
                }
                "END_TIME" ->{
                    endHourPicked = hourPicked
                    endMinutePicked = minutePicked
                    if(endHourPicked >= startHourPicked){
                        if(endMinutePicked >= startMinutePicked){
                            createScheduleTxtEndTime.text = MyDateFormatter.getTime(endHourPicked, endMinutePicked)
                            dialogTimePicker.dismiss()
                        }else{
                            createScheduleTxtEndTime.text = MyDateFormatter.getTime(startHourPicked, startMinutePicked)
                            dialogTimePicker.dismiss()
                            val startTime = MyDateFormatter.getTime(startHourPicked,startMinutePicked)
                            val toastMsg = "Pilih lebih dari pukul $startTime"
                            Toast.makeText(this@CreateScheduleActivity,toastMsg,
                                    Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        createScheduleTxtEndTime.text = MyDateFormatter.getTime(startHourPicked, startMinutePicked)
                        dialogTimePicker.dismiss()
                        val startTime = MyDateFormatter.getTime(startHourPicked,startMinutePicked)
                        val toastMsg = "Pilih lebih dari pukul $startTime"
                        Toast.makeText(this@CreateScheduleActivity,toastMsg,
                                Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setReminderAlarm(){

    }

    private fun setRepetition(){

    }

    private fun saveSchedule(){

    }

    private fun showKeyboard(showKeyboard: Boolean){
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(currentFocus!!.windowToken,
                        if (showKeyboard) InputMethodManager.SHOW_FORCED
                        else InputMethodManager.HIDE_NOT_ALWAYS )
    }
}
