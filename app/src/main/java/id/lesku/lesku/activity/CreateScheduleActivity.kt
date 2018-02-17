package id.lesku.lesku.activity

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import id.lesku.lesku.R
import id.lesku.lesku.adapter.CreateScheduleDailyNotesAdapter
import id.lesku.lesku.helper.SqliteDbHelper
import id.lesku.lesku.model.ScheduleDailyNotes
import id.lesku.lesku.model.Student
import id.lesku.lesku.utils.DayInBahasa
import id.lesku.lesku.utils.MyDateFormatter
import id.lesku.lesku.utils.ReminderTime
import kotlinx.android.synthetic.main.activity_create_schedule.*
import kotlinx.android.synthetic.main.dialog_set_repetition.*
import java.util.*
import kotlin.collections.ArrayList
import android.widget.DatePicker
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
    private var intWeeksRepetition: Int = 0
    private var listRepetitionDays: ArrayList<String> = ArrayList()
    private var isRepetitionSet: Boolean = false

    private var dataDailyNotes: ArrayList<ScheduleDailyNotes> = ArrayList()
    private var dataDailyNotesPicked: ArrayList<ScheduleDailyNotes> = ArrayList()
    private lateinit var adapterRvDailyNotes: RecyclerView.Adapter<*>
    private lateinit var lmRvDailyNotes: RecyclerView.LayoutManager
    private lateinit var animator: DefaultItemAnimator
    private lateinit var dividerItemDecoration: DividerItemDecoration

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

        lmRvDailyNotes = LinearLayoutManager(this@CreateScheduleActivity)
        createScheduleRvDailyNotes.layoutManager = lmRvDailyNotes
        animator = DefaultItemAnimator()
        createScheduleRvDailyNotes.itemAnimator = animator
        dividerItemDecoration = DividerItemDecoration(this@CreateScheduleActivity,
                DividerItemDecoration.VERTICAL)
        createScheduleRvDailyNotes.addItemDecoration(dividerItemDecoration)
        adapterRvDailyNotes = CreateScheduleDailyNotesAdapter(dataDailyNotesPicked)
        createScheduleRvDailyNotes.adapter = adapterRvDailyNotes

        val scheduleMonday = ScheduleDailyNotes(DayInBahasa.MONDAY.desc, false, null, null)
        val scheduleTuesday = ScheduleDailyNotes(DayInBahasa.TUESDAY.desc, false, null, null)
        val scheduleWednesday = ScheduleDailyNotes(DayInBahasa.WEDNESDAY.desc, false, null, null)
        val scheduleThursday = ScheduleDailyNotes(DayInBahasa.THURSDAY.desc, false, null, null)
        val scheduleFriday = ScheduleDailyNotes(DayInBahasa.FRIDAY.desc, false, null, null)
        val scheduleSaturday = ScheduleDailyNotes(DayInBahasa.SATURDAY.desc, false, null, null)
        val scheduleSunday = ScheduleDailyNotes(DayInBahasa.SUNDAY.desc, false, null, null)
        dataDailyNotes.add(scheduleMonday)
        dataDailyNotes.add(scheduleTuesday)
        dataDailyNotes.add(scheduleWednesday)
        dataDailyNotes.add(scheduleThursday)
        dataDailyNotes.add(scheduleFriday)
        dataDailyNotes.add(scheduleSaturday)
        dataDailyNotes.add(scheduleSunday)

        val dayPicked = MyDateFormatter.getDayBahasa(datePicked)
        for(i in 0 until dataDailyNotes.size){
            dataDailyNotes[i].isChecked = dataDailyNotes[i].day!! == dayPicked
        }
        setRvDailyNotesAdapterData(dataDailyNotes)

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

        createScheduleStartDateLayout.setOnClickListener {
            createScheduleCvDate.requestFocus()
            getDate(datePicked)
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

    private fun setRvDailyNotesAdapterData(dataDailyNotes: ArrayList<ScheduleDailyNotes>) {
        if(dataDailyNotesPicked.isNotEmpty()){
            dataDailyNotesPicked.clear()
        }

        for(i in 0 until dataDailyNotes.size){
            if(dataDailyNotes[i].isChecked!!){
                dataDailyNotesPicked.add(dataDailyNotes[i])
            }
        }

        adapterRvDailyNotes.notifyDataSetChanged()
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

            //update UI
            datePicked = MyDateFormatter.getDate(intDay, intMonth, intYear)
            createScheduleTxtStartDate.text = MyDateFormatter.dateBahasa(datePicked)

            val dayPicked = MyDateFormatter.getDayBahasa(datePicked)
            for(i in 0 until dataDailyNotes.size){
                dataDailyNotes[i].isChecked = dataDailyNotes[i].day!! == dayPicked
            }
            setRvDailyNotesAdapterData(dataDailyNotes)
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

    private fun setRepetition(){
        //set dialog datePicker
        val dialogSetRepetition = Dialog(this@CreateScheduleActivity)
        dialogSetRepetition.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogSetRepetition.setCanceledOnTouchOutside(true)
        dialogSetRepetition.setCancelable(true)
        dialogSetRepetition.setContentView(R.layout.dialog_set_repetition)
        val window = dialogSetRepetition.window
        val param = window.attributes
        param.gravity = Gravity.CENTER
        param.width = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = param
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialogSetRepetition.show()

        //get layout component
        val etWeeks: EditText = dialogSetRepetition.setRepetitionEtWeeks
        val layoutMonday: RelativeLayout = dialogSetRepetition.setRepetitionMondayLayout
        val layoutTuesday: RelativeLayout = dialogSetRepetition.setRepetitionTuesdayLayout
        val layoutWednesday: RelativeLayout = dialogSetRepetition.setRepetitionWednesdayLayout
        val layoutThursday: RelativeLayout = dialogSetRepetition.setRepetitionThursdayLayout
        val layoutFriday: RelativeLayout = dialogSetRepetition.setRepetitionFridayLayout
        val layoutSaturday: RelativeLayout = dialogSetRepetition.setRepetitionSaturdayLayout
        val layoutSunday: RelativeLayout = dialogSetRepetition.setRepetitionSundayLayout
        val cbMonday: CheckBox = dialogSetRepetition.setRepetitionCbMonday
        val cbTuesday: CheckBox = dialogSetRepetition.setRepetitionCbTuesday
        val cbWednesday: CheckBox = dialogSetRepetition.setRepetitionCbWednesday
        val cbThursday: CheckBox = dialogSetRepetition.setRepetitionCbThursday
        val cbFriday: CheckBox = dialogSetRepetition.setRepetitionCbFriday
        val cbSaturday: CheckBox = dialogSetRepetition.setRepetitionCbSaturday
        val cbSunday: CheckBox = dialogSetRepetition.setRepetitionCbSunday
        val btnSetRepetition: Button = dialogSetRepetition.setRepetitionBtnSet

        //Init last data input
        if(intWeeksRepetition > 0){
            etWeeks.setText(intWeeksRepetition.toString())
        }

        val dayPicked = MyDateFormatter.getDay(datePicked)
        Log.d("TES", dayPicked)
        Log.d("TES A", DayInBahasa.SATURDAY.toString())
        when(dayPicked.toLowerCase()){
            DayInBahasa.MONDAY.toString().toLowerCase()->{
                cbMonday.isChecked = true
                cbMonday.isEnabled = false
            }
            DayInBahasa.TUESDAY.toString().toLowerCase()->{
                cbTuesday.isChecked = true
                cbTuesday.isEnabled = false
            }
            DayInBahasa.WEDNESDAY.toString().toLowerCase()->{
                cbWednesday.isChecked = true
                cbWednesday.isEnabled = false
            }
            DayInBahasa.THURSDAY.toString().toLowerCase()->{
                cbThursday.isChecked = true
                cbThursday.isEnabled = false
            }
            DayInBahasa.FRIDAY.toString().toLowerCase()->{
                cbFriday.isChecked = true
                cbFriday.isEnabled = false
            }
            DayInBahasa.SATURDAY.toString().toLowerCase()->{
                cbSaturday.isChecked = true
                cbSaturday.isEnabled = false
            }
            DayInBahasa.SUNDAY.toString().toLowerCase()->{
                cbSunday.isChecked = true
                cbSunday.isEnabled = false
            }
        }

        if(listRepetitionDays.isNotEmpty()){
            for(i in 0 until listRepetitionDays.size){
                when(listRepetitionDays[i]){
                    DayInBahasa.MONDAY.desc ->{
                        cbMonday.isChecked = true
                    }
                    DayInBahasa.TUESDAY.desc ->{
                        cbTuesday.isChecked = true
                    }
                    DayInBahasa.WEDNESDAY.desc ->{
                        cbWednesday.isChecked = true
                    }
                    DayInBahasa.THURSDAY.desc ->{
                        cbThursday.isChecked = true
                    }
                    DayInBahasa.FRIDAY.desc ->{
                        cbFriday.isChecked = true
                    }
                    DayInBahasa.SATURDAY.desc ->{
                        cbSaturday.isChecked = true
                    }
                    DayInBahasa.SUNDAY.desc ->{
                        cbSunday.isChecked = true
                    }
                }
            }
        }

        //UI handling & listener
        layoutMonday.setOnClickListener {
            if(cbMonday.isEnabled){
                cbMonday.isChecked = !cbMonday.isChecked
            }
        }
        layoutTuesday.setOnClickListener {
            if(cbTuesday.isEnabled){
                cbTuesday.isChecked = !cbTuesday.isChecked
            }
        }
        layoutWednesday.setOnClickListener {
            if(cbWednesday.isEnabled){
                cbWednesday.isChecked = !cbWednesday.isChecked
            }
        }
        layoutThursday.setOnClickListener {
            if(cbThursday.isEnabled){
                cbThursday.isChecked = !cbThursday.isChecked
            }
        }
        layoutFriday.setOnClickListener {
            if(cbFriday.isEnabled){
                cbFriday.isChecked = !cbFriday.isChecked
            }
        }
        layoutSaturday.setOnClickListener {
            if(cbSaturday.isEnabled){
                cbSaturday.isChecked = !cbSaturday.isChecked
            }
        }
        layoutSunday.setOnClickListener {
            if(cbSunday.isEnabled){
                cbSunday.isChecked = !cbSunday.isChecked
            }
        }

        btnSetRepetition.setOnClickListener {
            if(etWeeks.text.toString().isNotEmpty()){
                etWeeks.error = null

                var isDayChecked = true
                if(!cbMonday.isChecked && !cbTuesday.isChecked && !cbWednesday.isChecked
                        && !cbThursday.isChecked && !cbFriday.isChecked && !cbSaturday.isChecked
                        && !cbSunday.isChecked){
                    isDayChecked = false
                }

                if(isDayChecked){
                    intWeeksRepetition = etWeeks.text.toString().toInt()

                    if(listRepetitionDays.isNotEmpty()){
                        listRepetitionDays.clear()
                    }

                    if(cbMonday.isChecked){
                        listRepetitionDays.add(DayInBahasa.MONDAY.desc)
                    }
                    if(cbTuesday.isChecked){
                        listRepetitionDays.add(DayInBahasa.TUESDAY.desc)
                    }
                    if(cbWednesday.isChecked){
                        listRepetitionDays.add(DayInBahasa.WEDNESDAY.desc)
                    }
                    if(cbThursday.isChecked){
                        listRepetitionDays.add(DayInBahasa.THURSDAY.desc)
                    }
                    if(cbFriday.isChecked){
                        listRepetitionDays.add(DayInBahasa.FRIDAY.desc)
                    }
                    if(cbSaturday.isChecked){
                        listRepetitionDays.add(DayInBahasa.SATURDAY.desc)
                    }
                    if(cbSunday.isChecked){
                        listRepetitionDays.add(DayInBahasa.SUNDAY.desc)
                    }

                    isRepetitionSet = true

                    //update UI
                    //createScheduleBtnRepetitionReset.visibility = View.VISIBLE
                    var days = listRepetitionDays[0]
                    if(listRepetitionDays.size > 1){
                        for(i in 1 until listRepetitionDays.size){
                            days += ", ${listRepetitionDays[i]}"
                        }
                    }
                    val strRepetition = "Diulang pada hari $days, selama $intWeeksRepetition minggu"
                    //createScheduleTxtRepetition.text = strRepetition

                    dialogSetRepetition.dismiss()
                }else{
                    Toast.makeText(this@CreateScheduleActivity, "pilih hari jadwal akang diulang",
                            Toast.LENGTH_SHORT).show()
                }
            }else{
                etWeeks.error = "akan diulang berapa minggu"
            }
        }
    }

    private fun saveSchedule(){
        Log.d("TES","SAVE")
    }

    private fun showKeyboard(showKeyboard: Boolean){
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(currentFocus!!.windowToken,
                        if (showKeyboard) InputMethodManager.SHOW_FORCED
                        else InputMethodManager.HIDE_NOT_ALWAYS )
    }
}
