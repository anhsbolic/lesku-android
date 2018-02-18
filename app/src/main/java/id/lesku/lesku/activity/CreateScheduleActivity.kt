package id.lesku.lesku.activity

import android.annotation.SuppressLint
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.*
import android.widget.*
import id.lesku.lesku.R
import id.lesku.lesku.helper.SqliteDbHelper
import id.lesku.lesku.model.Student
import id.lesku.lesku.utils.DayInBahasa
import id.lesku.lesku.utils.MyDateFormatter
import id.lesku.lesku.utils.ReminderTime
import kotlinx.android.synthetic.main.activity_create_schedule.*
import java.util.*
import kotlin.collections.ArrayList
import android.widget.DatePicker
import kotlinx.android.synthetic.main.dialog_schedule_notes.view.*
import kotlinx.android.synthetic.main.dialog_set_reminder.view.*


class CreateScheduleActivity : AppCompatActivity() {

    private var dataStudents: ArrayList<Student> = ArrayList()
    private var student: Student? = null
    private lateinit var datePicked: Date
    private var startHourPicked: Int = 0
    private var startMinutePicked: Int = 0
    private var endHourPicked: Int = 0
    private var endMinutePicked: Int = 0
    private var strTimePicked: ReminderTime = ReminderTime.MINUTES
    private var intReminderValue: Int = 10
    private var intReminderTimeInMillis: Int = 0
    private var isReminderSet: Boolean = false
    private var colorTextGray: Int = 0
    private var colorTextPrimary: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_schedule)
        title = null
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.menu_clear)

        //init value
        colorTextGray = ContextCompat.getColor(this@CreateScheduleActivity, R.color.colorTextGray)
        colorTextPrimary = ContextCompat.getColor(this@CreateScheduleActivity, R.color.colorTextPrimary)

        //Init Data
        loadThenSetStudentData()
        datePicked = Date()
        createScheduleTxtStartDate.text = MyDateFormatter.dateBahasa(datePicked)

        //UI Handling & listener
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

        createScheduleTxtStartDate.setOnClickListener {
            getDate(datePicked)
        }

        createScheduleCbMonday.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                createScheduleTxtAddNotesMonday.visibility = View.VISIBLE
                if(createScheduleTxtSubjectMonday.text.toString().isNotEmpty()){
                    createScheduleTxtSubjectMonday.visibility = View.VISIBLE
                }
                if(createScheduleTxtNotesMonday.text.toString().isNotEmpty()){
                    createScheduleTxtNotesMonday.visibility = View.VISIBLE
                }
            }else{
                createScheduleTxtAddNotesMonday.visibility = View.GONE
                createScheduleTxtSubjectMonday.visibility = View.GONE
                createScheduleTxtNotesMonday.visibility = View.GONE
            }
        }

        createScheduleTxtAddNotesMonday.setOnClickListener {
            addNotes(DayInBahasa.MONDAY.desc)
        }

        createScheduleCbTuesday.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                createScheduleTxtAddNotesTuesday.visibility = View.VISIBLE
            }else{
                createScheduleTxtAddNotesTuesday.visibility = View.GONE
            }
        }

        createScheduleTxtAddNotesTuesday.setOnClickListener {
            addNotes(DayInBahasa.TUESDAY.desc)
        }

        createScheduleCbWednesday.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                createScheduleTxtAddNotesWednesday.visibility = View.VISIBLE
            }else{
                createScheduleTxtAddNotesWednesday.visibility = View.GONE
            }
        }

        createScheduleTxtAddNotesWednesday.setOnClickListener {
            addNotes(DayInBahasa.WEDNESDAY.desc)
        }

        createScheduleCbThursday.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                createScheduleTxtAddNotesThursday.visibility = View.VISIBLE
            }else{
                createScheduleTxtAddNotesThursday.visibility = View.GONE
            }
        }

        createScheduleTxtAddNotesThursday.setOnClickListener {
            addNotes(DayInBahasa.THURSDAY.desc)
        }

        createScheduleCbFriday.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                createScheduleTxtAddNotesFriday.visibility = View.VISIBLE
            }else{
                createScheduleTxtAddNotesFriday.visibility = View.GONE
            }
        }

        createScheduleTxtAddNotesFriday.setOnClickListener {
            addNotes(DayInBahasa.FRIDAY.desc)
        }

        createScheduleCbSaturday.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                createScheduleTxtAddNotesSaturday.visibility = View.VISIBLE
            }else{
                createScheduleTxtAddNotesSaturday.visibility = View.GONE
            }
        }

        createScheduleTxtAddNotesSaturday.setOnClickListener {
            addNotes(DayInBahasa.SATURDAY.desc)
        }

        createScheduleCbSunday.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                createScheduleTxtAddNotesSunday.visibility = View.VISIBLE
            }else{
                createScheduleTxtAddNotesSunday.visibility = View.GONE
            }
        }

        createScheduleTxtAddNotesSunday.setOnClickListener {
            addNotes(DayInBahasa.SUNDAY.desc)
        }

        startHourPicked = MyDateFormatter.getHourFromDate(Date())
        startMinutePicked = MyDateFormatter.getMinuteFromDate(Date())
        createScheduleTxtStartTime.text = MyDateFormatter.getTime(startHourPicked, startMinutePicked)

        createScheduleStartTimeLayout.setOnClickListener {
            createScheduleCvTime.requestFocus()
            getTime("START_TIME", startHourPicked, startMinutePicked)
        }

        endHourPicked = MyDateFormatter.getHourFromDate(Date())
        endMinutePicked = MyDateFormatter.getMinuteFromDate(Date())
        createScheduleTxtEndTime.text = MyDateFormatter.getTime(endHourPicked, endMinutePicked)

        createScheduleEndTimeLayout.setOnClickListener {
            createScheduleCvTime.requestFocus()
            getTime("END_TIME", endHourPicked, endMinutePicked)
        }

        createScheduleReminderAlarmLayout.setOnClickListener {
            createScheduleCvTime.requestFocus()
            setAlarm()
        }

        createScheduleBtnReminderReset.setOnClickListener {
            intReminderTimeInMillis = 0
            isReminderSet = false
            createScheduleTxtReminderAlarmTime.text = "set alarm"
            createScheduleBtnReminderReset.visibility = View.GONE
        }
    }

    @SuppressLint("InflateParams")
    private fun addNotes(day: String) {
        val builder = AlertDialog.Builder(this@CreateScheduleActivity)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_schedule_notes, null)
        builder.setView(dialogView)

        val etSubject: EditText = dialogView.dialogScheduleNotesEtSubject
        val etNotes: EditText = dialogView.dialogScheduleNotesEtNotes

        builder.setTitle(day)

        builder.setPositiveButton("Simpan", { _ , _ ->
            val subject = etSubject.text.toString().trim()
            val notes = etNotes.text.toString().trim()
            when(day){
                DayInBahasa.MONDAY.desc->{
                    if(subject.isNotEmpty()){
                        createScheduleTxtSubjectMonday.text = subject
                        createScheduleTxtSubjectMonday.visibility = View.VISIBLE
                    }
                    if(notes.isNotEmpty()){
                        createScheduleTxtNotesMonday.text = notes
                        createScheduleTxtNotesMonday.visibility = View.VISIBLE
                    }
                    createScheduleMondayLayout.requestFocus()
                }
                DayInBahasa.TUESDAY.desc->{
                    if(subject.isNotEmpty()){
                        createScheduleTxtSubjectTuesday.text = subject
                        createScheduleTxtSubjectTuesday.visibility = View.VISIBLE
                    }
                    if(notes.isNotEmpty()){
                        createScheduleTxtNotesTuesday.text = notes
                        createScheduleTxtNotesTuesday.visibility = View.VISIBLE
                    }
                    createScheduleTuesdayLayout.requestFocus()
                }
                DayInBahasa.WEDNESDAY.desc->{
                    if(subject.isNotEmpty()){
                        createScheduleTxtSubjectWednesday.text = subject
                        createScheduleTxtSubjectWednesday.visibility = View.VISIBLE
                    }
                    if(notes.isNotEmpty()){
                        createScheduleTxtNotesWednesday.text = notes
                        createScheduleTxtNotesWednesday.visibility = View.VISIBLE
                    }
                    createScheduleWednesdayLayout.requestFocus()
                }
                DayInBahasa.THURSDAY.desc->{
                    if(subject.isNotEmpty()){
                        createScheduleTxtSubjectThursday.text = subject
                        createScheduleTxtSubjectThursday.visibility = View.VISIBLE
                    }
                    if(notes.isNotEmpty()){
                        createScheduleTxtNotesThursday.text = notes
                        createScheduleTxtNotesThursday.visibility = View.VISIBLE
                    }
                    createScheduleThursdayLayout.requestFocus()
                }
                DayInBahasa.FRIDAY.desc->{
                    if(subject.isNotEmpty()){
                        createScheduleTxtSubjectFriday.text = subject
                        createScheduleTxtSubjectFriday.visibility = View.VISIBLE
                    }
                    if(notes.isNotEmpty()){
                        createScheduleTxtNotesFriday.text = notes
                        createScheduleTxtNotesFriday.visibility = View.VISIBLE
                    }
                    createScheduleFridayLayout.requestFocus()
                }
                DayInBahasa.SATURDAY.desc->{
                    if(subject.isNotEmpty()){
                        createScheduleTxtSubjectSaturday.text = subject
                        createScheduleTxtSubjectSaturday.visibility = View.VISIBLE
                    }
                    if(notes.isNotEmpty()){
                        createScheduleTxtNotesSaturday.text = notes
                        createScheduleTxtNotesSaturday.visibility = View.VISIBLE
                    }
                    createScheduleSaturdayLayout.requestFocus()
                }
                DayInBahasa.SUNDAY.desc->{
                    if(subject.isNotEmpty()){
                        createScheduleTxtSubjectSunday.text = subject
                        createScheduleTxtSubjectSunday.visibility = View.VISIBLE
                    }
                    if(notes.isNotEmpty()){
                        createScheduleTxtNotesSunday.text = notes
                        createScheduleTxtNotesSunday.visibility = View.VISIBLE
                    }
                    createScheduleSundayLayout.requestFocus()
                }
            }
        })

        builder.setNegativeButton("Hapus", { _ , _ ->

        })

        builder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_create_schedulle_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home ->{
                //showKeyboard(false)
                onBackPressed()
                true
            }
            R.id.create_schedule_menu_save ->{
                saveSchedule()
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

    private fun getDate(date: Date){
        val builder = AlertDialog.Builder(this@CreateScheduleActivity)
        val myDatePicker = DatePicker(this@CreateScheduleActivity)
        val currentYear = MyDateFormatter.getYearFromDate(date)
        val currentMonth = MyDateFormatter.getMonthFromDate(date)
        val currentDay = MyDateFormatter.getDayOfMonthFromDate(date)
        myDatePicker.updateDate(currentYear, currentMonth, currentDay)
        builder.setView(myDatePicker)
        builder.setPositiveButton("Set Tanggal", { _ , _ ->
            val intDay = myDatePicker.dayOfMonth
            val intMonth = myDatePicker.month
            val intYear = myDatePicker.year
            datePicked = MyDateFormatter.getDate(intDay, intMonth, intYear)
            createScheduleTxtStartDate.text = MyDateFormatter.dateBahasa(datePicked)
        })

        builder.show()
    }

    @Suppress("DEPRECATION")
    private fun getTime(whatTime: String, hour: Int, minute: Int){
        val builder = AlertDialog.Builder(this@CreateScheduleActivity)
        val myTimePicker = TimePicker(this@CreateScheduleActivity)

        //set TimePicker time with pickedTime
        myTimePicker.setIs24HourView(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            myTimePicker.hour = hour
            myTimePicker.minute = minute
        }else{
            myTimePicker.currentHour = hour
            myTimePicker.currentMinute = minute
        }
        builder.setView(myTimePicker)
        builder.setPositiveButton("Set Waktu", { _ , _ ->
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
                        }else{
                            createScheduleTxtStartTime.text = MyDateFormatter.getTime(endHourPicked, endMinutePicked)
                            val endTime = MyDateFormatter.getTime(endHourPicked,endMinutePicked)
                            val toastMsg = "Pilih kurang dari pukul $endTime"
                            Toast.makeText(this@CreateScheduleActivity,toastMsg,
                                    Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        createScheduleTxtStartTime.text = MyDateFormatter.getTime(endHourPicked, endMinutePicked)
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
                        }else{
                            createScheduleTxtEndTime.text = MyDateFormatter.getTime(startHourPicked, startMinutePicked)
                            val startTime = MyDateFormatter.getTime(startHourPicked,startMinutePicked)
                            val toastMsg = "Pilih lebih dari pukul $startTime"
                            Toast.makeText(this@CreateScheduleActivity,toastMsg,
                                    Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        createScheduleTxtEndTime.text = MyDateFormatter.getTime(startHourPicked, startMinutePicked)
                        val startTime = MyDateFormatter.getTime(startHourPicked,startMinutePicked)
                        val toastMsg = "Pilih lebih dari pukul $startTime"
                        Toast.makeText(this@CreateScheduleActivity,toastMsg,
                                Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        builder.show()
    }

    @SuppressLint("InflateParams")
    private fun setAlarm(){
        val builder = AlertDialog.Builder(this@CreateScheduleActivity)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_set_reminder, null)
        builder.setView(dialogView)

        val etValue: EditText = dialogView.setAlarmEtValue
        val layoutMinutes: RelativeLayout = dialogView.setAlarmInMinutesLayout
        val txtMinutes: TextView = dialogView.setAlarmInMinutesTitle
        val imgMinute: ImageView = dialogView.setAlarmImgInMinutes
        val layoutHour: RelativeLayout = dialogView.setAlarmInHourLayout
        val txtHour: TextView = dialogView.setAlarmInHourTitle
        val imgHour: ImageView = dialogView.setAlarmImgInHour
        val layoutDay: RelativeLayout = dialogView.setAlarmInDayLayout
        val txtDay: TextView = dialogView.setAlarmInDayTitle
        val imgDay: ImageView = dialogView.setAlarmImgInDay

        //init view with last data
        etValue.setText("$intReminderValue")

        when(strTimePicked){
            ReminderTime.MINUTES ->{
                txtMinutes.setTextColor(colorTextPrimary)
                txtHour.setTextColor(colorTextGray)
                txtDay.setTextColor(colorTextGray)
                imgMinute.visibility = View.VISIBLE
                imgHour.visibility = View.GONE
                imgDay.visibility = View.GONE
            }
            ReminderTime.HOUR ->{
                txtMinutes.setTextColor(colorTextGray)
                txtHour.setTextColor(colorTextPrimary)
                txtDay.setTextColor(colorTextGray)
                imgMinute.visibility = View.GONE
                imgHour.visibility = View.VISIBLE
                imgDay.visibility = View.GONE
            }
            ReminderTime.DAY ->{
                strTimePicked = ReminderTime.DAY
                txtMinutes.setTextColor(colorTextGray)
                txtHour.setTextColor(colorTextGray)
                txtDay.setTextColor(colorTextPrimary)
                imgMinute.visibility = View.GONE
                imgHour.visibility = View.GONE
                imgDay.visibility = View.VISIBLE
            }
        }

        //UI handling & listener
        layoutMinutes.setOnClickListener {
            strTimePicked = ReminderTime.MINUTES
            txtMinutes.setTextColor(colorTextPrimary)
            txtHour.setTextColor(colorTextGray)
            txtDay.setTextColor(colorTextGray)
            imgMinute.visibility = View.VISIBLE
            imgHour.visibility = View.GONE
            imgDay.visibility = View.GONE
        }

        layoutHour.setOnClickListener {
            strTimePicked = ReminderTime.HOUR
            txtMinutes.setTextColor(colorTextGray)
            txtHour.setTextColor(colorTextPrimary)
            txtDay.setTextColor(colorTextGray)
            imgMinute.visibility = View.GONE
            imgHour.visibility = View.VISIBLE
            imgDay.visibility = View.GONE
        }

        layoutDay.setOnClickListener {
            strTimePicked = ReminderTime.DAY
            txtMinutes.setTextColor(colorTextGray)
            txtHour.setTextColor(colorTextGray)
            txtDay.setTextColor(colorTextPrimary)
            imgMinute.visibility = View.GONE
            imgHour.visibility = View.GONE
            imgDay.visibility = View.VISIBLE
        }
        builder.setPositiveButton("Set alarm", { _ , _ ->
            if(etValue.text.toString().isNotEmpty()){
                etValue.error = null
                isReminderSet = true
                intReminderValue = etValue.text.toString().toInt()
                val strTime: String
                when(strTimePicked){
                    ReminderTime.MINUTES ->{
                        intReminderTimeInMillis = intReminderValue * 60 * 1000
                        strTime = ReminderTime.MINUTES.desc
                    }
                    ReminderTime.HOUR ->{
                        intReminderTimeInMillis = intReminderValue * 60 * 60 * 1000
                        strTime = ReminderTime.HOUR.desc
                    }
                    ReminderTime.DAY ->{
                        intReminderTimeInMillis = intReminderValue * 24 * 60 * 60 * 1000
                        strTime = ReminderTime.DAY.desc
                    }
                }
                val strAlarmTime = "$intReminderValue $strTime"
                createScheduleTxtReminderAlarmTime.text = strAlarmTime
                createScheduleBtnReminderReset.visibility = View.VISIBLE
            }else{
                etValue.error = "berapa menit/jam/hari sebelum"
            }
        })

        builder.show()
    }

    private fun saveSchedule(){
        Log.d("TES","SAVE")
    }
}
